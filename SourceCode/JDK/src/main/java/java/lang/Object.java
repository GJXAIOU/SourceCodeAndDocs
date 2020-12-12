package java.lang;

/**
 * @Author GJXAIOU
 * @Date 2020/2/22 20:27
 */
/*
 * Copyright (c) 1994, 2012, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.

 */

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

    // ---------------下面的 notify()/notifyAll()/wait 方法均与线程同步相关-----------

    /**
     *
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
     使当前线程等待，直到另一个线程调用此对象的 notify（）方法或 notifyAll（）方法，或者指定的时间已过。
     当前线程必须拥有此对象的监视器（锁）。

     此方法会导致当前线程（称为 T 线程）将自身放置在此对象的等待集合中，然后放弃此对象上的任何和所有同步声明（即是释放锁）。线程 T 出于线程调度目的被禁用，并处于休眠状态，直到发生以下四种情况之一：

     - 其他一些线程调用了此对象的 notify 方法，而线程 T 恰好被任意选择为要唤醒的线程（从等待集合中选择一个）。
     - 其他一些线程为此对象调用 notifyAll 方法。
     - 其他一些线程中断了线程 T 。
     - 指定的时间或多或少已经过去。但是，如果参数 timeout 为零，则不考虑实时性，线程只需等待通知。

     然后从该对象的等待集合删除线程 T （因为已经被唤醒了），并重新启用线程调度。然后，它以通常的方式与其他线程去竞争对象上的同步权；一旦它获得了对象的控制权（可以对这个对象同步了），它对对象的所有同步声明都将恢复到原来的状态，也就是说，恢复到调用 wait 方法时的所处的状态。然后线程 T 从 wait方法的调用中返回。因此，从 wait 方法返回时，对象和线程 T 的同步状态与调用 wait 方法时完全相同。

     线程也可以在不被通知、中断或超时的情况下唤醒，即所谓的“虚假唤醒”。虽然这种情况在实践中很少发生，但应用程序必须通过测试本应导致线程被唤醒的条件，并在条件不满足时继续等待来防范这种情况。换句话说，等待应该总是以循环的形式出现，如下所示

     ```java
     // 对 obj 对象同步和上锁
     synchronized (obj) {
     while (<condition does not hold>)
     // 当另一个线程调用 obj 的 notify 方法的时候，正好当前线程就是被唤醒的线程的话，就会从这里唤醒然后执行一系列操作，然后再次判断
     obj.wait(timeout);
     ... // Perform action appropriate to condition
     }
     ```

     如果当前线程在等待之前或等待期间被任何线程中断，则抛出一个 InterruptedException。在还原此对象的锁定状态（如上所述）之前，不会引发此异常。

     注意，wait 方法在将当前线程放入此对象的等待集合中时，只解锁此对象；在线程等待期间，当前线程可能同步的任何其他对象都将保持锁定状态（因为一个线程在执行的时候可能同时调用几个对象的 wait 方法，但是某个时刻通过 notify 方法唤醒线程之后，但是其他对象还保持锁定）。

     此方法只能由作为此对象监视器所有者的线程调用。查看 notify 方法，了解线程成为监视器所有者的方式
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

