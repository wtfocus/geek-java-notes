[toc]

## 19 | Java 并发包提供了哪些并发工具类？

### 问题

-   **Java 并发包提供了哪些并发工具类？**

### 回答

1.  java.util.concurrent 及其子包的工具类：
    -   提供了比 synchronized 更加高级的各种**同步结构**。
        -   包括 CountDownLatch、CyclicBarrier、Semaphore 等。
    -   **各种线程安全的容器**
        -   ConcurrentHashMap、ConcurrentSkipListMap、CopyOnWriteArrayList 等。
    -   **各种并发队列实现**
        -   如各种 BlockingQueue 实现：ArrayBlockingQueue、 SynchronousQueue、PriorityBlockingQueue
    -   强大的 **Executor** 框架
        -   可以创建各种不同类型的线程池。

### 分析

1.  目的
    -   利用多线程提高程序的扩展能力，以达到业务对吞吐量的要求。
    -   协调线程间调度、交互，以完成业务逻辑。
    -   线程间传递数据和状态，这同样是实现业务逻辑的需要。

### 扩展

#### 并发包提供的同步结构

1.  重点关注
    -   **CountDownLatch**，允许一个或多个线程等待某些操作完成。
    -   **CyclicBarrier**，一种辅助性的同步结构，允许多个线程等待到达某个屏障。
    -   **Semaphore**，Java 版本的信号量实现。

##### **Semaphore**

1.  典型用法

    -   ```java
        public class UsualSemaphoreSample {
            public static void main(String[] args) {
                System.out.println("Action... Go!");
                Semaphore semaphore = new Semaphore(5);
                for (int i = 0; i < 10; i++) {
                    Thread t = new Thread(new SemaphoreWorker(semaphore));
                    t.start();
                }
            }
        }
        
        
        class SemaphoreWorker implements Runnable {
            private String name;
            private Semaphore semaphore;
            public SemaphoreWorker(Semaphore semaphore) {
                this.semaphore = semaphore;
            }
        
            @Override
            public void run() {
                try {
                    log("is waiting for a permit.");
                    semaphore.acquire();
                    log("acquire a permit.");
                    log("executed.");
                    // 模拟线程工作
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
        
        
                } finally {
                    log("release a permit.");
                    semaphore.release();
                }
            }
        
            private void log(String msg) {
                if (this.name == null) {
                    this.name = Thread.currentThread().getName();
                }
                System.out.println(this.name + " " + msg);
            }
        }
        
        ```

2.  总体来说，Semaphore 就是个**计数器**，其基本逻辑基于 **acquire/release**。

#### CountDownLatch 和 CyclicBarrier

##### **区别**

1.  CountDownLatch 是**不可重置**，所以**无法重用**。而 CyclicBarrier 没有这种限制，**可以重用**。
2.  CountDownLatch 的基本操作是 countDown/await。CountDownLatch 操作的是**事件**。
3.  CyclicBarrier 的基本操作是 await，当所有小伙伴都调用了 await，才会继续进行任务，并自动重置。CyclicBarrier 侧重点是线程，而不是事件。它的典型场景是**用来等待并发线程结束**。

##### **使用 CountDownLatch 实现排队**

1.  代码

    -   ```java
        public class LatchSample {
            public static void main(String[] args) throws InterruptedException {
                CountDownLatch latch = new CountDownLatch(6);
                for (int i = 0; i < 5; i++) {
                    Thread t = new Thread(new FirstBatchWorker(latch));
                    t.start();
                }
                for (int i = 0; i < 5; i++) {
                    Thread t = new Thread(new SecondBatchWorker(latch));
                    t.start();
                }
        
        //        while (latch.getCount() != 1) {
                    Thread.sleep(20 * 1000);
        //        }
                System.out.println("Wait for first batch finish.");
                latch.countDown();
            }
        }
        
        
        class FirstBatchWorker implements Runnable {
            private CountDownLatch latch;
            private String name;
            public FirstBatchWorker(CountDownLatch latch) {
                this.latch = latch;
            }
        
            @Override
            public void run() {
                if (this.name == null) {
                    this.name = Thread.currentThread().getName();
                }
                latch.countDown();
                System.out.println(this.name + ": First batch executed.");
            }
        }
        
        
        class SecondBatchWorker implements Runnable {
            private CountDownLatch latch;
            private String name;
            public SecondBatchWorker(CountDownLatch latch) {
                this.latch = latch;
            }
        
            @Override
            public void run() {
                try {
                    latch.await();
                    if (this.name == null) {
                        this.name = Thread.currentThread().getName();
                    }
                    System.out.println(this.name + ": Second batch executor.");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        ```

