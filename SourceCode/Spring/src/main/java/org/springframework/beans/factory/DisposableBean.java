//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.springframework.beans.factory;

/**
 * 实现 DisposableBean 接口的 destroy() 方法可以在销毁 Bean 之前做一些操作。
 */
public interface DisposableBean {
    void destroy() throws Exception;
}
