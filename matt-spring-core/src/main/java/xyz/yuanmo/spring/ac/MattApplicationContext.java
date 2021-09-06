package xyz.yuanmo.spring.ac;

import lombok.extern.slf4j.Slf4j;
import xyz.yuanmo.spring.annotation.MattAutowired;
import xyz.yuanmo.spring.annotation.MattComponent;
import xyz.yuanmo.spring.annotation.MattScan;
import xyz.yuanmo.spring.annotation.MattScope;
import xyz.yuanmo.spring.bean.InitializingBean;
import xyz.yuanmo.spring.bean.MattBeanDefinition;
import xyz.yuanmo.spring.bean.MattBeanPostProcessor;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * 启动类
 *
 * @author <a href="https://github.com/Matthew-Han">Matthew Han</a>
 * @date 2021/9/2 16:39
 * @since 1.0
 **/
public class MattApplicationContext implements AbstractApplicationContext {

    public static class DataBase {

    }

    private final Logger log = Logger.getLogger(this.getClass().getName());


    private Class<?> baseConfigClazz;

    private ClassLoader classLoader;

    private Set<String> scanClassPathSet;

    private String baseScanPath;

    private Set<MattBeanPostProcessor> mattBeanPostProcessorSet;

    private Map<String, MattBeanDefinition> mattBeanDefinitionMap;

    private Map<String, Object> mattSingletonBeanMap;


    public MattApplicationContext(Class<?> _baseConfigClazz) {
        this.classLoader = this.getClass().getClassLoader();
        this.baseConfigClazz = _baseConfigClazz;
        this.scanClassPathSet = new HashSet<>(1 << 2);
        this.mattBeanPostProcessorSet = new HashSet<>(1 << 2);
        this.mattBeanDefinitionMap = new ConcurrentHashMap<>();
        this.mattSingletonBeanMap = new ConcurrentHashMap<>();
    }

    public MattApplicationContext() {
    }

    /**
     * 启动应用上下文
     */
    @Override
    public void refresh() {

        // 扫描
        scan();

        // 构建 BeanDefinition
        buildBeanDefinition();

        // 执行 bean 的生命周期~
        createBean();


    }

    private void createBean() {
        log.info("创建 Bean 中并存入缓存中...");
        mattBeanDefinitionMap.forEach((beanName, mattBeanDefinition) -> {
            createBean(beanName, mattBeanDefinition);
        });
    }

