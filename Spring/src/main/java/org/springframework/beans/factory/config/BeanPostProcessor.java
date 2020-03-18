//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.springframework.beans.factory.config;

import org.springframework.beans.BeansException;
import org.springframework.lang.Nullable;

/**
 * 针对容器中所有的 Bean 或者特定的 Bean 定制初始化过程
 * @author GJXAIOU
 */
public interface BeanPostProcessor {
    /**
     * postProcessBeforeInitialization 方法会在容器中的 Bean 初始化之前执行
     */
    @Nullable
    default Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    /**
     * postProcessAfterInitialization 方法在容器中的 Bean 初始化之后执行
     */

    @Nullable
    default Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
