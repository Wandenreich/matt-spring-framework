/*
 * MIT License
 * Copyright (c) 2021 元末
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package xyz.yuanmo.spring.ac;

import lombok.SneakyThrows;
import xyz.yuanmo.spring.annotation.MattAutowired;
import xyz.yuanmo.spring.annotation.MattComponent;
import xyz.yuanmo.spring.annotation.MattScan;
import xyz.yuanmo.spring.annotation.MattScope;
import xyz.yuanmo.spring.bean.*;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * 启动应用上下文
 *
 * @author <a href="https://github.com/Matthew-Han">Matthew Han</a>
 * @date 2021/9/2 16:39
 * @since 1.0
 **/
public class MattApplicationContext implements AbstractApplicationContext {

    public static class DataBase {

    }

    private final Logger log = Logger.getLogger(this.getClass().getName());

    private Class<?> baseConfigClass;

    private ClassLoader classLoader;

    private Set<String> scanClassPathSet;

    private Set<Class<?>> scanBeanClassSet;

    private Map<String, Class<?>> registerBeanClassMap;

    private Set<MattInitialBeanPostProcessor> mattInitialBeanPostProcessorSet;

    private Set<MattInstanceBeanPostProcessor> mattInstanceBeanPostProcessorSet;

    private Map<String, MattBeanDefinition> mattBeanDefinitionMap;

    private Map<String, Object> mattSingletonBeanMap;

    /**
     * {@link MattFactoryBean} 工具人和真正的 bean 对象的关联关系, 用于依赖查找
     */
    private Map<Class<?>, Class<?>> mattFactoryBeanMap;

    /**
     * {@link MattApplicationContext#scanBeanClassSet} 需要注册的 bean, 仅仅只是扫描路径下的 class
     * {@link MattApplicationContext#registerBeanClassMap} 需要注册的 bean, 包含 {@link MattApplicationContext#register(String, Class)} 的 class
     *
     * @param _baseConfigClazz 配置类
     */
    public MattApplicationContext(Class<?> _baseConfigClazz) {
        this.classLoader = this.getClass().getClassLoader();
        this.baseConfigClass = _baseConfigClazz;

        this.scanClassPathSet = new HashSet<>(1 << 2);
        this.scanBeanClassSet = new HashSet<>(1 << 2);
        this.registerBeanClassMap = new ConcurrentHashMap<>(1 << 2);

        this.mattInitialBeanPostProcessorSet = new HashSet<>(1 << 2);
        this.mattInstanceBeanPostProcessorSet = new HashSet<>(1 << 2);
        this.mattBeanDefinitionMap = new ConcurrentHashMap<>();
        this.mattSingletonBeanMap = new ConcurrentHashMap<>(1 << 2);

        this.mattFactoryBeanMap = new ConcurrentHashMap<>();
    }

    public MattApplicationContext() {
    }

    /**
     * 启动应用上下文
     */
    @SneakyThrows
    @Override
    public void refresh() {
        // 扫描
        scan();

        // 构建 BeanDefinition
        buildBeanDefinition();

        // 执行 bean 的生命周期~
        createBean();

    }

    /**
     * 扫描
     */
    @SneakyThrows
    @Override
    public void scan() {

        log.info("扫描中加载 bean 中...");
        if (baseConfigClass.isAnnotationPresent(MattScan.class)) {
            MattScan mattScan = baseConfigClass.getAnnotation(MattScan.class);
            String singletonPath = mattScan.value();
            URL url = converted2AbsolutePath(singletonPath);
            File file = new File(url.getFile());
            dfs(file, ".class");
            for (String classPath : scanClassPathSet) {
                String className = classPath.substring(classPath.lastIndexOf("classes/") + "classes/".length(), classPath.indexOf(".class"));
                className = className.replace("/", ".");
                Class<?> clazz = classLoader.loadClass(className);
                scanBeanClassSet.add(clazz);
            }
        } else {
            throw new RuntimeException("BYD 闹麻了");
        }
    }

    /**
     * 递归获取 Class 文件绝对路径
     *
     * @param curr 当前 File 对象
     */
    private void dfs(File curr, String suffix) {
        if (curr == null) {
            return;
        }
        if (curr.isDirectory()) {
            for (File next : Objects.requireNonNull(curr.listFiles())) {
                dfs(next, suffix);
            }
        } else if (curr.isFile() && curr.getName().endsWith(suffix)) {
            scanClassPathSet.add(curr.getAbsolutePath());
        }
    }

    /**
     * 获取绝对路径
     *
     * @param relativePaths 绝对路径
     * @return URL
     */
    private URL converted2AbsolutePath(String relativePaths) {
        relativePaths = relativePaths.replace(".", "/");
        return classLoader.getResource(relativePaths);
    }

    /**
     * 构建 beanDefinition
     */
    private void buildBeanDefinition() throws Exception {
        log.info("构建 BeanDefinition 中...");
        for (Class<?> clazz : scanBeanClassSet) {
            // 需要被扫描的注解
            if (clazz.isAnnotationPresent(MattComponent.class)) {
                // 构建 beanDefinition
                MattComponent mattComponent = clazz.getDeclaredAnnotation(MattComponent.class);
                String beanName = mattComponent.value();
                registerBeanClassMap.put(beanName, clazz);
            }
        }
        for (Map.Entry<String, Class<?>> entry : registerBeanClassMap.entrySet()) {
            buildBeanDefinition(entry.getKey(), entry.getValue());
        }
    }

