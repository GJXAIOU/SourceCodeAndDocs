package java.lang;





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
