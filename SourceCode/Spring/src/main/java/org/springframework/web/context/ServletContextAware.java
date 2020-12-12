//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.springframework.web.context;

import javax.servlet.ServletContext;
import org.springframework.beans.factory.Aware;

/**
 * @author GJXAIOU
 * 在一个MVC应用中可以获取ServletContext对象，可以读取context中的参数。
 */
public interface ServletContextAware extends Aware {
    void setServletContext(ServletContext var1);
}
