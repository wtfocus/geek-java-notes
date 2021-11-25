package gateway.outbound;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import log.Logs;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class HttpOutboundHandler {

    ExecutorService executor;
    String backendUrl;
    CloseableHttpClient httpclient;

    public HttpOutboundHandler(List<String> backendUrls) {

        this.backendUrl = backendUrls.get(0);

        int corePoolSize = 4;
        int maximumPoolSize = 4;
        long keepAliveTime = 1000;
        int queueSize = 4;

        this.executor = new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(queueSize)
        );

        httpclient = HttpClients.createDefault();
    }

    public void handle(FullHttpRequest fullHttpRequest, ChannelHandlerContext ctx) {
        String url = this.backendUrl + fullHttpRequest.uri();

        this.executor.submit(()->fetchGet(fullHttpRequest, ctx, url));
    }

    public void fetchGet(FullHttpRequest fullHttpRequest, ChannelHandlerContext ctx, String url) {
        HttpGet httpGet = new HttpGet(url);
        try {
            httpclient.execute(httpGet, response -> {
                Logs.BUSINESS.info(
                        "handleResponse. url: {}; recode: {}", url, response.getStatusLine().getStatusCode());

                handleResponse(fullHttpRequest, ctx, response);
                return null;
            });
        } catch (Exception e) {
            e.printStackTrace();
            Logs.BUSINESS.error("req err. url: {};", url);
            handleResponse(fullHttpRequest, ctx, null);
        }
    }

    private void handleResponse(FullHttpRequest fullHttpRequest, ChannelHandlerContext ctx, HttpResponse resp) {
        FullHttpResponse response = null;

        if (resp != null) {
            Logs.BUSINESS.info("endpoint resp recode: {}, content len: {}",
                    resp.getStatusLine().getStatusCode(), resp.getFirstHeader("Content-Length").getValue());

            if (resp.getStatusLine().getStatusCode() == 200) {
                try {
                    HttpEntity entity = resp.getEntity();
                    byte[] body = EntityUtils.toByteArray(entity);

                    response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(body));

                    response.headers().set("Content-Type", "application/json");
                    response.headers().setInt("Content-Length", Integer.parseInt(resp.getFirstHeader("Content-Length").getValue()));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (response == null) {
            byte[] body = "req err".getBytes();
            response = new DefaultFullHttpResponse(HTTP_1_1, BAD_REQUEST, Unpooled.wrappedBuffer(body));
            response.headers().set("Content-Type", "application/json");
            response.headers().setInt("Content-Length", body.length);
        }

        ctx.write(response);
        ctx.flush();

    }
}
