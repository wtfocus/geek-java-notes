[toc]

## Java 相关框架

### Spring 技术发展

1.   Spring

     >   Spring makes Java simple

2.   Spring Boot

     >   Build Anything

3.   Spring Cloud

     >   Coordinate Anything

4.   Spring Cloud Data Flow

     >   Connect Everything

### Spring 框架设计

1.   框架是基于一组类库或工具，在特定领域里根据一定的规则组合成的、开放性的骨架。

2.   特性

     -   支撑性+扩展性
     -   聚合性+约束性

3.   Spring Framework

     -   常用

         >   1.   Core: Bean/Context/AOP
         >
         >   2.   Testing: Mock/TestContext
         >
         >   3.   DataAccess: Tx/JDBC/ORM
         >
         >   4.   Spring MVN/WebFlux: web

     -   不常用

         >   5.   Integration: remoting/JMS/WS
         >
         >   6.   Languages: Kotlin/Groovy

4.   Spring 框架设计

     -   ![image-20211202212111097](imgs/image-20211202212111097.png)

5.   Spring 研发协作模式

     -   ![image-20211202212749754](imgs/image-20211202212749754.png)

### Spring AOP 详解

1.   AOP - 面向切面编程

     -   Spring 早期版本的核心功能

         >   管理对象生命周期与对象装配

     -   增加一个中间层代理来实现所有对象的托管。 

2.   IoC - 控制反转

     -   DI（Dependency Injection），依赖注入。对象装配思路的改进。

3.   循环依赖

     -   Spring 可以解压循环依赖的问题
     -   构造器中的循环依赖（死锁），Spring / Java 是没法解决的。

### Spring Bean 核心原理

### Spring XML 配置原理

### Spring Messaging

### Spring -> Spring Boot

### Spring Boot

### Spring Boot Starter

### JDBC / 数据库连接池 / ORM-Hibernate / MyBatis

### Spring / Spring Boot 集成 ORM / JPA

