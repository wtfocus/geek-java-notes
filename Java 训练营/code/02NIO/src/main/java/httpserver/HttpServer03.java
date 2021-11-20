package httpserver;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer03 {
    public static void main(String[] args) throws IOException {
        ExecutorService executorService = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors() + 2
        );
        ServerSocket serverSocket = new ServerSocket(8003);
        while (true) {
            try{
                final Socket socket = serverSocket.accept();
                executorService.execute(() -> {
                    service(socket);
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void service(Socket socket) {
        try {
            String body = "hello,nio03";
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            printWriter.println("HTTP/1.1 200 OK");
            printWriter.println("Content-Type:text/html;charset=utf-8");
            printWriter.println("Content-Length:" + body.getBytes().length);
            printWriter.println();
            printWriter.println(body);
            printWriter.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
