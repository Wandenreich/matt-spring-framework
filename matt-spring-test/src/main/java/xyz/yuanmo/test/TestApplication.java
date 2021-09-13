package xyz.yuanmo.test;

import xyz.yuanmo.spring.ac.MattApplicationContext;
import xyz.yuanmo.test.core.BaseConfig;
import xyz.yuanmo.test.core.DataSourceConfig;
import java.util.Random;

/**
 * @author <a href="https://github.com/Matthew-Han">Matthew Han</a>
 * @date 2021/9/3 16:12
 * @since 1.0
 **/
public class TestApplication {

    public static void main(String[] args) throws Exception {
        MattApplicationContext mattApplicationContext = new MattApplicationContext(BaseConfig.class);

        mattApplicationContext.register(TestApplication.class);
        mattApplicationContext.register("testUser", User.class);
        mattApplicationContext.register("coreUser", xyz.yuanmo.test.core.User.class);
        mattApplicationContext.register(xyz.yuanmo.test.core.User.class);
        mattApplicationContext.refresh();


        mattApplicationContext.printBeanDefinition();

        System.out.println("dataSourceConfig: " + mattApplicationContext.getBean("dataSourceConfig"));
        System.out.println("coreUser: " + mattApplicationContext.getBeansOfType(xyz.yuanmo.test.core.User.class));

        mattApplicationContext.close();


    }
}
