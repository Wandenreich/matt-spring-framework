package xyz.yuanmo.spring.annotation;

import java.lang.annotation.*;

/**
 * @author <a href="https://github.com/Matthew-Han">Matthew Han</a>
 * @date 2021/9/2 16:56
 * @since 1.0
 **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MattComponent {

    String value() default "";
}
