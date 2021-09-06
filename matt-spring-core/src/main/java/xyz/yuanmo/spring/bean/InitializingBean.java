package xyz.yuanmo.spring.bean;

/**
 * @author <a href="https://github.com/Matthew-Han">Matthew Han</a>
 * @date 2021/9/6 16:30
 * @since 1.0
 **/
public interface InitializingBean {

    /**
     * 此方法允许 bean 实例在设置所有 bean 属性后执行其整体配置和最终初始化的验证。
     *
     * @throws Exception
     */
    void afterPropertiesSet() throws Exception;
}
