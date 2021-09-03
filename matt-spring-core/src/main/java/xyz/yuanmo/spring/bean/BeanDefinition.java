package xyz.yuanmo.spring.bean;


/**
 * @author <a href="https://github.com/Matthew-Han">Matthew Han</a>
 * @date 2021/9/2 17:36
 * @since 1.0
 **/
public class BeanDefinition {

    /**
     * 单例 | 原生
     */
    private ScopeFactory scope;

    public ScopeFactory getScope() {
        return scope;
    }

    public void setScope(ScopeFactory scope) {
        this.scope = scope;
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

}
