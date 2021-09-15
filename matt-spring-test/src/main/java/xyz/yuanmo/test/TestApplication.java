package xyz.yuanmo.test;

import xyz.yuanmo.spring.ac.MattApplicationContext;
import xyz.yuanmo.test.core.BaseConfig;
import xyz.yuanmo.test.pojo.Book;
import xyz.yuanmo.test.fb.MyFactoryBean;
import xyz.yuanmo.test.pojo.User;

/**
 * @author <a href="https://github.com/Matthew-Han">Matthew Han</a>
 * @date 2021/9/3 16:12
 * @since 1.0
 **/
public class TestApplication {

    public static void main(String[] args) throws Exception {
        MattApplicationContext mattApplicationContext = new MattApplicationContext(BaseConfig.class);

        mattApplicationContext.register(TestApplication.class);
        mattApplicationContext.register("pojoUser", User.class);
        mattApplicationContext.register("externalUser", xyz.yuanmo.test.User.class);
        mattApplicationContext.register(xyz.yuanmo.test.User.class);

        mattApplicationContext.register(MyFactoryBean.class);
        mattApplicationContext.refresh();




        System.out.println("dataSourceConfig: " + mattApplicationContext.getBean("dataSourceConfig"));
        System.out.println("coreUser: " + mattApplicationContext.getBeansOfType(xyz.yuanmo.test.User.class));


        System.out.println("book: " + mattApplicationContext.getBean(Book.class));
        System.out.println("book: " + mattApplicationContext.getBean("myFactoryBean"));


        mattApplicationContext.printBeanDefinition();
        mattApplicationContext.close();


    }
}
