package xyz.yuanmo.spring.ac;

import xyz.yuanmo.spring.annotation.MattScan;
import java.io.File;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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

    private Class<?> baseConfigClazz;

    Set<String> scanClazzPathSet;

    private String baseScanPath;

    public MattApplicationContext(Class<?> _baseConfigClazz) {
        this.baseConfigClazz = _baseConfigClazz;
        scanClazzPathSet = new HashSet<>(1 << 2);
    }

    public MattApplicationContext() {
    }

    /**
     * 启动应用上下文
     */
    @Override
    public void refresh() {
        scan();
    }

    /**
     * 扫描
     */
    @Override
    public void scan() {

        if (baseConfigClazz.isAnnotationPresent(MattScan.class)) {
            MattScan mattScan = baseConfigClazz.getAnnotation(MattScan.class);
            String singletonPath = mattScan.value();
            System.out.println("scanPath = " + singletonPath);
            URL url = converted2AbsolutePath(singletonPath, baseConfigClazz);
            System.out.println("url = " + url);
            File file = new File(url.getFile());
            dfs(file, scanClazzPathSet, ".class");
            System.out.println("scanClazzPathSet = " + scanClazzPathSet);

        } else {
            throw new RuntimeException("BYD 闹麻了");
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
     * @param clazz
     * @return
     */
    private URL converted2AbsolutePath(String relativePaths, Class<?> clazz) {
        relativePaths = relativePaths.replace(".", "/");
        ClassLoader classLoader = clazz.getClassLoader();
        return classLoader.getResource(relativePaths);
    }

    /**
     * 注册 bean
     *
     * @param clazz 注册 beanClass
     */
    @Override
    public void register(Class<?> clazz) {

        doRegister();
    }

    private void doRegister() {

    }

    /**
     * 依赖查找
     *
     * @return bean 对象
     */
    @Override
    public Object getBean() {
        return null;
    }
}
