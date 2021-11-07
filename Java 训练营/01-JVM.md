[toc]

## JVM

### Java 概览

- 优势
	1. 生态
	2. JVM / GC
	3. 跨平台
- 字节码、类加载器、虚拟机内存
	- ![image-20211107111746232](imgs/image-20211107111746232.png)

### Java 字节码

- 分类
	1.　栈操作指令，包括与局部变量交互的指令。（虚拟机本身需要）
	2.　程序流程控制指令（语言所需要）
	3.　对象操作指令，包括方法调用指令（语言所需要）
	4.　算术运算以及类型转换指令（语言所需要）
- 生成/  查看字节码
	- javac  xxxx.java -> xxxx.class
	- javap xxxx.class -> {bytecode}
- 算数操作
	- ![image-20211107121358008](imgs/image-20211107121358008.png)
- 类型转换
	- ![image-20211107121440903](imgs/image-20211107121440903.png)
- 方法调用指令
	- Invokestatic: 顾名思义，这个指令用于调用某个类的静态方法，这是方法调用指令中最快 的一个。
		Invokespecial : 用来调用构造函数，但也可以用于调用同一个类中的 private 方法, 以及可 见的超类方法。
		invokevirtual : 如果是具体类型的目标对象，invokevirtual 用于调用公共、受保护和 package 级的私有方法。
		invokeinterface : 当通过接口引用来调用方法时，将会编译为 invokeinterface 指令。
		invokedynamic : JDK7 新增加的指令，是实现“动态类型语言”(Dynamically Typed Language)支持而进行的升级改进，同时也是 JDK8 以后支持 lambda 表达式的实现基础。

### JVM 类加载器

- 类的生命周期
	- ![image-20211107144423627](imgs/image-20211107144423627.png)
- 类的加载时机
	1. 当虚拟机启动时，初始化用户指定的主类，就是启动执行的 **main 方法**所在的类;
	2. 当遇到用以新建目标类实例的 new 指令时，初始化 **new 指令**的目标类，就是 new 一 个类的时候要初始化;
	3. 当遇到调用**静态方法**的指令时，初始化该静态方法所在的类;
	4. 当遇到访问**静态字段**的指令时，初始化该静态字段所在的类;
	5. 子类的初始化会触发**父类**的初始化;
	6. 如果一个接口定义了 default 方法，那么直接实现或者间接实现该接口的类的初始化， 会触发该接口的初始化;
	7. 使用反射 API 对某个类进行反射调用时，初始化这个类，其实跟前面一样，反射调用要 么是已经有实例了，要么是静态方法，都需要初始化;
	8. 当初次调用 MethodHandle 实例时，初始化该 MethodHandle 指向的方法所在的类。
- 不会初始化

	1. 通过子类引用父类的静态字段，只会触发父类的初始化，而不会触发子类的初始化。
	2. 定义对象数组，不会触发该类的初始化。
	3. 常量在编译期间会存入调用类的常量池中，本质上并没有直接引用定义常量的类，不 会触发定义常量所在的类。
	4. 通过类名获取 Class 对象，不会触发类的初始化，Hello.class 不会让 Hello 类初始 化。
	5. 通过 Class.forName 加载指定类时，如果指定参数 initialize 为 false 时，也不会触 发类初始化，其实这个参数是告诉虚拟机，是否要对类进行初始化。 (Class.forName”jvm.Hello”)默认会加载 Hello 类。
	6. 通过 ClassLoader 默认的 loadClass 方法，也不会触发初始化动作(加载了，但是 不初始化)。
- 三类加载器
	1. 启动类加载器(BootstrapClassLoader) 
	2. 扩展类加载器(ExtClassLoader)
	3. 应用类加载器(AppClassLoader)　
- 加载器特点
	- 双亲委托
	- 负责依赖
	- 缓存加载
- 添加引用类的几种方式
	1、放到 JDK 的 lib/ext 下，或者 -Djava.ext.dirs
	2、 java-cp/classpath 或者 class 文件放到当前路径
	3、自定义 ClassLoader 加载
	4、拿到当前执行类的 ClassLoader，反射调用 addUrl 方法添加 Jar 或路径(JDK9 无效)

### JVM 内存模型

- JVM 内存结构
	- ![image-20211107153817750](imgs/image-20211107153817750.png)
	- 每个线程**只能访问**自己的线程栈。
	- 每个线程都**不能访问**（看不见）其他线程的局部变量。
	- 所有**原生类型**的局部变量都存储在线程栈中，因此，对其他线程是不可见的。
	- 线程可以将一个原生变量值的**副本**传给另一个线程，但不能共享原生局部变量本身。
	- 堆内存中包含了 Java 代码中创建的**所有对象**，不管是哪个线程创建的。其他也涵盖了包装类型（如，Byte, Integer, Long 等）。
	- 不管是创建一个对象并将其赋值给局部变量，还是赋值给另一个对象的成员变量，创建的对象都会被保存在**堆内存**中。
	- ![image-20211107161032659](imgs/image-20211107161032659.png)
	- 如果是**原生数据类型的局部变量**，那么它的内容就全部保留在**线程栈**上。
	- 如果是对象引用，则**栈**中的局部变量槽位中保存着对象的**引用地址**，而实际的**对象内容**保存在**堆**中。
	- 对象的**成员变量与对象本身**一起存储在堆上，不管成员变量的类型是原生数值，还是对象引用。
	- 类的**静态变量则和类定义**一样保存在堆中。
	- ![image-20211107161605770](imgs/image-20211107161605770.png)
	- 总结
		- 方法中使用的原生数据类型和对象引用地址在**栈**上存储；对象、对象成员与类定义、静态变量在**堆**上。
		- 堆内存又称为“共享堆”，堆中的所有对象，可以被所有线程访问，只要他们能拿到对象的引用地址。
		- 如果一个线程可以访问某个对象时，也就可以访问该对象的成员变量。
		- 如果两个线程同时调用某个对象的同一个方法，则它们都可以访问到这个对象的成员变量，但每个线程的局部变量副本是**独立的**。
