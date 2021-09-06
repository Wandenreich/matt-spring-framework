package xyz.yuanmo.spring.ac;

/**
 * @author <a href="https://github.com/Matthew-Han">Matthew Han</a>
 * @date 2021/9/2 16:51
 * @since 1.0
 **/
public interface AbstractApplicationContext {


    /**
     * 启动应用上下文
     */
    void refresh();

    /**
     * 扫描
     */
    void scan();

    /**
     * 注册 bean
     *
     * @param clazz 注册 beanClass
     */
    void register(Class<?> clazz);

    /**
     * 依赖查找 byName
     *
     * @param beanName beanName
     * @return bean 对象
     */
    Object getBean(String beanName);


    /**
     * 依赖查找 byType
     *
     * @param beanClass beanClass
     * @return bean对象
     */
    Object getBean(Class<?> beanClass);

    /**
     * 关闭应用上下文
     */
    void close();


}
