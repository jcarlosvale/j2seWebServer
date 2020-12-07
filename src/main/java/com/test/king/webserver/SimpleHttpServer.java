package com.test.king.webserver;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import sun.misc.Signal;
import sun.misc.SignalHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SimpleHttpServer implements SignalHandler {

    private final static Logger logger = Logger.getLogger(SimpleHttpServer.class.getName());

    private HttpServer server;
    private ExecutorService httpThreadPool;

    public void start(int port, int maxConnections) throws IOException {
        logger.info("Starting server");
        server = HttpServer.create(new InetSocketAddress(port), maxConnections);
        HttpContext context = server.createContext("/");
        context.setHandler(SimpleHttpServer::handleRequest);

        httpThreadPool = Executors.newCachedThreadPool();
        server.setExecutor(httpThreadPool);

        server.start();
        logger.log(Level.INFO, () -> "Server started using port " + port);
        logger.log(Level.INFO, () -> "Use Control-C to stop this server");
    }

    private static void handleRequest(HttpExchange httpExchange) throws IOException {
        String response = "Hi there!";
        httpExchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    @Override
    public void handle(Signal sig) {
        logger.info("Signal [" + sig.getName() + "] is received, stopServer soon...");
        stopServer();
        logger.info("Stop successfully.");
    }

    private void stopServer() {
        server.stop(1);
        httpThreadPool.shutdown();
    }

    /*
    I use the below code to start it

    this.httpServer = HttpServer.create(addr, 0);
    HttpContext context = this.httpServer.createContext("/", new DocumentProcessHandler());
    this.httpThreadPool = Executors.newFixedThreadPool(this.noOfThreads);
    this.httpServer.setExecutor(this.httpThreadPool);
    this.httpServer.start();
and below code to stop it

        this.httpServer.stop(1);
        this.httpThreadPool.shutdownNow();
     */
}
