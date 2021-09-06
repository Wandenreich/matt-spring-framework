package xyz.yuanmo.spring.annotation;

import java.lang.annotation.*;

/**
 * @author <a href="https://github.com/Matthew-Han">Matthew Han</a>
 * @date 2021/9/3 16:15
 * @since 1.0
 **/
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MattBean {

    String[] value() default {};

}
