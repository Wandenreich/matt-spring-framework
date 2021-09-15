package xyz.yuanmo.test.fb;

import xyz.yuanmo.spring.bean.MattFactoryBean;
import xyz.yuanmo.test.pojo.Book;

/**
 * @author <a href="https://github.com/Matthew-Han">Matthew Han</a>
 * @date 2021/9/15 16:03
 * @since 1.0
 **/
public class MyMattFactoryBean implements MattFactoryBean<Book> {

    /**
     * 返回此工厂管理的对象的实例（可能是共享的或独立的)
     *
     * @return bean 的一个实例（可以为null）
     * @throws Exception Exception
     */
    @Override
    public Book getObject() throws Exception {
        return new Book("<丫丫历险记>", "$6326");
    }

    /**
     * 返回此 FactoryBean 创建的对象类型, 如果事先未知, 则返回null
     * 这允许人们在不实例化对象的情况下检查特定类型的 bean, 例如在自动装配时
     * 在创建单例对象的实现的情况下, 此方法应尽量避免创建单例, 它应该提前估计类型
     * 对于原型, 也建议在此处返回有意义的类型
     *
     * @return 此 FactoryBean 创建的对象类型, 如果在调用时未知, 则为 null
     */
    @Override
    public Class<?> getObjectType() {
        return Book.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
