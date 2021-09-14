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
package xyz.yuanmo.spring.ac;

import java.util.List;

/**
 * @author <a href="https://github.com/Matthew-Han">Matthew Han</a>
 * @date 2021/9/2 16:51
 * @since 1.0
 **/
public interface AbstractApplicationContext {


    /**
     * 启动应用上下文
     */
    void refresh();

    /**
     * 扫描
     */
    void scan();

    /**
     * 注册 bean
     * 相同的类, 会覆盖上一个, 也就是说只有一个对象, 重复注册相当于向一个 Set 里丢
     *
     * @param clazz 注册 beanClass
     * @see MattApplicationContext#generateBeanName(Class)
     */
    void register(Class<?> clazz);

    /**
     * 注册 bean
     *
     * @param name  beanName
     * @param clazz 注册 beanClass
     */
    void register(String name, Class<?> clazz);

    /**
     * 依赖查找 byName
     *
     * @param beanName beanName
     * @return bean 对象
     */
    Object getBean(String beanName);


    /**
     * 依赖查找 byType
     *
     * @param beanClass beanClass
     * @return bean 对象
     * @throws Exception e
     */
    Object getBean(Class<?> beanClass) throws Exception;


    /**
     * beans 依赖查找集合 byType
     *
     * @param beanClass beanClass
     * @return beans 集合
     */
    List<Object> getBeansOfType(Class<?> beanClass);

    /**
     * 关闭应用上下文
     */
    void close();


}
