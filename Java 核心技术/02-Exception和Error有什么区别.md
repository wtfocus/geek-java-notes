[toc]

## 02 | Exception 和 Error 有什么区别？

1.  问题：
    -   请对比 Exception 和 Error。
    -   另外，运行时异常与一般异常有什么区别？

### 典型回答

1.  Exception 和 Error **都是继承了 Throwable 类**。只有 Throwable 类型的实例才可以被抛出（throw）或捕获（catch）
2.  Exception 是程序正常运行中，可以预料的意外情况。可能并且应该被捕获，进行相应处理。
    -   **可检查（checked）异常**，编译期检查。
    -   **不检查（unchecked）异常（运行时异常）**，如 NullPointerException、ArrayIndexOutOfBoundsException。
3.  Error 是指在正常情况下，不大可能出现的情况。可能会导致程序崩溃。如 OutOfMemoryError 之类。

### 考点分析

#### 如何处理好异常

1.  **理解 Throwable、Exception、Error 的设计和分类**

    -   ![img](imgs/accba531a365e6ae39614ebfa3273900.png)

2.  **理解 Java 语言中操作 Throwable 的元素和实践**

    -   try-catch-finally

    -   throw

    -   throws

    -   try-with-resources 和 multiple catch

        -   ```java

            try (BufferedReader br = new BufferedReader(…);
                 BufferedWriter writer = new BufferedWriter(…)) {// Try-with-resources
            // do something
            catch ( IOException | XEception e) {// Multiple catch
               // Handle it
            } 
            ```

        

### 知识扩展

#### 代码分析一

1.  代码

    -   ```java
        
        try {
          // 业务代码
          // …
          Thread.sleep(1000L);
        } catch (Exception e) {
          // Ignore it
        }
        ```

2.  分析

    -   一，**尽量不要捕获类似 Exception 这样的通用异常，而是应该捕获特定的异常。**
        -   代码可读性，具体的异常类，更好理解。
        -   不希望捕获的异常，让它扩散出来。
    -   二，**不要生吞（swallow）异常**
        -   生吞异常，因为看不到错误信息，异常问题追查带来极大的困难，所以一定要注意。
        -   处理异常时，如不知道如何处理，则用以下方式处理：
            -   通过log系统输出到日志文件。
            -   可以继续往上抛异常，由更上层处理。

### 代码分析二

1.  代码

    -   ```java
        
        try {
           // 业务代码
           // …
        } catch (IOException e) {
            e.printStackTrace();
        }
        ```

2.  分析

    -   printStackTrace 会输出到错误输出中，可以导致找不到输出到哪里了。**最好使用产品日志**。

#### 体会 “Throw early, catch late” 原则

1.  代码（**Throw early**）

    -   ```java
        // Throw early 前
        
        public void readPreferences(String fileName){
           //...perform operations... 
          InputStream in = new FileInputStream(fileName);
           //...read the preferences file...
        }
        
        // 使用 Throw early，更加清晰反映问题
        public void readPreferences(String filename) {
          Objects. requireNonNull(filename);
          //...perform other operations... 
          InputStream in = new FileInputStream(filename);
           //...read the preferences file...
        }
        ```

2.  catch late

    -   如果不知道如何处理，可以保留原有异常的 cause 信息，直接再抛出。由更高层面，决定如何处理。

### 从性能角度审视 Java 异常机制

1.  仅捕获必要代码段，尽量不要一个大的 try 包住整个代码段。
2.  勿用异常控制代码流程，它比（if/else、switch）更低效。
3.  如无必要，勿用异常。Java 每实例化一个 Exception，都会对当前栈进行快照。