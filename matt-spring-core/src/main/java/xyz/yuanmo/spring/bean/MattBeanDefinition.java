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
