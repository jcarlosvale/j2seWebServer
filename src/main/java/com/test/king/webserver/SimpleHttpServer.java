package com.test.king.webserver;

import com.sun.net.httpserver.HttpServer;
import com.test.king.constants.LogMessages;
import com.test.king.httpHandler.RankingHttpHandler;
import com.test.king.repository.SessionKeyRepository;
import com.test.king.service.SessionKeyService;
import sun.misc.Signal;
import sun.misc.SignalHandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class SimpleHttpServer implements SignalHandler {

    private static final Logger logger = Logger.getLogger(SimpleHttpServer.class.getName());

    private final HttpServer server;
    private final ExecutorService httpThreadPool;
    private final int port;

    public SimpleHttpServer(int port, int maxThreadPool) throws IOException {
        setupLogger();
        Signal.handle(new Signal("INT"), this);
        this.port = port;
        this.server = HttpServer.create(new InetSocketAddress(this.port), 0);
        RankingHttpHandler rankingHttpHandler =
                RankingHttpHandler.getInstance(SessionKeyService.getInstance(SessionKeyRepository.getInstance()));
        this.server.createContext("/", rankingHttpHandler::handleRequest);
        this.httpThreadPool = Executors.newFixedThreadPool(maxThreadPool);
        this.server.setExecutor(httpThreadPool);
    }

    private void setupLogger() throws IOException {
        InputStream inputStream =
                Thread.currentThread().getContextClassLoader().getResourceAsStream("logging.properties");
        LogManager.getLogManager().readConfiguration(inputStream);
    }

    public void start() {
        logger.log(Level.INFO, LogMessages.STARTING_SERVER::getMessage);

        server.start();

        logger.log(Level.INFO, LogMessages.SERVER_STARTED.getMessage(), String.valueOf(port));
        logger.log(Level.INFO, LogMessages.STOP_SERVER_ORIENTATION::getMessage);
    }

    @Override
    public void handle(Signal sig) {
        stop();
    }

    public void stop() {
        logger.log(Level.INFO, LogMessages.STOPPING_SERVER.getMessage());

        server.stop(1);
        httpThreadPool.shutdown();

        logger.log(Level.INFO, LogMessages.SERVER_STOPPED::getMessage);
    }
}