    private void buildBeanDefinition(String beanName, Class<?> clazz) throws Exception {
        // 实现了 beanPostProcessor 的 bean
        if (MattInitialBeanPostProcessor.class.isAssignableFrom(clazz)) {
            mattInitialBeanPostProcessorSet.add((MattInitialBeanPostProcessor) clazz.getDeclaredConstructor().newInstance());
        }

        // 实现了 beanPostProcessor 的 bean
        if (MattInstanceBeanPostProcessor.class.isAssignableFrom(clazz)) {
            mattInstanceBeanPostProcessorSet.add((MattInstanceBeanPostProcessor) clazz.getDeclaredConstructor().newInstance());
        }

        MattBeanDefinition mattBeanDefinition = new MattBeanDefinition();

        // 实现了 FactoryBean 的 bean
        // 所以 getBeanByType 方法无法找到 FactoryBean 的实现类, 只能找到 FactoryBean 创建的 bean 对象的类型
        if (MattFactoryBean.class.isAssignableFrom(clazz)) {
            MattFactoryBean<?> mattFactoryBean = (MattFactoryBean<?>) clazz.getDeclaredConstructor().newInstance();
            MattBeanDefinition.ScopeFactory scopeFactory = mattFactoryBean.isSingleton() ? MattBeanDefinition.ScopeFactory.SCOPE_SINGLETON : MattBeanDefinition.ScopeFactory.SCOPE_PROTOTYPE;
            mattBeanDefinition.setScope(scopeFactory);
            mattBeanDefinition.setBeanClass(clazz);
            mattBeanDefinition.setFactoryBean(true);
            // 建立 MattFactoryBean class 和 clazz 之间的关联关系
            mattFactoryBeanMap.put(mattFactoryBean.getObjectType(), clazz);

        } else {
            MattBeanDefinition.ScopeFactory scopeFactory = clazz.isAnnotationPresent(MattScope.class) ? clazz.getDeclaredAnnotation(MattScope.class).scopeType() : MattBeanDefinition.ScopeFactory.SCOPE_SINGLETON;
            mattBeanDefinition.setScope(scopeFactory);
            mattBeanDefinition.setBeanClass(clazz);
            mattBeanDefinition.setFactoryBean(false);
        }
        mattBeanDefinitionMap.put(beanName, mattBeanDefinition);

    }

    private void createBean() {
        log.info("创建 Bean 中并存入缓存中...");
        mattBeanDefinitionMap.forEach(this::createBean);
        mattSingletonBeanMap.forEach((k, v) -> log.info("已创建的 bean = " + k + " , val = " + v));
    }

