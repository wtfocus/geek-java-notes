[toc]

## 07 | int 和 Integer 有什么区别？

### 问题

-   **int 和 Integer 有什么区别？谈谈 Integer 的值缓存范围。**

### 回答

1.  Java 原始类型（Primitive Types）： boolean、byte、short、char、int、float、double、long。

#### int

1.  非对象

2.  空间：内存开销较小

3.  时间：原始数据类型有内存里是一段连续的内存，可以利用 CPU 缓存机制。

4.  线程安全问题

    -   如果有线程安全需要，建议使用 AtomicInteger、AtomicLong
    -   float、double 不能保证原子性

5.  局限性

    -   不能与泛型配合使用，因为 Java 泛型必须保证相应类型可以转换为 Object。
    -   无法高效地表达复杂的数据结构，如 vector、tuple。

#### Integer

1.  int 对应的包装类

2.  自动装箱/拆箱（valueOf/intValue）

3.  值缓存（默认为 -128 到 127 之间）

4.  可以使用 JVM 参数设置最大值

    -   ```bash
        -XX:AutoBoxCacheMax=N
        ```

5.  其他 Boolean、Short、Byte、Character 都有值缓存机制

6.  被 “private final” 声明，保证了线程安全

### 扩展

#### 自动装箱、拆箱

1.  语法糖，Java 平台为我们自动进行了一些转换，保证不同写法在运行时等价。

2.  发生在编译阶段。

3.  **基础类型与 Object 的隐式转换**。验证（反编译/规范文档）

    -   代码

        -   ```java
            
            Integer integer = 1;
            int unboxing = integer ++;
            ```

    -   反编译

        -   ```bash
            
            1: invokestatic  #2                  // Method
            java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            8: invokevirtual #3                  // Method
            java/lang/Integer.intValue:()I
            ```

4.  **避免无意中的装箱/拆箱行为**，尤其是性能敏感的场合。

### 小结

1.  非并发 || 大量计算，用基础类型。
2.  快速业务 || 并发，用包装类。
3.  警惕 int 溢出。

### 分享

1.  int 乘法溢出示例

    -   ```java
        public class OverflowTest {
        
            public static void main(String[] args) {
                int buyTimes = 19522579;
                int price = 220;
        
                int consume = buyTimes * price;
                
                // output: 84
                System.out.println(consume);
            }
        }
        ```

    -   两个正数相乘，可能依然是正数。一般检查很容易漏过。

2.  建议使用 guava 的计算函数，带有溢出处理的判断。（[Guava wiki/MathExplained#checked-arithmetic](https://github.com/google/guava/wiki/MathExplained#checked-arithmetic)）

    >   IntMath.checkedAdd
    >   IntMath.checkedSubtract
    >   IntMath.checkedMultiply
    >   IntMath.checkedPow
    >
    >   LongMath.checkedAdd
    >   LongMath.checkedSubtract
    >   LongMath.checkedMultiply
    >   LongMath.checkedPow

