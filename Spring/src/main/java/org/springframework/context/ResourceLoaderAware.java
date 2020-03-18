//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.springframework.context;

import org.springframework.beans.factory.Aware;
import org.springframework.core.io.ResourceLoader;

/**
 *  获得ResourceLoader对象，可以获得classpath中某个文件。
 *
 */
public interface ResourceLoaderAware extends Aware {
    void setResourceLoader(ResourceLoader var1);
}