    /**
     * 创建 bean 对象
     *
     * @param beanName       beanName
     * @param beanDefinition 描述文件
     * @return 单例 | 原生 的 bean 对象
     */
    private Object createBean(String beanName, MattBeanDefinition beanDefinition) {
        Class<?> beanClass = beanDefinition.getBeanClass();
        try {
            // 准备实例化 BeanDefinition
            Object instance = null;


            // 如果是由 FactoryBean 创建的, 需要从 mattSingletonBeanMap 获取, 不重新实例化
            if (beanDefinition.getFactoryBean()) {
                MattFactoryBean<?> factoryBean = (MattFactoryBean<?>) beanClass.getDeclaredConstructor().newInstance();
                instance = factoryBean.getObject();
            } else {
                if (MattInstanceBeanPostProcessor.class.isAssignableFrom(beanClass)) {
                    // beanPostProcessor 实例化前的处理
                    for (MattInstanceBeanPostProcessor beanPostProcessor : mattInstanceBeanPostProcessorSet) {
                        if (beanPostProcessor.getClass() == beanClass) {
                            instance = beanPostProcessor.postProcessBeforeInstantiation(beanClass, beanName);
                            break;
                        }
                    }
                } else {
                    instance = beanClass.getConstructor().newInstance();
                }

            }

            // 依赖注入
            inject(beanClass, instance);

            // beanPostProcessor 初始化前的处理
            for (MattInitialBeanPostProcessor beanPostProcessor : mattInitialBeanPostProcessorSet) {
                if (beanPostProcessor.getClass() == beanClass) {
                    instance = beanPostProcessor.postProcessBeforeInitialization(instance, beanName);
                    break;
                }
            }

            // 初始化处理
            if (instance instanceof MattInitializingBean) {
                ((MattInitializingBean) instance).afterPropertiesSet();
            }

            // beanPostProcessor 初始化后的处理
            for (MattInitialBeanPostProcessor beanPostProcessor : mattInitialBeanPostProcessorSet) {
                if (beanPostProcessor.getClass() == beanClass) {
                    instance = beanPostProcessor.postProcessAfterInitialization(instance, beanName);
                    break;
                }
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
     * 依赖注入
     *
     * @param beanClass
     * @param instance
     * @throws IllegalAccessException
     */
    private void inject(Class<?> beanClass, Object instance) throws IllegalAccessException {
        // 属于是致敬 AutowiredAnnotationBeanPostProcessor.AutowiredFieldElement#inject() 方法了
        for (Field field : beanClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(MattAutowired.class)) {
                field.setAccessible(true);
                // 将 bean 重新赋值给 instance
                if (null == getBean(field.getName())) {
                    throw new RuntimeException("该 bean 未注册: " + field.getName());
                } else {
                    field.set(instance, getBean(field.getName()));
                }
            }
        }
    }

    /**
     * 注册 bean
     *
     * @param clazz 注册 beanClass
     * @see MattApplicationContext#generateBeanName(Class)
     */
    @Override
    public void register(Class<?> clazz) {
        register(generateBeanName(clazz), clazz);
    }

    /**
     * 注册 bean
     *
     * @param beanName beanName
     * @param clazz    注册 beanClass
     */
    @Override
    public void register(String beanName, Class<?> clazz) {
        registerBeanClassMap.put(beanName, clazz);
    }

    /**
     * 生成 beanName
     *
     * @param clazz beanClass
     * @return beanName
     */
    public String generateBeanName(Class<?> clazz) {
        String tmp = clazz.getName().substring(clazz.getName().lastIndexOf(".") + 1);
        char[] name = tmp.toCharArray();
        name[0] = Character.toLowerCase(name[0]);
        return new String(name);
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
     * @throws Exception e
     */
    @Override
    public Object getBean(Class<?> beanClass) throws Exception {
        // 验证是不是由 mattFactoryBean 创建的 beanClass
        // 是的话经过转换
        Set<Class<?>> beanClassSet = beanClassConvert(beanClass);
        if (beanClassSet.size() > 1) {
            throw new Exception("BYD, 一堆 beans 是吧? 爷的框架没实现 @Primary 啊! 用 getBeansOfType() 方法!");
        }

        Object ans = null;
        int cnt = 0;
        for (Map.Entry<String, MattBeanDefinition> entry : mattBeanDefinitionMap.entrySet()) {
            if (entry.getValue().getBeanClass() == beanClass) {
                ans = getBean(entry.getKey());
                cnt++;
            }
            if (cnt > 1) {
                throw new Exception("BYD, 一堆 beans 是吧? 爷的框架没实现 @Primary 啊! 用 getBeansOfType() 方法!");
            }
        }
        return ans;
    }

    /**
     * beans 依赖查找集合 byType
     *
     * @param beanClass beanClass
     * @return beans 集合
     */
    @Override
    public List<Object> getBeansOfType(Class<?> beanClass) {
        List<Object> ans = new ArrayList<>();
        Set<Class<?>> beanClassSet = beanClassConvert(beanClass);

        for (Class<?> clazz : beanClassSet) {
            for (Map.Entry<String, MattBeanDefinition> entry : mattBeanDefinitionMap.entrySet()) {
                if (entry.getValue().getBeanClass().equals(clazz)) {
                    ans.add(getBean(entry.getKey()));
                }
            }
        }
        return ans;
    }

    /**
     * 验证是不是由 mattFactoryBean 创建的 beanClass
     * 是的话经过转换
     * 最多两个 class
     *
     * @param src 实际需要查找的类
     * @return 0 | 1 | 2 个 class
     */
    private Set<Class<?>> beanClassConvert(Class<?> src) {
        Set<Class<?>> res = new HashSet<>();
        for (Map.Entry<String, MattBeanDefinition> entry : mattBeanDefinitionMap.entrySet()) {
            if (entry.getValue().getBeanClass() == src) {
                res.add(src);
                break;
            }
        }
        res.add(mattFactoryBeanMap.getOrDefault(src, src));
        return res;
    }

    /**
     * 关闭应用上下文
     * bean 的销毁, 但是不一定会被 GC
     */
    @Override
    public void close() {
        log.info("应用上下文正在关闭...");
        scanClassPathSet.clear();
        scanBeanClassSet.clear();
        registerBeanClassMap.clear();
        mattInitialBeanPostProcessorSet.clear();
        mattBeanDefinitionMap.clear();
        mattSingletonBeanMap.clear();
        log.info("Good bye ~");
    }

    /**
     * 打印 mattBeanDefinitionMap 和 mattSingletonBeanMap
     */
    public void printBeanDefinition() {
        mattBeanDefinitionMap.forEach((k, v) -> {
            System.out.println("k = " + k);
            System.out.println("v = " + v);
            System.out.println("------------------");
        });

        System.out.println("==================");

        mattSingletonBeanMap.forEach((k, v) -> {
            System.out.println("k = " + k);
            System.out.println("v = " + v);
            System.out.println("------------------");
        });

        System.out.println("==================");

        mattInstanceBeanPostProcessorSet.forEach(v -> {
            System.out.println("v = " + v);
            System.out.println("------------------");
        });

        mattInitialBeanPostProcessorSet.forEach(v -> {
            System.out.println("v = " + v);
            System.out.println("------------------");
        });
    }
}
