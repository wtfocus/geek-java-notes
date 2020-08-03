[toc]

## 06 | JVM 是如何处理异常的？

1.  异常处理
    -   抛出异常
        -   显式
            -   主体：应用程序，手动
        -   隐式
            -   主体：JVM，自动
    -   捕获异常
        -   代码块
            -   try
            -   catch
            -   finally
                -   

### 异常的基本概念

1.  所有异常都是 Throwable 类或其子类。
2.  Throwable 两大直接子类
    -   Error -- 非检查异常（unchecked exception）
    -   Exception
        -   RuntimeException -- 非检查异常（unchecked exception）
    -   ![img](imgs/47c8429fc30aec201286b47f3c1a5993.png)

3.  构造异常实例

    -   构造异常**消耗很大**

    -   需要生成该异常的栈轨迹（stack trace）
    -   逐一访问当前线程的 Java 栈帧，记录各调试信息。包括方法名、文件名、及第几行触发该异常。

### Java 虚拟机是如何捕获异常的？

### Java 7 的 Suppressed 异常以及语法糖

1.  程序可以在 try 关键字后声明实例化了 AutoCloseable 接口的类，编译器将自动添加对应的 close() 操作。

2.  与手工代码相比，try-with-resources 还会使用 Suppressed 异常的功能，来避免异常“被消失”。

3.  示例

    -   ```java
        // 使用前
        
          FileInputStream in0 = null;
          FileInputStream in1 = null;
          FileInputStream in2 = null;
          ...
          try {
            in0 = new FileInputStream(new File("in0.txt"));
            ...
            try {
              in1 = new FileInputStream(new File("in1.txt"));
              ...
              try {
                in2 = new FileInputStream(new File("in2.txt"));
                ...
              } finally {
                if (in2 != null) in2.close();
              }
            } finally {
              if (in1 != null) in1.close();
            }
          } finally {
            if (in0 != null) in0.close();
          }
        
        
        
        // 使用后
        
        public class Foo implements AutoCloseable {
          private final String name;
          public Foo(String name) { this.name = name; }
        
          @Override
          public void close() {
            throw new RuntimeException(name);
          }
        
          public static void main(String[] args) {
            try (Foo foo0 = new Foo("Foo0"); // try-with-resources
                 Foo foo1 = new Foo("Foo1");
                 Foo foo2 = new Foo("Foo2")) {
              throw new RuntimeException("Initial");
            }
          }
        }
        
        // 运行结果：
        Exception in thread "main" java.lang.RuntimeException: Initial
                at Foo.main(Foo.java:18)
                Suppressed: java.lang.RuntimeException: Foo2
                        at Foo.close(Foo.java:13)
                        at Foo.main(Foo.java:19)
                Suppressed: java.lang.RuntimeException: Foo1
                        at Foo.close(Foo.java:13)
                        at Foo.main(Foo.java:19)
                Suppressed: java.lang.RuntimeException: Foo0
                        at Foo.close(Foo.java:13)
                        at Foo.main(Foo.java:19)
        
        ```

4.  同一 catch 捕获多种异常

    -   ```java
        
        // 在同一catch代码块中捕获多种异常
        try {
          ...
        } catch (SomeException | OtherException e) {
          ...
        }
        ```

    -   

### 小结

