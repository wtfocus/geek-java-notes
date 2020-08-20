[toc]

## 11 | Java 提供了哪些 IO 方式？NIO 如何实现多路复用？

### 问题

-   **Java 提供了哪些 IO 方式？NIO 如何实现多路复用？**

### 回答

1.  BIO，同步、阻塞。
2.  NIO，同步、非阻塞。使用多路复用机制。
3.  NIO 2（AIO），异步、非阻塞。基于事件和回调机制。

### 扩展

#### 基本概念

1.  同步/异步
    -   同步：有序运行机制。
    -   异步：常依靠事件、回调等机制。
2.  阻塞/非阻塞
    -   阻塞：线程等待返回。
    -   非阻塞：直接返回，相应操作后台继续处理。

#### NIO 主要组成

1.  Buffer，高效数据容器。

2.  Channel，是 NIO 中被用来支持批量式 IO 操作的一种抽象。

3.  Selector，是 NIO 实现多路复用的基础。它可以检测到注册在 Selector 上的多个 Channel 中，是否有 Channel 处于就绪状态，进而实现单线程对多 Channel 的高效管理。

4.  Chartset，提供 Unicode 字符串定义，和相应的编码/解码等。

    -   ```java
        Charset.defaultCharset().encode("Hello world!"));
        ```

#### NIO 解决的问题

1.  使用 java.io 和 java.net 中的**同步、阻塞式** API，实现简单的 Socket 服务器。

    -   ```java
        public class DemoServer extends Thread{
            private ServerSocket serverSocket;
        
            public void run() {
                try {
        //            ExecutorService pool = Executors.newFixedThreadPool(8);
                    serverSocket = new ServerSocket(8888);
                    while (true) {
                        Socket socket = serverSocket.accept();
                        RequestHandler requestHandler = new RequestHandler(socket);
                        requestHandler.start();
        //                pool.execute(requestHandler);
                    }
                } catch (IOException exc) {
                    exc.printStackTrace();
                } finally {
                    if (serverSocket != null) {
                        try {
                            serverSocket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        
            public static void main(String[] args) {
                DemoServer demoServer = new DemoServer();
                demoServer.start();
            }
        }
        
        
        class RequestHandler extends Thread {
            private Socket socket;
        
            RequestHandler(Socket socket) {
                this.socket = socket;
            }
        
            public void run() {
                try (PrintWriter out = new PrintWriter(socket.getOutputStream())) {
                    out.println("Hello world. -- " + this.getName());
                    out.flush();
                } catch (Exception exc) {
                    exc.printStackTrace();
                }
            }
        }
        ```

    -   缺点：每一个 Client 启动一个线程，开销较大。

2.  使用**线程池**来避免浪费

    -   ```java 
            private ServerSocket serverSocket;
        
            public void run() {
                try {
                    // 创建线程池
                    ExecutorService pool = Executors.newFixedThreadPool(8);
                    serverSocket = new ServerSocket(8888);
                    while (true) {
                        Socket socket = serverSocket.accept();
                        RequestHandler requestHandler = new RequestHandler(socket);
                        requestHandler.start();
                        // 分配执行线程
                        pool.execute(requestHandler);
                    }
                } catch (IOException exc) {
                    exc.printStackTrace();
                } finally {
                    if (serverSocket != null) {
                        try {
                            serverSocket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        
        ```

    -   缺点：高并发时，线程上下文切换会成为瓶颈。

3.  NIO 引入的多路复用机制

    -   ```java
        public class NIOServer extends Thread{
        
            public void run() {
                try (
                        // 创建一个 Selector 实例
                        Selector selector = Selector.open();
                        // 创建一个供服务器使用的 ServerSocketChannel 实例
                        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()
                        ){
        
                    serverSocketChannel.bind(new InetSocketAddress(InetAddress.getLocalHost(), 8888));
                    // 用于指定服务器处理请求的方式是阻塞的还是非阻塞的，对于Java NIO都是以非阻塞的方式进行处理的
                    // 阻塞模式下，注册操作是不允许的，会抛出 IllegalBlockingModeException 异常。
                    serverSocketChannel.configureBlocking(false);
        
                    // 注册到指定 Selector，并设计关注点。可选如下几种：
                    //      SelectionKey.OP_CONNECT：监听 Channel 建立连接事件
                    //      SelectionKey.OP_READ：监听 Channel 的可读取事件，也即客户端已经发送数据过来，此时可以读取
                    //      SelectionKey.OP_WRITE：监听 Channel 的可写事件，即当前可以写入数据到 Channel 中
                    //      SelectionKey.OP_ACCEPT：监听 Channel 套接字事件
                    serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
                    while (true) {
        
                        // 阻塞，等待就绪的 Channel。
                        selector.select();
                        // 获取当前所有有事件到达的客户端 Channel 对应的 SelectionKey 实例
                        Set<SelectionKey> selectionKeySet = selector.selectedKeys();
                        Iterator<SelectionKey> iter = selectionKeySet.iterator();
        
                        while (iter.hasNext()) {
                            SelectionKey selectionKey = iter.next();
                            sayHelloWorld((ServerSocketChannel) selectionKey.channel());
                            iter.remove();
                        }
                    }
                } catch (IOException exc) {
                    exc.printStackTrace();
                }
            }
        
            private void sayHelloWorld(ServerSocketChannel server) throws IOException {
                try (SocketChannel client = server.accept()) {
        
                    client.write(Charset.defaultCharset().encode("Hello World. -- " + new Thread().getName()));
                }
            }
        
            public static void main(String[] args) {
                NIOServer nioServer = new NIOServer();
                nioServer.start();
            }
        }
        
        ```

    -   优点：利用单线程轮询事件的机制，定位就绪的 Channel。仅仅是 select 阶段阻塞，有效避免大量客户端连接时，频繁线程切换带来的开销。

4.  使用 NIO 2（AIO），利用事件和回调，处理 Accept、Read 等操作。

    -   ```java
        public class AIOServer extends Thread{
        
            public void run() {
                try {
                    AsynchronousServerSocketChannel serverSocket = AsynchronousServerSocketChannel
                            .open()
                            .bind(new InetSocketAddress(InetAddress.getLocalHost(), 8888));
                    
                    // 为异步操作指定回调
                    serverSocket.accept(serverSocket, new CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel>() {
                        @Override
                        public void completed(AsynchronousSocketChannel result, AsynchronousServerSocketChannel attachment) {
                            serverSocket.accept(serverSocket, this);
                            sayHelloWorld(
                                    result,
                                    Charset.defaultCharset().encode("Hello World. -- " + new Thread().getName()));
                        }
        
                        @Override
                        public void failed(Throwable exc, AsynchronousServerSocketChannel attachment) {
        
                        }
                    });
        
                } catch (IOException exc) {
                    exc.printStackTrace();
                }
            }
        
            private void sayHelloWorld(AsynchronousSocketChannel socketChannel, ByteBuffer byteBuffer) {
                socketChannel.write(byteBuffer);
            }
        
            public static void main(String[] args) {
                AIOServer nioServer = new AIOServer();
                nioServer.start();
            }
        }
        ```

    -   