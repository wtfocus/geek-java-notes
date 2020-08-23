[toc]

## 15 | synchronized 和 ReentrantLock 有什么区别呢？

### 问题

-   **synchronized 和 ReentrantLock 有什么区别？有人说 synchronized 最慢，这话靠谱吗？**

### 回答

1.  **synchronized** 是 Java 内建的同步机制，它提供了互斥的语义和可见性，当一个线程已经获取当前锁时，其他试图获取的线程只能等待或阻塞在那里。
2.  **ReentrantLock**（再入锁），提供更多细节控制，代码书写也更加灵活。

### 分析

1.  基础知识
    -   什么是线程安全。
    -   synchronized、ReentrantLock 的使用与案例。
2.  更近一步
    -   synchronized、ReentrantLock 底层实现。
    -   java.util.concurrent.lock 各种不同实现和案例分析。

### 扩展

#### 什么是线程安全

1.  定义

    -   线程安全是一个多线程环境下的正确性的概念，也就是保证多线程环境下**共享的**、**可修改**的状态的**正确性**。

2.  保证线程安全的两个方法

    -   **封装**：把对象内部状态隐藏、保护起来。
    -   **不可变**：final 和 immutable。

3.  线程安全需要保证几个基本特性

    -   **原子性**，操作中途不被其他线程干扰。
    -   **可见性**，一个线程修改的修改，能够立即被其他线程知晓。volatile 负责保证可见性。
    -   **有序性**，保证线程内串行语义，避免指令重排等。

4.  示例：分析原子性需求体现在哪里。

    -   代码

    -   ```java
        public class ThreadSafeSample {
            public int sharedState;
            public void nonSafeAction() {
                while (sharedState < 1000000) {
                    int former = sharedState++;
                    int latter = sharedState;
                    if (former != latter - 1) {
                        System.out.println(
                                "Observer data race, former is " + former + ", latter is " + latter
                        );
                    }
                }
            }
        
            public static void main(String[] args) throws InterruptedException {
                ThreadSafeSample sample = new ThreadSafeSample();
                Thread threadA = new Thread() {
                    @Override
                    public void run() {
                        sample.nonSafeAction();
                    }
                };
                Thread threadB = new Thread() {
                    @Override
                    public void run() {
                        sample.nonSafeAction();
                    }
                };
        
                threadA.start();
                threadB.start();
                threadA.join();
                threadB.join();
            }
        }
        ```

    -   输出

    -   ```bash
        Observer data race, former is 11423, latter is 11433
        Observer data race, former is 63472, latter is 63486
        Observer data race, former is 67814, latter is 67821
        ```

5.  使用 synchronized 优化

    -   ```java
                while (sharedState < 1000000) {
                    int former;
                    int latter;
                    synchronized (this) {
                        former = sharedState++;
                        latter = sharedState;
                    }
        
                    if (former != latter - 1) {
                        System.out.println(
                                "Observer data race, former is " + former + ", latter is " + latter
                        );
                    }
                }
        ```

#### ReentrantLock

1.  什么是再入？

    -   一个线程可以再次获取它已经获取到的锁。

2.  设置公平性（fairness）

    -   ```java
        
        ReentrantLock fairLock = new ReentrantLock(true);
        ```

    -   synchronized，无法进行公平的选择。

    -   建议：**只有**当程序确实有公平性的需要的时候，才有必要指定它。

3.  每一个 lock() 建议都立即对应一个 try-catch-finally。

    -   ```java
        
        ReentrantLock fairLock = new ReentrantLock(true);// 这里是演示创建公平锁，一般情况不需要。
        fairLock.lock();
        try {
          // do something
        } finally {
           fairLock.unlock();
        }
        ```

#### 条件变量（java.util.concurrent.Condition）

1.  Condition 是将 wait、notify、notifyAll 等操作转化为相应的对象，将复杂而晦涩的同步操作转变为直观可控的对象行为。

2.  场景：ArrayBlockingQueue

    -   ```java
        public ArrayBlockingQueue(int capacity, boolean fair) {
          if (capacity <= 0)
              throw new IllegalArgumentException();
          this.items = new Object[capacity];
          lock = new ReentrantLock(fair);
          notEmpty = lock.newCondition();
          notFull =  lock.newCondition();
        }
        
        
        public E take() throws InterruptedException {
          final ReentrantLock lock = this.lock;
          lock.lockInterruptibly();
          try {
              while (count == 0)
                  notEmpty.await();
              return dequeue();
          } finally {
              lock.unlock();
          }
        }
        
        
        private void enqueue(E e) {
          // assert lock.isHeldByCurrentThread();
          // assert lock.getHoldCount() == 1;
          // assert items[putIndex] == null;
          final Object[] items = this.items;
          items[putIndex] = e;
          if (++putIndex == items.length) putIndex = 0;
          count++;
          notEmpty.signal(); // 通知等待的线程，非空条件已经满足
        }
        
        ```

#### 小结

1.  **大多数情况下，无需纠结于性能，还是考虑代码书写结构的便利性、可维护性等**。