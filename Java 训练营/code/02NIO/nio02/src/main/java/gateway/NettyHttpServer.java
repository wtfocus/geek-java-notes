package gateway;

import gateway.inbound.HttpInboundServer;
import log.Logs;

import java.util.Arrays;
import java.util.List;

public class NettyHttpServer {

    public static void main(String[] args) throws InterruptedException {

        Logs.BUSINESS.info("NettyHttpServer start...");

        String ProxyServers = System.getProperty("ProxyServers", "http://127.0.0.1:8000");
        String ProxyPort = System.getProperty("ProxyPort", "8802");


        int port = Integer.parseInt(ProxyPort);
        List<String> servers = Arrays.asList(ProxyServers.split(","));

        HttpInboundServer server = new HttpInboundServer(port, servers);
        server.run();
    }
}
