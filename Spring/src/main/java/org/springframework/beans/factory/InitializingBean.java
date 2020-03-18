//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.springframework.beans.factory;

/**
 * 通过实现 InitializingBean 接口的 afterPropertiesSet() 方法可以在 Bean 属性值设置好之后做一些操作
 */
public interface InitializingBean {
    void afterPropertiesSet() throws Exception;
}
