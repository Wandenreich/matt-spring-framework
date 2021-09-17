package xyz.yuanmo.test.core;

import xyz.yuanmo.spring.annotation.MattAutowired;
import xyz.yuanmo.spring.bean.MattBeanPostProcessor;
import xyz.yuanmo.spring.annotation.MattBean;
import xyz.yuanmo.spring.annotation.MattComponent;
import xyz.yuanmo.test.User;

/**
 * @author <a href="https://github.com/Matthew-Han">Matthew Han</a>
 * @date 2021/9/6 10:11
 * @since 1.0
 **/
@MattComponent("dataSourceConfig")
public class DataSourceConfig implements MattBeanPostProcessor {


    @MattAutowired
    private User user;

    /**
     * 对 DataSource 初始化前做些处理
     *
     * @param bean     bean
     * @param beanName bean 的 key
     * @return
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        if (bean instanceof DataSourceConfig && "dataSourceConfig".equals(beanName)) {
            System.out.println("DataSourceConfig 初始化前的处理");
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
            return "DataSource{" +
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




