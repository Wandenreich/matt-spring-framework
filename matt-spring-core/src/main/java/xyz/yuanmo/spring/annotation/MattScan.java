package xyz.yuanmo.spring.annotation;


import java.lang.annotation.*;

/**
 * @author <a href="https://github.com/Matthew-Han">Matthew Han</a>
 * @date 2021/9/2 16:58
 * @since 1.0
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface MattScan {

    String value();


}
