import org.junit.Test;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * @author <a href="https://github.com/Matthew-Han">Matthew Han</a>
 * @date 2021/9/6 15:26
 * @since 1.0
 **/
public class TestAnnotation {

    @Test
    public void fuck() {
        for (Annotation annotation : AnnotationConfig.class.getAnnotations()) {
            if (annotation.getClass().isAssignableFrom(Father.class)) {
                for (Field field : AnnotationConfig.class.getDeclaredFields()) {
                    System.out.println("field.getName() = " + field.getName());

                }
            }
            System.out.println(annotation.getClass().isAssignableFrom(Father.class));
            System.out.println("annotation = " + annotation);
            System.out.println("annotation.annotationType() = " + annotation.getClass().getAnnotatedSuperclass());
        }


    }

}
