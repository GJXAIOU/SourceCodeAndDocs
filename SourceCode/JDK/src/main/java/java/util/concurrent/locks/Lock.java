package java.util.concurrent.locks;
import java.util.concurrent.TimeUnit;

/*
 *
 Lock 实现提供了比使用 synchronized 方法和语句更广泛的锁定操作。它们允许更灵活的结构，可能具有完全不同的属性，并且可能支持多个关联的 Condition 对象（也在
 * locks 中，后面讲解）。

**锁是一种工具，用于控制多个线程对共享资源的访问**。通常情况下，锁提供对共享资源的独占访问：一次只有一个线程可以获取锁，**对共享资源的所有访问都要求首先获取锁**。但是，有些锁可能允许并发访问共享资源，例如 ReadWriteLock 的读锁。

> 注：通常情况下， Lock
* 是一种排他性的锁，即同一时刻只能有一个线程拥有这把锁，然后访问该锁控制的资源，如果有其它线程想访问该共享资源则只能等待持有该锁的线程执行完或者抛出异常从而释放该锁，然后去争抢这把锁。
* 当时这种方式对于共享资源划分力度不够，例如通常资源读的次数大于写次数，当多个线程对同一个资源都是读取，本质上都是不需要上锁的，所以通过
* ReadWriteLock，读和写线程分别获取读锁和写锁。具体后续再分析啦。

使用 synchronized
* 方法或语句可以访问与每个对象关联的隐式监视锁，但会强制以块结构的方式获取和释放所有锁：当获取多个锁时，它们必须按相反的顺序释放，所有锁都必须在获得它们的相同词法（作用域）范围内释放。

虽然 synchronized
* 方法和语句的作用域机制使使用监视器锁编程更加容易，并有助于避免许多涉及锁的常见编程错误，但有时需要以更灵活的方式使用锁。例如，一些遍历并发访问数据结构的算法需要使用“head - over
* - head”或“链锁定”：先获取节点 A 的锁，然后获取节点 B，然后释放 A 和获取 C，然后释放 B 和获取D，依此类推。 **Lock
* 接口的实现允许在不同的作用域中获取和释放一个锁，并允许以任何顺序获取和释放多个锁**，从而允许使用此类技术。

随着这种灵活性的增加，也带来了额外的责任。缺少块结构锁定将删除 synchronized 方法和语句所发生的锁的自动释放。在大多数情况下，应使用以下用法：

```java
Lock l = ...;
l.lock();
// 即在 try 中访问锁保护的资源，并且在 finally 中释放锁
try {
   // access the resource protected by this lock
} finally {
   l.unlock();
}
```

当锁定和解锁发生在不同的作用域中时，必须注意确保在锁定期间执行的所有代码都受到 try finally 或 try catch 的保护，以确保在必要时释放锁定。

Lock 实现通过提供一个非阻塞的获取锁的尝试 `tryLock()`，一个获取可以中断的锁的尝试lockInterruptibly，以及试图获取可以超时的锁
* `tryLock（long，TimeUnit)`。

Lock 类还可以提供与隐式监视锁完全不同的行为和语义，例如保证排序、不可重入使用或死锁检测。如果一个实现提供了这样的专门语义，那么该实现必须记录这些语义。

注意 Lock 实例只是普通对象，它们本身可以用作  synchronized 语句中的目标。**获取 lock 实例的监视器锁与调用该实例的任何 lock
* 方法都没有指定的关系**。建议您不要以这种方式使用 Lock 实例，除非在它们自己的实现中。

除非另有说明，否则为任何参数传递 null 值将导致引发 NullPointerException。

<h3>内存同步</h3>

所有的 Lock实现都必须执行内置监视器锁提供的相同内存同步语义，如 [Java 语言规范（17.4内存模型）]( http://docs.oracle
* .com/javase/specs/jls/se7/html/jls-17.html )

成功的 lock 操作与成功的<em>lock</em>操作具有相同的内存同步效果。

成功的 unlock 操作与成功的<em>unlock</em>操作具有相同的内存同步效果。

不成功的锁定和解锁操作以及可重入的锁定/解锁操作不需要任何内存同步效果。

<h3>实现上的注意事项</h3>

锁获取的三种形式（可中断、不可中断和定时）在性能特征、顺序保证或其他实现质量方面可能有所不同。此外，在给定的 lock
* 类中可能无法中断正在进行的获取锁的能力。因此，实现不需要为所有三种锁获取形式定义完全相同的保证或语义，也不需要支持正在进行的锁获取的中断。需要一个实现来清楚地记录每个锁定方法提供的语义和保证。它还必须遵守此接口中定义的中断语义，只要支持锁获取中断：要么完全中断，要么仅中断方法入口。

由于中断通常意味着取消，并且对中断的检查通常是不经常的，所以实现可能倾向于响应中断，而不是普通的方法返回。即使可以显示在另一个操作可能已解除阻止线程之后发生的中断，这也是正确的。实现应该记录此行为。
 *
 * @see ReentrantLock
 * @see Condition
 * @see ReadWriteLock
 *
 * @since 1.5
 * @author Doug Lea
 */
public interface Lock {

    /**
     * Acquires the lock.
     *
     * <p>If the lock is not available then the current thread becomes
     * disabled for thread scheduling purposes and lies dormant until the
     * lock has been acquired.
     *
     * <p><b>Implementation Considerations</b>
     *
     * <p>A {@code Lock} implementation may be able to detect erroneous use
     * of the lock, such as an invocation that would cause deadlock, and
     * may throw an (unchecked) exception in such circumstances.  The
     * circumstances and the exception type must be documented by that
     * {@code Lock} implementation.
     */
    void lock();