2.  输出

    -   ```bash
        Thread-0: First batch executed.
        Thread-4: First batch executed.
        Thread-3: First batch executed.
        Thread-2: First batch executed.
        Thread-1: First batch executed.
        Wait for first batch finish.
        Thread-5: Second batch executor.
        Thread-8: Second batch executor.
        Thread-7: Second batch executor.
        Thread-6: Second batch executor.
        Thread-9: Second batch executor.
        ```

3.  CountDownLatch 用于线程间等待操作结束是非常简单普遍的用法。通过 countDown/await 组合进行通信是**很高效的**。

##### **CyclicBarrier 实现**

1.  代码

    -   ```java
        public class CyclicBarrierSample {
            public static void main(String[] args) {
                CyclicBarrier barrier = new CyclicBarrier(5, new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("Action... Go again!");
                    }
                });
                for (int i = 0; i < 5; i++) {
                    Thread t = new Thread(new CyclicWorker(barrier));
                    t.start();
                }
            }
        }
        
        
        class CyclicWorker implements Runnable {
            private CyclicBarrier barrier;
            private String name;
            public CyclicWorker (CyclicBarrier cyclicBarrier) {
                this.barrier = cyclicBarrier;
            }
        
            @Override
            public void run() {
                if (this.name == null) {
                    this.name = Thread.currentThread().getName();
                }
                try {
                    for (int i = 0; i < 3; i++) {
                        System.out.println(this.name + " -- executor.");
                        Thread.sleep(3000);
                        barrier.await();
                    }
                } catch (BrokenBarrierException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        ```

2.  输出

    -   ```bash
        Thread-0 -- executor.
        Thread-1 -- executor.
        Thread-2 -- executor.
        Thread-3 -- executor.
        Thread-4 -- executor.
        
        Action... Go again!
        Thread-0 -- executor.
        Thread-2 -- executor.
        Thread-3 -- executor.
        Thread-4 -- executor.
        Thread-1 -- executor.
        Action... Go again!
        Thread-1 -- executor.
        Thread-0 -- executor.
        Thread-3 -- executor.
        Thread-2 -- executor.
        Thread-4 -- executor.
        Action... Go again!
        ```

3.  CyclicBarrier 会自动重置，所以，这个逻辑可以非常自然的**支持更多排队人数**。

#### 线程安全 Map、List、Set

1.  类图
    -   ![img](imgs/35390aa8a6e6f9c92fda086a1b95b457.png)
2.  选择
    -   如果侧重于 Map **存取速度**，而不在乎顺序，推荐 ConcurrentHashMap。反之则 ConCurrentSkipListMap。
    -   如果需要对**大量数据**进行非常**频繁地修改**，ConcurrentSkipListMap 也可能表现出优势。
3.  SkipList 内部结构
    -   ![img](imgs/63b94b5b1d002bb191c75d2c48af767b.png)

#### CopyOnWrite

1.  原理：**任何修改操作，如 add/set/remove，都会拷贝原数组，修改后替换原来的数组**。通过这种防御性的方式，实现另类线程安全。

    -   ```java
        
        public boolean add(E e) {
          synchronized (lock) {
              Object[] elements = getArray();
              int len = elements.length;
                   // 拷贝
              Object[] newElements = Arrays.copyOf(elements, len + 1);
              newElements[len] = e;
                   // 替换
              setArray(newElements);
              return true;
                    }
        }
        final void setArray(Object[] a) {
          array = a;
        }
        ```

2.  场景

    -   适合读多写少的操作。不然修改的开销还是非常明显的。

### 分享

1.  CountDownLatch 和 CyclicBarrier 都能够实现线程之间的等待，只不过它们侧重点不同：
    -   CountDownLatch 一般用于某个线程 A 等待若干个其他线程执行完任务之后，它才执行；
    -   而 CyclicBarrier 一般用于一组线程互相等待至某个状态，然后这一组线程再同时执行；
    -   另外，CountDownLatch是不能够重用的，而CyclicBarrier是可以重用的。
2.  Semaphore 其实和锁有点类似，它一般用于控制对某组资源的访问权限。

