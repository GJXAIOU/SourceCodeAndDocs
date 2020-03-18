# README

该仓库记录自己 JDK、Spring、MyBatis 的源码阅读笔记。

## JDK

- 时间：2020.02-2020.04
- 阅读顺序：基础类、简单集合、原子类、同步器、并发集合、多线程

**已完成**：

java

- lang
    - Object.java
    - Thread.java
    - Runnable.java
    - ThreadLocal.java(待定)
    - StringBuffer.java
    - StringBuilder.java
- util
    - ArrayList.java
    - HashMap.java
    - HashSet.java(待定)
    - HashTable.java(待定)
    - LinkedHashMap.java(待定)
    - LinkedList.java
    - Queue.java(待定)
    - Stack.java(待定)
    - TreeMap.java(待定)
    - Vector.java(待定)
    - concurrent
        - locks
        - Lock.java
        - CopyOnWriteArrayList.java
    - ConcurrentHashMap.java（待定）



## Spring

org.springframework.

- beans
    - factory
        - config
            - BeanPostProcessor.java
        - support
            - DefaultSingletonBeanRegistry.java
        - BeanFactory.java
        - BeanNameAware.java
        - InitializingBean.java
        - DisposableBean.java
- context
    - ApplicationContextAware.java
    - ResourceLoaderAware.java
- web
    - context
    - ServletContextAware.java

