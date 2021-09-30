/*
 * MIT License
 * Copyright (c) 2021 元末
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package xyz.yuanmo.spring.bean;

import xyz.yuanmo.spring.ac.MattApplicationContext;

/**
 * 该接口可以手动创建一个 bean
 * bean 的 beanName 还是与其它注册的 bean 类似
 * bean 的 class 类型会以接口本身的类型构建 MattBeanDefinition
 * 所以在 {@link xyz.yuanmo.spring.ac.MattApplicationContext#getBean(Class)} 中, 需要选择创建的类型
 * 该实现类本身也会存储到容器中, 与创建的对象的类直接获取是同一个东西
 * <p>
 *
 * @author <a href="https://github.com/Matthew-Han">Matthew Han</a>
 * @date 2021/9/6 16:27
 * @see MattApplicationContext#createBean(String, MattBeanDefinition) ()
 * @see MattApplicationContext#buildBeanDefinition(String, Class) ()
 * @see MattApplicationContext#getBean(Class)
 * @see MattApplicationContext#mattFactoryBeanMap
 * @see MattApplicationContext#beanClassConvert(Class)
 * @since 1.0
 **/
public interface MattFactoryBean<T> {


    /**
     * 返回此工厂管理的对象的实例（可能是共享的或独立的)
     *
     * @return bean 的一个实例（可以为null）
     * @throws Exception Exception
     */
    T getObject() throws Exception;


    /**
     * 返回此 FactoryBean 创建的对象类型, 如果事先未知, 则返回null
     * 这允许人们在不实例化对象的情况下检查特定类型的 bean, 例如在自动装配时
     * 在创建单例对象的实现的情况下, 此方法应尽量避免创建单例, 它应该提前估计类型
     * 对于原型, 也建议在此处返回有意义的类型
     *
     * @return 此 FactoryBean 创建的对象类型, 如果在调用时未知, 则为 null
     */
    Class<?> getObjectType();


    /**
     * FactoryBean 本身的单例状态一般由拥有的 BeanFactory 提供
     * 通常, 它必须在那里定义为单例
     * 默认实现返回true, 因为 FactoryBean 通常管理一个单例实例
     *
     * @return 暴露的对象是否是单例
     */
    default boolean isSingleton() {
        return true;
    }

}
