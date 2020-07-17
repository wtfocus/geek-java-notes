[toc]

## 03 | 谈谈 final、finally、finalize 有什么不同？

1.  问题
    -   **谈谈 final、finally、finalize 有什么不同**？

### 典型回答

1.  final 可以用来**修饰类、方法、变量**。
    -   修饰 class，代表不可以继承扩展。
    -   修饰 变量，不可以修改。
    -   修饰 方法，不可以重写（overide）。
2.  finally 是 Java **保证重点代码一定要被执行的一种机制**。
    -   try-finally
    -   try-catch-finally
3.  finalize 是 java.lang.Object 的一个方法。
    -   保证对象在被垃圾收集前完成特定资源的回收。

### 考点分析

1.  final
    -   明确表示我们代码的**语义**、逻辑意图。
    -   有效避免 API 使用者更改基础功能，某种程度上，保证平台**安全**的必要手段。
    -   对**性能**也有好处。但，大部分情况下，并没有考虑的必要。
2.  finally
    -   需要关闭连接等资源，更推荐使用 Java 7 中添加的 **try-with-resources** 语句。
3.  finalize
    -   **不推荐使用**
    -   无法保证 finalize 什么时候执行，执行是否符合预期。

### 扩展

#### **final 不等同于 immutable** !!!

-   ```java
    // final
     final List<String> strList = new ArrayList<>();
     strList.add("Hello");
     strList.add("world");  
    
    // immutable
     List<String> unmodifiableStrList = List.of("hello", "world");
     unmodifiableStrList.add("again");    // 运行时抛出异常
    ```

    1.  final 只能约束 strList 这个引用不可以被赋值。但，strList 对象行为不被 final 影响，添加元素等操作是完全正常的。
    2.  [List.of](http://openjdk.java.net/jeps/269) 方法创建的本身就是不可变 List。
    3.  实现 immutable 的类
        -   将 class 声明为 final
        -   将所有成员变量定义为 private 和 final，并且不实现 setter
        -   构造对象时，成员变量使用深度拷贝来初始化
        -   使用 copy-on-write 原则，创建私有的 copy
    4.  **关于 setter/getter，建议你确定有需要时再实现**。

