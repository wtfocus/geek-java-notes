[toc]

## 09 | Java 线程（上）：Java 线程的生命周期

1.  Java 语言里的线程本质上就是操作系统的线程，它们是一一对应的。
2.  对于有生命周期的事物，学习它的思路非常简单：搞懂**生命周期中各个节点的状态转换机制**就可以了。

### 通用的线程生命周期

1.  “五态模型”，分别是：
    -   **初始状态、可运行状态、运行状态、休眠状态、终止状态**
    -   ![img](imgs/9bbc6fa7fb4d631484aa953626cf6ae5.png)

#### 初始状态

1.  在语言层面被创建，在操作系统层面，真正线程还没有创建。

#### 可运行状态

1.  操作系统线程已经被成功创建。

#### 运行状态

1.  线程获取到 CPU 资源。

#### 休眠状态

1.  线程调用阻塞 API 或等待某个事件。等事件出现，线程从休眠状态转换到可运行状态。

#### 终止状态

1.  线程执行完或出现异常。线程命令周期线束。

### Java 中线程的生命周期

1.  Java 语言中线程共有六种状态，分别是：
    -   **NEW**，初始化状态
    -   **RUNNABLE**，可运行/运行状态
    -   **BLOCKED**，阻塞状态
    -   **WAITING**，无时限等待
    -   TIMED_WAITING，有时限等待
    -   **TERMINATED**，终止状态
2.  操作系统层面，Java 线程中的 BLOCKED、WAITING、TEMED_WAITING 是一种状态：休眠状态。**这个状态下，线程永远没有 CPU 的使用权**。Java 线程的生命周期简化图如下：
    -   ![img](imgs/3f6c6bf95a6e8627bdf3cb621bbb7f8c.png)

#### RUNNABLE 与 BLOCKED 的状态转换

1.  **触发场景**
    -   线程等待 synchronized 的隐式锁
2.  线程调用阻塞式 API 时，是否会转换到 BLOCKED 状态呢？
    -   操作系统层面，线程会转换到休眠状态。
    -   JVM 层面，**JVM 层面并不关心操作系统调度相关的状态**，在 JVM 看来，等待 CPU 和等待 I/O 没有区别，都是在等待资源，所以都归入 RUNNABLE 状态。

#### RUNNABLE 与 WAITING 的状态转换

1.  **触发场景**
    -   场景一，获得 synchronized 隐式锁的线程，调用无参数的 **Object.wait()** 方法。
    -   场景二，调用无参数的 **Thread.join()** 方法。
    -   场景三，调用 **LockSupport.park()** 方法。

#### RUNNABLE 与 TIMED_WAITING 的状态转换

1.  **触发场景**
    -   场景一，调用**带超时参数**的 **Thread.sleep(long millis)** 方法。
    -   场景二，获得 synchronized 隐式锁的线程，调用**带超时参数**的 **Object.wait(long timeout)** 方法。
    -   场景三，调用**带超时参数**的 **Thread.join(long millis)** 方法。
    -   场景四，调用**带超时参数**的 **LockSupport.parkNanos(Object blocker, long deadline)** 方法。
    -   场景五，调用**带超时参数**的 **LockSupport.parkUntil(long deadline)** 方法。

#### 从 NEW 到 RUNNABLE 状态

1.  **触发场景**

    -   调用线程对象的 **start()** 方法。

        -   ```java
            
            MyThread myThread = new MyThread();
            // 从NEW状态转换到RUNNABLE状态
            myThread.start()；
            ```

        -   

#### 从 RUNNABLE 到 TERMINATED 状态

1.  触发场景
    -   执行完成
    -   抛出异常
    -   强制中断
        -   **Thread 类的 stop() 方法**，已经标记为 @Deprecated，不建议使用。
        -   调用 **interrupt()** 方法。
2.  **stop() 和 interrupt() 方法的主要区别是什么呢**？
    -   stop() 方法
        -   真正杀死线程，不给线程喘息的机会。
    -   interrupt() 方法
        -   **通知线程**，线程有机会执行一些后续操作。也可以无视这个通知。
        -   那怎么收到这个通知呢？
            -   一种是**异常**。
            -   一种是**主动检测**。

### 总结

1.  理解 Java 线程的状态及生命周期对于诊断多线程 Bug 非常有帮助，大部分的死锁、饥饿、活锁都需要跟踪分析线程的状态。