    /**
     * 获取锁，除非当前线程是{ thread#interrupt interrupted}。
     * 获取锁（如果可用）并立即返回。
     * 如果锁不可用，则当前线程将被禁用以进行线程调度，并处于休眠状态，直到发生以下两种情况之一：
     * - 锁是由当前线程获取的；
     * - 或者某些其他线程中断当前线程，并且支持锁获取的中断。
     * <p>
     * 如果当前线程：
     * - 在进入此方法时设置了中断状态；
     * - 或者在获取锁时设置了{ thread#interrupt interrupted}，并且支持中断获取锁
     * 则抛出{@link InterruptedException}，并清除当前线程的中断状态。
     * <p>
     * 实施注意事项
     * <p>
     * 在某些实现中中断锁获取的能力可能是不可能的，并且如果可能的话可能是一个昂贵的操作。程序员应该意识到情况可能是这样的。在这种情况下，实现应该记录下来。
     * <p>
     * 实现有利于响应中断，而不是普通的方法返回。
     * <p>
     * {@code Lock}实现可能能够检测到锁的错误使用，例如可能导致死锁的调用，并且在这种情况下可能抛出（未检查的）异常。环境和异常类型必须由{@code Lock}实现记录。
     *
     * @throws InterruptedException if the current thread is
     *                              interrupted while acquiring the lock (and interruption
     *                              of lock acquisition is supported)
     */
    void lockInterruptibly() throws InterruptedException;

    /**
     * 只有在调用时锁是空闲的情况下才获取锁。
     * <p>
     * 获取锁（如果可用），并立即返回值{@code true}。如果锁不可用，则此方法将立即返回值{@code false}。
     *
     * <p>A typical usage idiom for this method would be:
     * <pre> {@code
     * // 定义一个 lock 对象
     * Lock lock = ...;
     * if (lock.tryLock()) {
     *   try {
     *     // manipulate protected state
     *   } finally {
     *     lock.unlock();
     *   }
     * } else {
     *   // perform alternative actions
     * }}</pre>
     * <p>
     * // 此用法确保在获取锁时将其解锁，并且在未获取锁时不会尝试解锁。因为if 之后在 finally 中解锁了。
     *
     * @return {@code true} if the lock was acquired and
     * {@code false} otherwise
     */
    boolean tryLock();

    /**
     * 如果锁在给定的等待时间内空闲并且当前线程未被终中断，则获取该锁。
     * <p>
     * 如果锁可用，则此方法立即返回值 true。
     * <p>
     * 如果锁不可用，则当前线程将被禁用以进行线程调度，并处于休眠状态，直到发生以下三种情况之一：
     * - 锁由当前线程获取；
     * - 另一个线程中断当前线程，并且支持锁获取中断；
     * - 指定的等待时间已过
     * <p>
     * 如果获取了锁，则返回值{@code true}。
     * <p>
     * 如果当前线程：在进入此方法时设置了中断状态；或在获取锁时是中断的，并且支持中断获取锁，然后 InterruptedException 被抛出，当前线程的中断状态被清除。
     * <p>
     * <p>
     * <p>
     * 如果指定的等待时间已过，则值返回 false
     * <p>
     * 如果时间小于或等于零，则该方法根本不会等待。
     * <p>
     * ### 实施考虑1
     * <p>
     * 在某些实现中中断锁获取的能力可能是不可能的，并且如果可能的话可能是一个昂贵的操作。
     * <p>
     * 程序员应该意识到情况可能是这样的。在这种情况下，实现应该记录下来。
     * <p>
     * 实现可以支持响应中断，而不是普通的方法返回，或者报告超时。
     * <p>
     * {@code Lock}实现可能能够检测到锁的错误使用，例如可能导致死锁的调用，并且在这种情况下可能抛出（未检查的）异常。
     * <p>
     * 环境和异常类型必须由{@code Lock}实现记录。
     *
     * @param time the maximum time to wait for the lock
     * @param unit the time unit of the {@code time} argument
     * @return {@code true} if the lock was acquired and {@code false}
     * if the waiting time elapsed before the lock was acquired
     * @throws InterruptedException if the current thread is interrupted
     *                              while acquiring the lock (and interruption of lock
     *                              acquisition is supported)
     */
    boolean tryLock(long time, TimeUnit unit) throws InterruptedException;

    /**
     * Releases the lock.
     *
     * <p><b>Implementation Considerations</b>
     *
     * <p>A {@code Lock} implementation will usually impose
     * restrictions on which thread can release a lock (typically only the
     * holder of the lock can release it) and may throw
     * an (unchecked) exception if the restriction is violated.
     * Any restrictions and the exception
     * type must be documented by that {@code Lock} implementation.
     */
    void unlock();

    /**
     * 返回绑定到此{@code Lock}实例的新{ Condition}实例。
     * 在等待条件之前，锁必须由当前线程持有。
     * 对{ Condition#A wait（）}的调用将在等待之前自动释放锁，并在等待返回之前重新获取锁
     * <p>
     * 实施注意事项
     *
     * @return A new { Condition} instance for this {@code Lock} instance
     * @throws UnsupportedOperationException if this {@code Lock}
     *                                       implementation does not support conditions
     */
    Condition newCondition();
}
