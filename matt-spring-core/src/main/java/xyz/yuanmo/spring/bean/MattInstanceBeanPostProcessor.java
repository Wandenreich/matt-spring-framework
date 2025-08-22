package xyz.yuanmo.spring.bean;

/**
 * 实例化的 post processor
 *
 * @author <a href="https://github.com/Matthew-Han">Matthew Han</a>
 * @since 2025/8/22 10:24:54
 **/
public interface MattInstanceBeanPostProcessor {


    default Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) {
        return null;
    }

    default boolean postProcessAfterInstantiation(Object bean, String beanName) {
        return true;
    }
}
