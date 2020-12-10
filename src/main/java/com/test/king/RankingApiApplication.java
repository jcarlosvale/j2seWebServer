package com.test.king;

import com.test.king.webserver.SimpleHttpServer;

import java.io.IOException;

/**
 * Executable class to start the WebServer and specific http handler
 * It is possible to start the application in a different PORT and using different ThreadPool size:
 * ex1: java -jar target\rankingAPI-1.0.jar 8081  --> starting at port 8081
 * ex2:  java -jar target\rankingAPI-1.0.jar 8081 5  --> starting at port 8081 and thread pool size 5
 * Default values:
 * - port 8080
 * - size of thread pool = 10
 */
public class RankingApiApplication {

    public static void main(String[] args) throws IOException {
        int port = extractPort(args);
        int maxThreadPool  = extractMaxThreadPool(args);
        if (args.length > 1) {
            try {
                maxThreadPool = Integer.parseInt(args[1]);
            } catch (Exception e) {
                maxThreadPool = 10;
            }
        }
        new SimpleHttpServer(port, maxThreadPool).start();
    }

    private static int extractMaxThreadPool(String[] args) {
        if (args.length > 1) {
            try {
                int maxThreadPool = Integer.parseInt(args[1]);
                if (maxThreadPool <= 0) {
                    throw new NumberFormatException();
                }
                return maxThreadPool;
            } catch (Exception e){ }
        }
        return 10;
    }

    private static int extractPort(String[] args) {
        if (args.length > 0) {
            try {
                int port = Integer.parseInt(args[0]);
                if (port <= 0) {
                    throw new NumberFormatException();
                }
                return port;
            } catch (Exception e) { }
        }
        return 8080;
    }
}
