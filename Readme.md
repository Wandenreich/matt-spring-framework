# Matt-Spring-Framework(mini Spring) ![http://openjdk.java.net/projects/jdk/11/](https://img.shields.io/badge/Java-11.0.9-brightgreen)

> 风是从哪儿来手上的狗尾巴草摇的更剧烈
> 
> 稻穗也晃起来我紧握着你的手把它拍成照片

## 0x00

默认 SDK 基于 Java 11

**目前以实现的功能:**

- [x] `@Autowired` 注解字段自动注入 Bean
- [x] `@Component` 注解实现扫描注册当前类成为 Bean
- [x] `@ComponentScan` 注解实现递归扫描当前路径下的所有需要注册的类
- [x] `register` 方法手动注册 Bean
- [x] `getBeanByName` 根据 `beanName` 依赖查找 Bean
- [x] `getBeanByType` 根据类型依赖查找 Bean
- [x] `getBeansOfType` 根据类型依赖查找 Beans 集合
- [x] `BeanPostProcessor` 初始化后置处理器
- [x] `InitializingBean` 接口实现 Bean 的初始化
- [x] `BeanDefinition` 根据 `scope` 创建单例或原生的 Bean

**待实现的功能:**

- [ ] `Servlet`相关
- [ ] 简易 JDBC 封装(类似 `mybatis-spring`)
- [ ] `@Bean` 注解实现自动注册装配 Bean
- [ ] `FactoryBean` 接口的相关实现

**暂时无法解决的问题:**

- [ ] 循环依赖问题
- [ ] 部分操作线程非安全

## 0x01

Spring 推荐学习视频:

> 小白级别

0. [极客时间 - 小马哥讲Spring核心编程思想](https://time.geekbang.org/course/intro/100042601) (需要付费解锁, 讲得较为全面但是没有特别区分重点非重点, 需要突击面试的情况的话需要自己取舍.)
1. [图灵教育公开课](https://www.bilibili.com/video/BV1dK4y127mH?p=64) (免费的公开课, 非常的浅, 比较适合对 Spring 完全没概念的小白, 学习之后会对 Spring 有一个大概的认知.)