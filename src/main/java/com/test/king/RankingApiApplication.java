package com.test.king;

import com.test.king.webserver.SimpleHttpServer;

import java.io.IOException;

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
            } catch (Exception e) { }
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
