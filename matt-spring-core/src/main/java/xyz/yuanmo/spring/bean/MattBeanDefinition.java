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


/**
 * @author <a href="https://github.com/Matthew-Han">Matthew Han</a>
 * @date 2021/9/2 17:36
 * @since 1.0
 **/
public class MattBeanDefinition {

    /**
     * 单例 | 原生
     */
    private ScopeFactory scope;

    /**
     * bean class
     */
    private Class<?> beanClass;


    /**
     * 是否是 factoryBean 对象
     *
     * @see MattFactoryBean
     */
    private boolean isFactoryBean;

    public ScopeFactory getScope() {
        return scope;
    }

    public void setScope(ScopeFactory scope) {
        this.scope = scope;
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    public boolean getFactoryBean() {
        return isFactoryBean;
    }

    public void setFactoryBean(boolean factoryBean) {
        isFactoryBean = factoryBean;
    }

    public enum ScopeFactory {

        /**
         * 单例对象
         */
        SCOPE_SINGLETON,

        /**
         * 原生对象
         */
        SCOPE_PROTOTYPE;
    }

    @Override
    public String toString() {
        return "MattBeanDefinition{" +
                "scope=" + scope +
                ", beanClass=" + beanClass +
                '}';
    }
}
