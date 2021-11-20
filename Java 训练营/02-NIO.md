[toc]

## NIO

### 服务器通信原理

1. 通信模型

	- ![image-20211113191044564](imgs/image-20211113191044564-6801847.png)

2.  HTTP 服务器 01

  - 流程

  	> 1. 创建一个 ServerSocket
  	> 2. 绑定端口
  	> 3. 通过 accept 方法拿到 Socket，进行处理。
  	> 	- Content-Length 指定内容长度
  	> 4. 模拟输出
  	> 5. 关闭socket

  -   idea 启动多进程时，添加　-Xmx512，设置最大堆内存。

  -   代码

  	- 

  -  压测工具

    - windows/mac：sb (SuperBenchmarker)

    	> sb -u http://localhost:8001/ -c 40 -N 30

    - linux: Apache ab / wrk
    
  - 压测结果

  	> wangtaodeMac-mini-2:02NIO wangtao$ wrk -c 40 -d 30s http://127.0.0.1:8001
  	> Running 30s test @ http://127.0.0.1:8001
  	>   2 threads and 40 connections
  	>   Thread Stats   Avg      Stdev     Max   +/- Stdev
  	>     Latency     6.44ms   78.32ms   1.91s    99.30%
  	>     Req/Sec     1.41k     0.91k    3.81k    66.46%
  	>   22689 requests in 30.09s, 4.20MB read
  	>   Socket errors: connect 40, read 143671, write 41, timeout 0
  	> Requests/sec:    753.92
  	> Transfer/sec:    142.80KB

3. HTTP 服务器 02

  - 为每个客户端创建一个线程

  - 压测结果

  	> wangtaodeMac-mini-2:02NIO wangtao$ curl http://127.0.0.1:8002
  	> hello,nio01
  	>
  	> wangtaodeMac-mini-2:02NIO wangtao$ wrk -c 40 -d 30s http://127.0.0.1:8002
  	> Running 30s test @ http://127.0.0.1:8002
  	>   2 threads and 40 connections
  	>   Thread Stats   Avg      Stdev     Max   +/- Stdev
  	>     Latency     2.60ms    1.49ms  60.65ms   97.54%
  	>     Req/Sec     0.92k   232.80     1.84k    76.72%
  	>   54878 requests in 30.08s, 13.98MB read
  	>   Socket errors: connect 0, read 442925, write 50, timeout 0
  	> Requests/sec:   1824.64
  	> Transfer/sec:    476.02KB

4. HTTP 服务器 03

  - 创建一个固定大小的线程池

  - 压测结果

  	> wangtaodeMac-mini-2:02NIO wangtao$ curl http://127.0.0.1:8003
  	> hello,nio03
  	>
  	> wangtaodeMac-mini-2:02NIO wangtao$ wrk -c 40 -d 30s http://127.0.0.1:8003
  	> Running 30s test @ http://127.0.0.1:8003
  	>   2 threads and 40 connections
  	>   Thread Stats   Avg      Stdev     Max   +/- Stdev
  	>     Latency     2.31ms   29.59ms 662.58ms   99.42%
  	>     Req/Sec     2.14k     1.02k    6.88k    84.98%
  	>   67572 requests in 30.05s, 16.03MB read
  	>   Socket errors: connect 39, read 248818, write 18, timeout 0
  	> Requests/sec:   2248.74
  	> Transfer/sec:    546.24KB

### 服务器通信过程分析

1.　操作分类
	1.　CPU
	2.　IO
2.　线程池大小
	1.　 线程 IO 操作时，不消耗 CPU，可以创建多于 CPU 核数的线程数量。
3.　对于一个 IO 相关应用来说，大量的 IO 会造成大部分　CPU 等资源，可能就就被浪费了。
	1.　多线程，管理成本较高。上下文切换频繁。
4.　深入一层的看问题
	1.　不仅面临线程/CPU 的问题
	2.　还要面对数据来回复制的问题。（用户空间/内核空间）

### IO 模型与相关概念

1. 阻塞、非阻塞，是线程处理模式。

2. 同步、异步，是通信模式。

3. 五种 IO 模型

	1. |      | 阻塞          | 非阻塞              |
		| ---- | ------------- | ------------------- |
		| 同步 | 阻塞 I/O 模型 | 非阻塞 I/O 模型     |
		| 同步 | I/O 复用模型  | 信号驱动的 I/O 模型 |
		| 异步 |               | 异步 I/O 模型       |

4. 阻塞 I/O 模型

  1. BIO

      -   ![image-20211117224427714](imgs/image-20211117224427714.png)

      -   

5. 非阻塞 I/O 模型

  1.　轮询查看数据是否准备好
  2.　类 BIO
      -   ![image-20211117224338516](imgs/image-20211117224338516.png)

6. I/O 多路复用模型

  1.　先阻塞在　select/poll/epoll 上，（内核轮询）
  2.　再阻塞在 IO 操作上
  3.　epoll !  ，可以看作 select/poll 的改进。
  4.　Reactor 模式
      -   ![image-20211117234207462](imgs/image-20211117234207462.png)

