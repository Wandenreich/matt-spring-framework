package xyz.yuanmo.spring.annotation;

import xyz.yuanmo.spring.bean.MattBeanDefinition;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author <a href="https://github.com/Matthew-Han">Matthew Han</a>
 * @date 2021/9/6 11:04
 * @since 1.0
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MattScope {

    MattBeanDefinition.ScopeFactory scopeType() default MattBeanDefinition.ScopeFactory.SCOPE_SINGLETON;
}
