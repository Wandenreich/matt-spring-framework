package xyz.yuanmo.spring.bean;

/**
 * @author <a href="https://github.com/Matthew-Han">Matthew Han</a>
 * @date 2021/9/6 10:41
 * @since 1.0
 **/
public interface MattBeanPostProcessor {

    /**
     * 初始化前
     *
     * @param bean     bean
     * @param beanName bean 的 key
     * @return Object
     */
    default Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    /**
     * 初始化后
     *
     * @param bean     bean
     * @param beanName bean 的 key
     * @return Object
     */
    default Object postProcessAfterInitialization(Object bean, String beanName) {
        return bean;
    }

}
