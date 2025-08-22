package xyz.yuanmo.test.core;

import lombok.ToString;
import xyz.yuanmo.spring.annotation.MattAutowired;
import xyz.yuanmo.spring.annotation.MattBean;
import xyz.yuanmo.spring.annotation.MattComponent;
import xyz.yuanmo.spring.bean.MattInitialBeanPostProcessor;
import xyz.yuanmo.spring.bean.MattInstanceBeanPostProcessor;
import xyz.yuanmo.test.User;

/**
 * @author <a href="https://github.com/Matthew-Han">Matthew Han</a>
 * @date 2021/9/6 10:11
 * @since 1.0
 **/
@ToString
@MattComponent("myDataSourceConfig")
public class MyDataSourceConfig implements MattInstanceBeanPostProcessor, MattInitialBeanPostProcessor {


    @MattAutowired
    private User xxx;

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) {
        if (beanClass == MyDataSourceConfig.class && "myDataSourceConfig".equals(beanName)) {
            System.out.println("myDataSourceConfig [实例化]前的处理");
        }
        try {
            return beanClass.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) {
        return MattInstanceBeanPostProcessor.super.postProcessAfterInstantiation(bean, beanName);
    }


    /**
     * 对 DataSource 初始化前做些处理
     *
     * @param bean     bean
     * @param beanName bean 的 key
     * @return
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        if (bean instanceof MyDataSourceConfig && "myDataSourceConfig".equals(beanName)) {
            System.out.println("myDataSourceConfig [初始化]前的处理");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (bean instanceof MyDataSourceConfig && "myDataSourceConfig".equals(beanName)) {
            System.out.println("myDataSourceConfig [初始化]后的处理");
        }
        return bean;
    }

    @MattBean
    public DataSource setDataSource() {
        return new DataSource.Builder()
                .setDatabase("fuck world")
                .setPassword("0x3f3f3f3f0xc0c0c0c0")
                .setPort(886)
                .setUrl("localhost")
                .build();

    }


    public static class DataSource {

        private final String url;

        private final String database;

        private final Integer port;

        private final String password;


        public DataSource(Builder builder) {
            this.url = builder.getUrl();
            this.database = builder.getDatabase();
            this.port = builder.getPort();
            this.password = builder.getPassword();
        }

        @Override
        public String toString() {
            return "MyDataSource{" +
                    "url='" + url + '\'' +
                    ", database='" + database + '\'' +
                    ", port=" + port +
                    ", password='" + password + '\'' +
                    '}';
        }

        public static class Builder {

            private String url;

            private String database;

            private Integer port;

            private String password;


            public String getUrl() {
                return url;
            }

            public Builder setUrl(String url) {
                this.url = url;
                return this;
            }

            public String getDatabase() {
                return database;
            }

            public Builder setDatabase(String database) {
                this.database = database;
                return this;
            }

            public Integer getPort() {
                return port;
            }

            public Builder setPort(Integer port) {
                this.port = port;
                return this;
            }

            public String getPassword() {
                return password;
            }

            public Builder setPassword(String password) {
                this.password = password;
                return this;
            }

            public DataSource build() {
                return new DataSource(this);
            }
        }


    }
}




