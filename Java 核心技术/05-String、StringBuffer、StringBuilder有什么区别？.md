[toc]

## 05 | String、StringBuffer、StringBuilder 有什么区别？

1.  问题：**理解 Java 的字符串，String、StringBuffer、StringBuilder 有什么区别？**

### 典型回答

#### String

1.  典型的 **Immutable 类**，被声明成为 final class，所有属性也都是 final 的。
2.  由于这种**不可变性**，类似拼接、裁剪都会产生新的 String 对象。从而影响性能。

#### StringBuffer

1.  为解决上面提到拼接产生太多中间对象的问题而提供的一个类。
2.  **线程安全**，但也因此有额外的性能开销。

#### StringBuilder

1.  Java 1.5 新增，与 StringBuffer 没有本质区别。
2.  去掉了线程安全部分，绝大部分情况的首选。

### 扩展

#### 1. 字符串设计和实现考量

-   反编译如下代码看看：

    -   ```java
        
        public class StringConcat {
             public static String concat(String str) {
               return str + “aa” + “bb”;
             }
        }
        ```

-   反编译输出

    -   ```bash
        
                 0: new           #2                  // class java/lang/StringBuilder
                 3: dup
                 4: invokespecial #3                  // Method java/lang/StringBuilder."<init>":()V
                 7: aload_0
                 8: invokevirtual #4                  // Method java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
                11: ldc           #5                  // String aa
                13: invokevirtual #4                  // Method java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
                16: ldc           #6                  // String bb
                18: invokevirtual #4                  // Method java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
                21: invokevirtual #7                  // Method java/lang/StringBuilder.toString:()Ljava/lang/String;
        ```

-   **非静态拼接逻辑在 JDK 8 中自动被 javac 转换为 StringBuilder 操作。**

#### 2. 字符串缓存

-   String 在 Java 6 以后提供了 intern() 方法。
-   Java 6 常量池存在于 PermGen(永久代)。
-   Java 8 中存于堆中，且 PermGen 被 MetaSpace（元数据区）替代。