7. 信号驱动的 I/O 模型

  1.　事件驱动模式
      -   ![image-20211117234806340](imgs/image-20211117234806340.png)
  2.　线程池 -> EDA -> SEDA
      -   ![image-20211117234743676](imgs/image-20211117234743676.png)

8. 异步 I/O 模型

  1.　![image-20211117235011301](imgs/image-20211117235011301.png)
  2.　Proactor 模式
      -   ![image-20211117235528754](imgs/image-20211117235528754.png)

### Netty 概览

0. Netty 是一个 Java 的网络应用开发框架。

1. 概览
    -   ![image-20211118001035377](imgs/image-20211118001035377.png)
    
2. 特点
  1. 异步
  2. 事件驱动
  3. 基于 NIO
  
3. 适用于
  1. 服务器
  2. 客户端
  3. TCP/UDP/HTTP
  
4. 特性
  1. 高性能协议服务器

  	> 高吞吐
  	> 低延迟
  	> 低开销
  	> 零拷贝
  	> 可扩容

  2. 松耦合：网络和业务逻辑分离

  3. 使用方便、可维护性好

5. 兼容性

    - JDK 兼容性

    	> Netty 3.x: JDK5
    	>
    	> Netty 4.x: JDK6
    	>
    	> Netty 5.x （已废弃）

    - 协议兼容性

    	> 兼容大部分通用协议
    	>
    	> 支持自定义协议

6. 嵌入式应用
  1.　HTTP Server
  2.　HTTPS Server
  3.　WebSocket Server
  4.　TCP Server
  5.　UDP Server
  6.　In VM Pipe

7.  Netty 中的基本概念
  1. Channel

  	> 通道，Java NIO 中的基础概念。

  2. ChannelFuture

  	> Java 的 Future 接口。

  3. Event & Handler

  	> Netty 基于事件驱动。

  4. Encoder & Decoder

  	> 处理网络 IO 时，需要转换 Java 对象与字节流。（序列化与反序列化）

  5. ChannelPipeline

  	> 事件处理链。

8. Netty 应用组成

    - 网络事件
    - 应用程序逻辑事件
    - 事件处理程序

9. Event & Handler
  1. 入站事件

  	> 通道激活和停用
  	>
  	> 读操作事件
  	>
  	> 异常事件
  	>
  	> 用户事件

  2. 出站事件

  	> 打开连接
  	>
  	> 关闭连接
  	>
  	> 写入数据
  	>
  	> 刷新数据

  3. 事件处理程序接口

  	> ChannelHandler 
  	>
  	> ChannelOutboundHandler
  	>
  	> ChannelInboundHandler

  4. 适配器（空实现，需要继承使用）

  	> ChannelInboundHandlerAdapter
  	>
  	> ChannelOutboundHandlerAdapter

10. demo

    - 压测结果

    	> wangtaodeMac-mini-2:02NIO wangtao$ wrk -c 40 -d 30s http://127.0.0.1:8801
    	> Running 30s test @ http://127.0.0.1:8801
    	>   2 threads and 40 connections
    	>   Thread Stats   Avg      Stdev     Max   +/- Stdev
    	>     Latency   264.45us    1.09ms  69.08ms   99.59%
    	>     Req/Sec    81.38k     6.74k   99.15k    87.00%
    	>   4859333 requests in 30.01s, 500.50MB read
    	> Requests/sec: 161925.78
    	> Transfer/sec:     16.68MB

### Netty 原理

1. 什么是高性能

  1. 高并发用户

  	> 业务指标 -- QPS
  	>
  	> wrk -c {并发用户数}
  	>
  	> Requests/sec: 161925.78 QPS
  	> Transfer/sec:     16.68MB  TPS

  2. 高吞吐量
    
    > 技术指标 -- TPS
    
  3. 低延迟

    > 技术指标
    >
    > 使用百分位指标，而非平均指标
    >
    >  wrk --latency 打印延迟统计信息
    >
    >   Latency Distribution
    >      50%  227.00us
    >      75%  232.00us
    >      90%  238.00us
    >      99%  446.00us

  4. 容量

2. 高性能的另一方面（副作用）

	1. 系统复杂度 ×10以上
	2. 建设与维护成本　++++
	3. 故障或　BUG 导致的破坏性　×10　以上

3. 应对策略：稳定性建议（混沌工程）

  1. 容量/现状 （自省、自知）

  	> 86400s/day 
  	>
  	> 淘宝 TPS 3000W / 86400 ~~ 300/s
  	>
  	> 高并发业务：活动、秒杀

  2. 爆炸半径

  	> 影响范围

  3. 工程方面积累与改进

  	> 积累、分析、改进、标准流程

