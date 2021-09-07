package xyz.yuanmo.test;

import xyz.yuanmo.spring.ac.MattApplicationContext;
import xyz.yuanmo.test.core.BaseConfig;
import xyz.yuanmo.test.core.DataSourceConfig;

/**
 * @author <a href="https://github.com/Matthew-Han">Matthew Han</a>
 * @date 2021/9/3 16:12
 * @since 1.0
 **/
public class TestApplication {

    public static void main(String[] args) throws Exception {
        MattApplicationContext mattApplicationContext = new MattApplicationContext(BaseConfig.class);


        mattApplicationContext.register("User", User.class);
        mattApplicationContext.register("User1", xyz.yuanmo.test.core.User.class);
        mattApplicationContext.refresh();

        System.out.println("dataSourceConfig: " + mattApplicationContext.getBean("dataSourceConfig"));
        System.out.println("User1: " + mattApplicationContext.getBean(xyz.yuanmo.test.core.User.class));

        mattApplicationContext.close();


    }
}
