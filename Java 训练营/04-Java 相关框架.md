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

4.   Spring AOP 

     -   ![image-20211204150820341](imgs/image-20211204150820341.png)

5.   AOP-面向切面编程，示例

6.   AOP 实现

     -   ![image-20211204150935377](imgs/image-20211204150935377.png)

### Spring Bean 核心原理

1.   Bean 的加载过程
     -   ![image-20211204151824137](imgs/image-20211204151824137.png)
     -   ![image-20211204151843885](imgs/image-20211204151843885.png)
     -   ![image-20211204151901693](imgs/image-20211204151901693.png)
2.   Bean 的加载过程 -- 代码层面
     -   ![image-20211204152101436](imgs/image-20211204152101436.png)
     -   ![image-20211204152121121](imgs/image-20211204152121121.png)
3.   Bean 的加载过程
     -   ![image-20211204152509152](imgs/image-20211204152509152.png)
4.   Spring 编程、协作模式
     -   ![image-20211204152550386](imgs/image-20211204152550386.png)
5.   示例

### Spring XML 配置原理

1.   XML 配置原理
     -   ![image-20211204160731330](imgs/image-20211204160731330.png)
2.   自动化 XML 配置工具：XmlBeans -> Spring-xbean
     -   原理
         -   根据Bean的字段结构，自动生成XSD 
         -   根据Bean的字段结构，加载XML文件
3.   Spring Bean 配置方式演化
     -   ![image-20211204171824602](imgs/image-20211204171824602.png)

### Spring Messaging

1.   同步消息 RPC
     -   ![image-20211204173344408](imgs/image-20211204173344408.png)
2.   异步消息 MQ
     -   ![image-20211204173410055](imgs/image-20211204173410055.png)
3.   生产－消费 Queue
     -   ![image-20211204173546996](imgs/image-20211204173546996.png)
4.   发布－订阅 Topic
     -   ![image-20211204173607554](imgs/image-20211204173607554.png)
5.   示例
     -    

### Spring -> Spring Boot

1.   Spring 配置的发展方向

     -   XML-- 全局
     -   注解　－－　类
     -   配置类　－－　方法
     -   Spring  4　以上的特性，走向Spring Boot

2.   Spring Boot 的出发点

     -   Spring 臃肿以后的必然选择
         -   一切为了简化
         -   让开发变简单
         -   让配置变简单
         -   让运行变简单
     -   怎么变简单？
         -   整合
         -   原则：约定大于配置

3.   Spring Boot 如何做到简化？

     -   Spring 本身技术的成熟与完善，各方面第三方组件的成熟集成 
     -   Spring 团队在去 web 容器化等方面的努力
     -   基于 MAVEN 与 POM 的 Java 生态体系，整合 POM 模板成为可能 
     -   避免大量 maven 导入和各种版本冲突

4.   什么是 Spring Boot

     -   Spring Boot 是 Spring 的一套快速配置脚手架，关注于自动配置，配置驱动。

     -   定义

         >   Spring Boot 使创建独立运行、生产级别的 Spring 应用变得容易，你可以直接运行它。
         >
         >   我们对 Spring 平台和第三方库采用限定性视角，以此让大家能在最小的成本下上手。
         >
         >   大部分 Spring Boot 应用仅仅需要最少量的配置。

     -   功能特性

         >   创建独立运行的 Spring 应用
         >   
         >   直接嵌入 Tomcat 或 Jetty，Undertow，无需部署 WAR 包
         >   
         >   提供限定性的 starter 依赖简化配置(就是脚手架)
         >   
         >   在必要时自动化配置 Spring 和其他三方依赖库
         >   
         >   提供生产 production-ready 特性，例如指标度量，健康检查，外部配置等 
         >   
         >   完全零代码生产和不需要 XML 配置
         >   

5.   快速构建基于 maven 项目

### Spring Boot 核心原理

1.   自动化配置: 简化配置核心 基于 Configuration，EnableXX，Condition

2.   spring-boot-starter: 脚手架核心 整合各种第三方类库，协同工具

3.   配置　-> starter

     -   ![image-20211204201601997](imgs/image-20211204201601997.png)

4.   约定大于配置

     -   优点：开箱即用　

     -   默认约定

         >   Maven 的目录结构:　默认有 resources 文件夹存放配置文件。默认打包方式为 jar。 
         >
         >   默认的配置文件:　application.properties 或 application.yml 文件
         >
         >   默认通过 spring.profiles.active 属性来决定运行环境时的配置文件。 
         >
         >   EnableAutoConfiguration 默认对于依赖的 starter 进行自动装载。
         >
         >   spring-boot-start-web 中默认包含 spring-mvc 相关依赖以及内置的 
         >   web 容器，使得构建 一个 web 应用更加简单。
     