    private Object createBean(String beanName, MattBeanDefinition beanDefinition) {
        Class<?> beanClass = beanDefinition.getBeanClass();

        try {
            // 实例化 BeanDefinition
            Object instance = beanClass.getConstructor().newInstance();

            // 依赖注入
            for (Field field : beanClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(MattAutowired.class)) {
                    field.setAccessible(true);
                    // 将 bean 重新赋值给 instance
                    field.set(instance, getBean(field.getName()));
                }
            }
            // beanPostProcessor 初始化前的处理
            for (MattBeanPostProcessor beanPostProcessor : mattBeanPostProcessorSet) {
                instance = beanPostProcessor.postProcessBeforeInitialization(instance, beanName);
            }

            // 初始化处理
            if (instance instanceof InitializingBean) {
                ((InitializingBean) instance).afterPropertiesSet();
            }

            // beanPostProcessor 初始化后的处理
            for (MattBeanPostProcessor beanPostProcessor : mattBeanPostProcessorSet) {
                instance = beanPostProcessor.postProcessAfterInitialization(instance, beanName);
            }
            if (beanDefinition.getScope() == MattBeanDefinition.ScopeFactory.SCOPE_SINGLETON) {
                mattSingletonBeanMap.put(beanName, instance);
            }
            return instance;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 扫描
     */
    @Override
    public void scan() {

        log.info("扫描中加载 bean 中...");
        if (baseConfigClazz.isAnnotationPresent(MattScan.class)) {
            MattScan mattScan = baseConfigClazz.getAnnotation(MattScan.class);
            String singletonPath = mattScan.value();
            URL url = converted2AbsolutePath(singletonPath);
            File file = new File(url.getFile());
            dfs(file, scanClassPathSet, ".class");

        } else {
            throw new RuntimeException("BYD 闹麻了");
        }
    }

    /**
     * 构建 beanDefinition
     */
    private void buildBeanDefinition() {
        log.info("构建 BeanDefinition 中...");
        for (String classPath : scanClassPathSet) {
            String className = classPath.substring(classPath.indexOf("classes/") + "classes/".length(), classPath.indexOf(".class"));
            className = className.replace("/", ".");
            System.out.println("className = " + className);
            try {
                Class<?> clazz = classLoader.loadClass(className);
                // 需要被扫描的注解
                if (clazz.isAnnotationPresent(MattComponent.class)) {

                    // 实现了 beanPostProcessor 的 class
                    if (clazz.isAssignableFrom(MattBeanPostProcessor.class)) {
                        mattBeanPostProcessorSet.add((MattBeanPostProcessor) clazz.getDeclaredConstructor().newInstance());
                    }


                    // 构建 beanDefinition
                    MattComponent mattComponent = clazz.getDeclaredAnnotation(MattComponent.class);
                    String beanName = mattComponent.value();
                    System.out.println("beanName = " + beanName);

                    MattBeanDefinition mattBeanDefinition = new MattBeanDefinition();
                    MattBeanDefinition.ScopeFactory scopeFactory = clazz.isAnnotationPresent(MattScope.class) ? clazz.getDeclaredAnnotation(MattScope.class).scopeType() : MattBeanDefinition.ScopeFactory.SCOPE_SINGLETON;
                    mattBeanDefinition.setScope(scopeFactory);
                    mattBeanDefinition.setBeanClass(clazz);

                    mattBeanDefinitionMap.put(beanName, mattBeanDefinition);


                }
            } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 递归获取 Class 文件绝对路径
     *
     * @param curr
     * @param scanClazzPathSet
     */
    private void dfs(File curr, Set<String> scanClazzPathSet, String suffix) {
        if (curr == null) {
            return;
        }
        if (curr.isDirectory()) {
            for (File next : curr.listFiles()) {
                dfs(next, scanClazzPathSet, suffix);
            }
        } else if (curr.isFile() && curr.getName().endsWith(suffix)) {
            scanClazzPathSet.add(curr.getAbsolutePath());
        }
    }

    /**
     * 获取绝对路径
     *
     * @param relativePaths
     * @return
     */
    private URL converted2AbsolutePath(String relativePaths) {
        relativePaths = relativePaths.replace(".", "/");
        return classLoader.getResource(relativePaths);
    }

    /**
     * 注册 bean
     *
     * @param clazz 注册 beanClass
     */
    @Override
    public void register(Class<?> clazz) {
    }


    /**
     * 依赖查找 byName
     *
     * @param beanName beanName
     * @return bean 对象
     */
    @Override
    public Object getBean(String beanName) {
        if (mattBeanDefinitionMap.containsKey(beanName)) {
            MattBeanDefinition curr = mattBeanDefinitionMap.get(beanName);
            return curr.getScope() == MattBeanDefinition.ScopeFactory.SCOPE_SINGLETON ? mattSingletonBeanMap.get(beanName) : createBean(beanName, curr);
        }
        return null;
    }

    /**
     * 依赖查找 byType
     *
     * @param beanClass beanClass
     * @return bean对象
     */
    @Override
    public Object getBean(Class<?> beanClass) {

        return null;
    }

    /**
     * 关闭应用上下文
     */
    @Override
    public void close() {

        log.info("应用上下文正在关闭...");
        mattBeanDefinitionMap.clear();
        mattSingletonBeanMap.clear();
        mattBeanPostProcessorSet.clear();
        scanClassPathSet.clear();
        log.info("Good bye ~");

    }
}