- JVM 内存整体结构
	- ![image-20211107162952618](imgs/image-20211107162952618.png)
	- 每启动一个线程，JVM 就会在栈空间分配对应的线程栈。
	- 线程栈也叫 Java 方法栈。如果使用 JNI 方法，则会分配一个单独的本地方法栈（Native Stack）。
	- 线程执行过程中，一般会有多个方法组成调用栈（Stack Trace），每执行到一个方法，就会创建对应的栈帧（Frame）。
- JVM 栈内存结构
	- ![image-20211107163332273](imgs/image-20211107163332273.png)
	- 栈帧是一个逻辑上的概念，具体的大小在一个方法编写完成后基本就能确定。
- JVM 堆内存结构
	- ![image-20211107163612913](imgs/image-20211107163612913.png)
	- 堆（Heap）内存是所有线程共用的内存空间
		- JVM 将堆内存分为年轻代（Young-gen）和老年代（Old-gen）。
		- 年轻代还划分为 3 个内存池，新生代（Eden-space）和存活区（S0/S1）
	- 非堆（Non-Heap）本质上还是 Heap，只是一般不归GC 管理，里面划分为 3 个内存池。
		- Metaspace（持久代/永久代）
		- CSS（Compressed Class Space），存放 class 信息的，和 Metaspace 有交叉。
		- Code Cache，存放 JIT 编译器编译后的本地机器代码。
- JMM
	- [JSR-133. Java Memory Model and Thread Specification]，《Java 语言规 范》的 [$17.4. Memory Model章 节]
	- JMM 规范明确定义了不同线程之间，通过哪些方式，在什么时候可以看见其他线程保存到共享变量中的值；以及在必要时，如何对共享变量的访问进行同步。
	- 好处，屏蔽各种平台间的内存访问差异，实现了 Java 并发程序真正的跨平台。

### JVM 启动参数

-  格式

	- ```tex
		java [options] class [args]
		java [options] -jar filename [args]
		```

- 分类

	- 标准参数 -
	- 系统属性 -D
	- 非标准参数 -X
		- java -X
	- 非稳定参数 -XX
		- -XX: +-Flags，对布尔值进行开关。
		- -XX: key=value，指定某个选项的值。

- 分类（特点、作用）

	- 系统属性参数

		- 

			```bash
			# 命令行设置
			-Dfile.coding=UTF-8
			-Duser.timezone=GMT+08
			-Dmaven.test.skip=true
			-Dio.netty.eventLoopThreads=8
			
			# 程序内设置
			System.setProperty("a", "100");
			String a = System.getProperty("a");
			```

	- 运行模式参数

		- -server，启动速度慢，运行时性能和内存管理效率很高。
		- -client，启动速度快，但运行时性能和内存管理效率不高。
		- -Xint，解释模式
		- -Xcomp，编译模式
		- -Xmixed，混合模式（JVM 默认模式）

	- 堆内存设置参数

		- -Xmx，最大堆内存， 

			> 部署 Java 应用要资源隔离，防止多个 Java 应用抢资源。
			>
			> xmx <= 物理内存的70%（60～80）

		- -Xms，初始化大小

			> 建议配置大小和-Xmx 大小一致，降低 Full GC 抖动。

		- -Xmn，等价于 -XX:NewSize。

			> 官方建议为 -Xmx 的 1/2~1/4。

		- -XX: MaxPermSize=size，JDK1.7 之前使用，Java 8 无效。

		- -XX: MaxMetaspaceSize=size，Java 8 默认不限制 Meta 空间，不允许设置该参数。

		- -XX: MaxDirectMemorySize=size，最大堆外内存，等同 -Dsun.nio.MaxDirectMemorySize 。

		- -Xss，每个线程栈的字节数。

	- GC 设置参数（多且杂）

		- JDK 默认的 GC 是什么？

	- 分析诊断参数

	- JavaAgent 参数

1.　

### JVM 命令行工具

1. jps -lmv

2. jinfo {pid}

3. jstat -gc 91739 1000 1000

4. jstat -gcutil 91739 1000 1000 （百分比）

5. jmap -histo 91739

6. jstack -l 91739　线程信息

7. jcmd

	> (env) wangtaodeMac-mini:code wangtao$ jcmd 91739 help
	> 91739:
	> The following commands are available:
	> JFR.stop
	> JFR.start
	> JFR.dump
	> JFR.check
	> VM.native_memory
	> VM.check_commercial_features
	> VM.unlock_commercial_features
	> ManagementAgent.stop
	> ManagementAgent.start_local
	> ManagementAgent.start
	> VM.classloader_stats
	> GC.rotate_log
	> Thread.print
	> GC.class_stats
	> GC.class_histogram
	> GC.heap_dump
	> GC.finalizer_info
	> GC.heap_info
	> GC.run_finalization
	> GC.run
	> VM.uptime
	> VM.dynlibs
	> VM.flags
	> VM.system_properties
	> VM.command_line
	> VM.version
	> help

### 图形化工具

1.　jconsole
2.　jvisualvm
3.　VisualGC
4.　jmc

### GC 背景＆原理

1. Y-gen
	- Eden/S0/S1 = 8/1/1