4. Netty 运行原理

	1. BIO 多线程模型

		- ![image-20211120191347632](imgs/image-20211120191347632.png)

	2. 事件处理机制

		- ![image-20211120191425107](imgs/image-20211120191425107.png)

	3. Reactor 模型

		- ![image-20211120191455427](imgs/image-20211120191455427.png)

		- Service Handler 

			> 会同步的将输入的请求多路复用的分发给相应的 Event Handler。
			>
			> 分隔 IO 与 业务

	4. Netty NIO -- 01

		- ![image-20211120192921955](imgs/image-20211120192921955.png)
		- ![image-20211120193131579](imgs/image-20211120193131579.png)

	5. Netty NIO -- 02

		- ![image-20211120193304692](imgs/image-20211120193304692.png)
		- ![image-20211120194020325](imgs/image-20211120194020325.png)

	6. Netty NIO -- 03

		- ![image-20211120194115571](imgs/image-20211120194115571.png)
		- ![image-20211120201829569](imgs/image-20211120201829569.png)

	7. Netty 对三种模式的支持

		- ![image-20211120201916635](imgs/image-20211120201916635.png)

	8. Netty 启动和处理流程

		- ![image-20211120202223971](imgs/image-20211120202223971.png)

	9. Netty 线程模式 -- EventLoop

		- ![image-20211120202358809](imgs/image-20211120202358809.png)

	10. Netty 核心对象 -- EventLoop

		- ![image-20211120202658604](imgs/image-20211120202658604.png)

	11. Netty 运行原理

		- ![image-20211120203502562](imgs/image-20211120203502562.png)

5. 关键对象

  0. 流程图
  	- ![image-20211120204025529](imgs/image-20211120204025529.png)

  1. Bootstrap

  	> 启动线程，开启socket

  2. EventLoopGroup

  3. EventLoop

  4. SocketChannel

  	> 连接

  5. ChannelInitializer

  	> 初始化

  6. ChannelPipline

  	> 处理器链

  7. ChannelHandler

  	> 处理器

6. ChannelPipline

  1. 流程图
    - ![image-20211120204226898](imgs/image-20211120204226898.png)
  2. inbound
  3. outbound

7. Event & Handler

  1. 入站事件
  	1. 通道激活和停用
  	2. 读操作事件
  	3. 异常事件
  	4. 用户事件
  2. 出站事件
  	1. 打开连接
  	2. 关闭连接
  	3. 写入数据
  	4. 刷新数据

### Netty 网络程序优化

1. 粘包与拆包（tcp 上层）

  1. 模型图

    - ![image-20211120210118102](imgs/image-20211120210118102.png)

  2. ByteToMessageDecoder 一些常见的实现类

    - FixedLengthFrameDecoder

    	> 定长协议解码器

    - LineBasedFrameDecoder

    	> 行分隔符解码器

    - DelimiterBasedFrameDecoder

    	> 分隔符解码器

    - LengthFieldBasedFrameDecoder

    	> 长度编码解码器

    - JsonObjectDecoder

    	> Json 格式解码器

2. Nagle 与 TCP_NODELAY

  1.　MTU，最大传输单元，1500 Byte
  2.　MSS，最大分段大小，1460 Byte
  3.　Nagle 算法优化
  	1.　优化条件
  		- 缓冲区满
  		- 达到超时

3. 连接优化

  1.　TIME_WAIT 2MSL
  	1.　降低时间
  	2.　端口复用

4. Netty 优化

  1.　不要阻塞　EventLoop

  2.　系统参数优化

  	- fd 文件描述符限制

  	- TIME_WAIT 2MSL

  		> Linux ulimit -a /proc/sys/net/ipv4/tcp_fin_timeout
  		>
  		> Windows TcpTimeWaitDelay

  3.　缓冲区优化

  	1.　SO_RCVBUF　接收缓冲
  	2.　SO_SNDBUF　发送缓冲
  	3.　SO_BACKLOG 保持连接状态
  	4.　REUSEXXX

  4.　心跳周期优化

  	1.　心跳机制与断线重连

  5.　内存与　ByteBuffer 优化

  	1.　DirectBuffer 与　HeapBuffer

  6.　其他优化

  	1.　ioRatio
  	2.　Watermark
  	3.　TrafficShaping

  ### API 网关

  1. 职能
  	1. 请求接入，所有 API　接口服务请求的接入点。
  	2. 业务聚合，作为所有后端业务服务的聚合点
  	3. 中介策略，实现安全、验证、路由、过滤、流控等策略
  	4. 统一管理，对所有 API 服务和策略进行统一管理
  2. 分类
  	1. 流量网关
  		1. 关注稳定与安全
  			1. 流控
  			2. 日志
  			3. 防SQL 注入
  			4. 防 WEb 攻击
  			5. 屏蔽工具
  			6. 黑白 IP 名单
  			7. 证书/加解密处理
  	2. 业务网关
  		1. 提供更好的服务
  			1. 服务级别流控
  			2. 服务降级与熔断
  			3. 路由、LB、灰度
  			4. ……

