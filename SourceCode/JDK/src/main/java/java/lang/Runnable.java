package java.lang;


/**
 * The Runnable interface should be implemented by any class whose instances are intended to be
 * executed by a thread. The class must define a method of no arguments called run.
 * This interface is designed to provide a common protocol for objects that wish to execute code
 * while they are active. For example, Runnable is implemented by class Thread. Being active
 * simply means that a thread has been started and has not yet been stopped.
 * In addition, Runnable provides the means for a class to be active while not subclassing Thread
 * . A class that implements Runnable can run without subclassing Thread by instantiating a
 * Thread instance and passing itself in as the target. In most cases, the Runnable interface
 * should be used if you are only planning to override the run() method and no other Thread
 * methods. This is important because classes should not be subclassed unless the programmer
 * intends on modifying or enhancing the fundamental behavior of the class.
 * Since:
 * JDK1.0
 * See Also:
 * Thread, java.util.concurrent.Callable
 * Author:
 * Arthur van Hoff
 */

/**
 `Runnable` 接口应该由其实例要由线程执行的任何类实现。类必须定义一个没有参数的 run 方法。

 此接口旨在为希望在活动时执行代码的对象提供通用协议。例如，Thread 类实现了 Runnable 接口。

 **处于活动状态只意味着线程已启动但尚未停止**。

 此外，`Runnable` 提供了一种方法，使类在不子类化 `Thread` 类的情况下处于活动状态。
 实现 Runnable 接口的类可以通过实例化 Thread 实例并将其自身作为目标传入而运行，而无需子类化 Thread 类。在大多数情况下，
 如果您只计划重写 run（）方法，而不打算重写其他 Thread 类的方法，则应使用  Runnable 接口。
 这一点很重要，因为除非程序员打算修改或增强类的基本行为，否则不应该对类进行子类化（即不应该继承）。
 *
 * @author  Arthur van Hoff
 * @see     java.lang.Thread
 * @see     java.util.concurrent.Callable
 * @since   JDK1.0
 */
@FunctionalInterface
public interface Runnable {
    /**
     * 当实现 Runnable 接口的对象被用于创建一个线程的时候，启动该线程的时候会导致该对象的 run 方法在独立执行的线程中被调用。
     *
     * @see     java.lang.Thread#run()
     */
    public abstract void run();
}
