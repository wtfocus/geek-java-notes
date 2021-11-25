[toc]

## 08 | 对比 Vector、ArrayList、LinkedList 有何区别？

### 问题

-   对比 Vector、ArrayList、LinkedList 有何区别？

### 回答

1.  三者都是实现集合框架中的 List（有序集合）。

#### Vector

1.  **线程安全**的动态数组。
2.  内部使用**对象数组**来保存数据
3.  动态扩容，扩容时增加 **100%**

#### ArrayList

1.  **非线程安全**的动态数组。
2.  性能较好
3.  扩容时增加 **50%**

#### LinkedList

1.  **双向链表**
2.  **非线程安全**

### 分析

#### 适合的场景

1.  Vector 和 ArrayList
    -   内部元素以数组形式**顺序存储**的，非常适合随机访问。
    -   除尾部插入和删除，往往**性能相对较差**。
2.  LinkedList
    -   插入、删除要高效很多
    -   随机访问性能相对较慢

#### Java 集合框架，需要掌握的方面

1.  **设计结构**，至少要有一个整体印象
2.  了解或掌握对应的**数据结构、算法**
3.  扩展到 **性能、并发** 等领域
4.  演进与发展

#### 典型的排序算法

1.  内部排序
    -   掌握基础算法，如归并、交换（冒泡、快排）、选择、插入等。
2.  外部排序
    -   掌握利用内存和外部存储处理越大数据集，至少理解过程和思路。
3.  是否稳定？稳定意味什么？
4.  对不同数据集，最好或最差情况。

### 扩展

#### 集合框架

1.  Java 集合框架图

    -   ![img](imgs/675536edf1563b11ab7ead0def1215c7.png)

2.  三大类集合

    -   List，有序集合

    -   Set，不允许重复元素

        >   TreeSet，支持自然顺序访问
        >
        >   HashSet，利用哈希算法
        >
        >   LinkedHashSet，内部构建了一个记录插入顺序的双向链表，

    -   Queue/Deque，标准队列结构的实现

3.  今天介绍的这些集合类，**都不是线程安全的**。但，可以通过 java.util.concurrent 实现线程安全。

    -   ```java
        // synchronized 方法
        static List synchronizedList(List list)
            
        // 使用示例
        List list = Collections.synchronizedList(new ArrayList());
        ```

### 分享

1.  使用时必须注意**是否需要线程安全**的场合

    -   有并发读写的代码里，必须使用 Vertor，或其他线程安全的集合结构。
    -   没有并发写的代码里，则使用 ArrayList、LinkedList，性能更优。

2.  测试代码：

    -   ```java
        
        public class ListTest {
            static List<Integer> list = new ArrayList<Integer>(1000);
            static CountDownLatch latch = null;
        
            public static void main(String[] args) throws InterruptedException {
                System.out.println("begin");
        
                int size = 2;
                latch = new CountDownLatch(size);
        
                for (int i = 0; i < size; i++) {
                    new LTest().start();
                }
        
                latch.await();
                System.out.println("finish, list.size: " + list.size());
                System.out.println("list: " + list);
            }
        }
        
        
        class LTest extends Thread {
            static AtomicInteger at = new AtomicInteger();
        
            public void run() {
                List<Integer> list = ListTest.list;
        
                try {
                    while(at.get() < 1000) {
                        list.add(at.get());
                        int value = at.incrementAndGet();
        
                        if (value % 100 == 0) {
                            System.out.println(value);
                        }
                        if (value - 1 >= 0) {
                            list.get(value - 1);
                        }
                        if (value - 2 >= 0) {
                            list.get(value - 2);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
        
                System.out.println(list.size());
                ListTest.latch.countDown();
        
            }
        }
        ```

3.  运行后，输出结果如下：

    -   ```bash
        begin
        java.lang.IndexOutOfBoundsException: Index: 46, Size: 47
        	at java.util.ArrayList.rangeCheck(ArrayList.java:657)
        	at java.util.ArrayList.get(ArrayList.java:433)
        	at com.xxx.LTest.run(ListTest.java:45)
        47
        java.lang.IndexOutOfBoundsException: Index: 47, Size: 47
        	at java.util.ArrayList.rangeCheck(ArrayList.java:657)
        	at java.util.ArrayList.get(ArrayList.java:433)
        47
        	at com.xxx.LTest.run(ListTest.java:45)
        finish, list.size: 47
        list: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 31, 32, 33, 35, 35, 37, 37, 39, 39, 41, 41, 43, 43, 45, 47]
        
        ```

4.  小结

    -   并发读写时，有数据越界、位置错乱的问题。
    -   注意线程安全事项，否则可能数据错乱引起 BUG。