[toc]

## 01 | 谈谈你对 Java 平台的理解？

1.  问题
    -   谈谈你对 Java 平台的理解？
    -   “Java 是解释执行”，这句话正确吗？

### 典型回答

1.  Java 最显著的特性：
    -   书写一次，到处运行（Write once，run anywhere）
    -   垃圾收集（GC，Garbage Collection）
2.  JRE（Java Runtime Environment）
    -   包含了 JVM 和 Java 类库，及一些模块等。
3.  JDK（Java Development Kit）
    -   可以看作 JRE 的一个超集，提供了更多工具。
4.  "Java 是解释执行" 这句话，**不太准确**。
    -   java 源码 => 字节码（bytecode）=> 通过 JVM 转换成机器码。
    -   Oracle JDK 提供的 Hotspot JVM，都提供了 **JIT (Just-In-Time) 编译器**，也就是常说的动态编译器，JIT 能够在运行时将热点代码编译成机器码。这种情况下部分代码就属于**编译执行**，而不是解释执行了。

### 考点分析

1.  尽量**表现出自己的思维深入并系统化，Java 知识理解得也比较全面**。

### 扩展

1.  对于 Java 平台的理解
    -   Java 语言特性
    -   基础类库
    -   JVM 的一些基础概念和机制
        -   类加载机制：Class-Loader
        -   类加载大致过程：加载、验证、链接、初始化
        -   垃圾收集：SerialGC、Parallel GC、CMS、G1

2.  蓝图

    -   ![img](imgs/20bc6a900fc0b829c2f0e723df050732.png)

3.  Oracle Hotspot JVM 内置了两个不同的 JIT compiler

    -   C1，client 模式，适用于对启动速度敏感的应用。如，Java 桌面应用。
    -   C2，server 模式，适用于长时间运行的服务器端应用。

4.  JVM 参数

    -   “-Xint”，解释执行
    -   “-Xcomp”，关闭解释器，不要进行解释执行

5.  AOT（Ahead-of-Time Compilation）

    -   直接将字节码编译成机器代码

    -   ```bash 
        
        # 编译
        jaotc --output libHelloWorld.so HelloWorld.class
        jaotc --output libjava.base.so --module java.base
            
        # 启动
        java -XX:AOTLibrary=./libHelloWorld.so,./libjava.base.so HelloWorld
        ```