5.   自动化配置原理

     -   ![image-20211204203631339](imgs/image-20211204203631339.png)

6.   Spring Boot 自动配置注解

     -   @SpringBootApplication
     -   @SpringBootConfiguration
     -   @EnableAutoConfiguration
     -   @AutoConfigurationPackage
     -   @Import({AutoConfigurationImportSelector.class})

7.   条件化自动配置

     -   @ConditionalOnBean 
     -   @ConditionalOnClass 
     -   @ConditionalOnMissingBean 
     -   @ConditionalOnProperty 
     -   @ConditionalOnResource 
     -   @ConditionalOnSingleCandidate 
     -   @ConditionalOnWebApplication

### Spring Boot Starter

1.   starter 所需要配置文件
     -   spring.provides 
     -   spring.factories 
     -   additional-xxxx-metadata 
     -   自定义 Configuration 类
2.   示例
     -   `shardingsphere -> shardingsphere-jdbc-core-spring-boot-starter`

### JDBC / 数据库连接池

1.   JDBC 定义了数据库交互接口：
     -   DriverManager
     -   Connection
     -   Statement
     -   ResultSet 
     -   DataSource--Pool
2.   JDBC - Java 操作数据库的核心
     -   Java 操作数据库的各种类库，都可以看做是在 JDBC 上做的增强实现
3.   数据库连接池
     -   C3P0
     -   DBCP--Apache CommonPool 
     -   Druid
     -   Hikari（推荐）

### ORM -- Hibernate / MyBatis

0.   ORM 
     -   实现 Java Obj -> SQL 的转换

1.   Hibernate
     -    国际使用最多

2.   MyBatis
     -   国内使用最多

     -   半自动化的 ORM

         >   映射文件　mapper.xml　定义 map 规则和 SQL
         >
         >   定义　mapper/DAO ，基于 xml 规则，操作数据库

3.   Hibernate vs MyBatis

     -   Mybatis 优点: 原生SQL(XML 语法)，直观，对 DBA 友好

         >   对 DBA友好
         >
         >   对性能调优友好
         >
         >   对 SQL 审计友好

     -   Hibernate 优点: 简单场景不用写 SQL(HQL、Cretiria、SQL)

     -   Mybatis 缺点: 繁琐，可以用 MyBatis-generator、MyBatis-Plus 之类的插件

     -   Hibernate 缺点: 对DBA 不友好

### Spring / Spring Boot 集成 ORM / JPA

1.   JPA

     -   全称 Java Persistence API

         >   即 Java 持久化 API，是一套基于 ORM 的规范，内部是由一系列的接口和抽象类构成。

     -   核心 EntityManager

2.   Spring JDBC vs ORM

     -   ![image-20211206205554426](imgs/image-20211206205554426.png)

3.   Spring 管理事务

     -   JDBC 

         >   编程式事务管理

     -   Spring 

         >   声明式事务管理:事务管理器+AOP

     -   Spring 声明式事务配置参考

         >   事务的传播性:
         >
         >   @Transactional(propagation=Propagation.REQUIRED)
         >
         >   
         >
         >   事务的隔离级别:
         >
         >   @Transactional(isolation = Isolation.READ_UNCOMMITTED) 
         >
         >   读取未提交数据(会出现脏读，不可重复读) 基本不使用
         >
         >   
         >
         >   只读:
         >
         >   @Transactional(readOnly=true)
         >   该属性用于设置当前事务是否为只读事务，设置为 true 表示只读，false 则表示可读写，默认值为 false。 
         >
         >   
         >
         >   事务的超时性:
         >
         >   @Transactional(timeout=30)
         >
         >   
         >
         >   回滚:
         >
         >   指定单一异常类:@Transactional(rollbackFor=RuntimeException.class) 
         >
         >   指定多个异常类:@Transactional(rollbackFor={RuntimeException.class, Exception.class})
     
4.   示例

     -   `shardingsphere -> examples`
     -   Spring 集成 MyBatis
     -   Spring 集成 Hibernate/JPA
     -   Spring Boot 集成 JPA/Hibernate
     -   Spring Boot 集成 MyBatis

5.   Spring/Spring Boot 使用 ORM 的经验

     -   本地事务(事务的设计与坑) 
     -   多数据源(配置、静态制定、动态切换) 
     -   数据库连接池池配置(大小、重连、超时) 
     -   ORM 内的复杂 SQL，级联查询
     -   ORM 辅助工具和插件

