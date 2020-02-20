/*
 * Copyright (c) 1994, 2012, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.

 */

package java.lang;

public class Object {

    private static native void registerNatives();

    /**
     *  对象初始化时自动调用此方法
     */
    static {
        registerNatives();
    }

    /**
     * 返回此 Object 的运行时类
     */
    public final native Class<?> getClass();

    /**
     *hashCode 的常规协定是：
     * 1.在 java 应用程序执行期间,在对同一对象多次调用 hashCode()方法时,必须一致地返回相同的整数,前提是将对
     * 象进行 equals 比较时所用的信息没有被修改。从某一应用程序的一次执行到同一应用程序的另一次执行,
     * 该整数无需保持一致。
     *  2.如果根据 equals(object) 方法,两个对象是相等的,那么对这两个对象中的每个对象调用 hashCode 方法都必须
     *  生成相同的整数结果。
     * 3.如果根据 equals(java.lang.Object) 方法,两个对象不相等,那么对这两个对象中的任一对象上调用 hashCode()
     * 方法不要求一定生成不同的整数结果。但是,程序员应该意识到,为不相等的对象生成不同整数结果可以提高哈希表的性能。
     */
    public native int hashCode();

    /**
     *  equals 方法比较的是对象的内存地址
     */
    public boolean equals(Object obj) {
        return (this == obj);
    }

    /**
     * 本地的 clone() 方法，用于对象的复制
     *
     */
    protected native Object clone() throws CloneNotSupportedException;

    /**
     * toString 方法，返回该对象的字符串表示
     * 其中：getClass().getName() 用于获取字节码文件的对应的完整的路径名，例如：java.lang.Object
     * Integer.toHexString(hashCode()) 用于将哈希值转换为 16 进制数格式的字符串
     *
     */
    public String toString() {
        return getClass().getName() + "@" + Integer.toHexString(hashCode());
    }

    /**
     * 用于唤醒一个因为等待该对象（调用了 wait 方法）而被处于等待状态（waiting 或者 time_wait）的线程，
     *     该方法只能在同步方法或者同步块中调用；
     */
    public final native void notify();

    /**
     * 解释同上，区别是用于唤醒所有在因为等待该对象被处于等待状态的线程；
     *
     */
    public final native void notifyAll();

    /**
     * 用于线程调用过程中，导致当前的线程进入等待状态（time_waiting）, 其中 timeout 为等待时间，单位为毫秒，
     *   超过设置时间之后线程会重新进入可运行状态（当得到 CPU 执行权的时候会进入运行状态：见 JavaEEDay24），
     *   同样只能在同步方法或者同步块中调用；
     *
     */
    public final native void wait(long timeout) throws InterruptedException;

    /**
     * 功能：至少等待 timeout 毫秒，nanos 是一个纳秒级的附加时间，用来微调 timeout 参数
     *      内部的具体实现参考 Thread 中的 void sleep(long millis, int nanos) 方法；
     *
     */
    public final void wait(long timeout, int nanos) throws InterruptedException {
        if (timeout < 0) {
            throw new IllegalArgumentException("timeout value is negative");
        }

        if (nanos < 0 || nanos > 999999) {
            throw new IllegalArgumentException(
                                "nanosecond timeout value out of range");
        }

        if (nanos >= 500000 || (nanos != 0 && timeout == 0)) {
            timeout++;
        }

        wait(timeout);
    }

    /**
     * 在其他线程调用此对象的 notify()方法或 notifyAll()方法前,导致当前线程等待。换句话说,此方法的行为就好
     * 像它仅执行wait(0)调用一样。当前线程必须拥有此对象监视器。
     * 该线程发布对此监视器的所有权并等待,直到其他线程通过调用 notify 方法或 notifyAll 方法通知在此对象的监视
     * 器上等待的线程醒来,然后该线程将等到重新获得对监视器的所有权后才能继续执行。
     */
    public final void wait() throws InterruptedException {
        wait(0);
    }

    /**
     * 这个方法用于当对象被回收时调用，这个由 JVM 支持，Object 的 finalize方法默认是什么都没有做，如果子类需要
     * 在对象被回收时执行一些逻辑处理，则可以重写 finalize 方法。
     */
    protected void finalize() throws Throwable { }
}
