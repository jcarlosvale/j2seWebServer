package com.test.king;

import com.test.king.config.LoggerConfiguration;
import com.test.king.webserver.SimpleHttpServer;
import sun.misc.Signal;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) throws IOException {
        new LoggerConfiguration();
        SimpleHttpServer simpleHttpServer = new SimpleHttpServer();
        Signal.handle(new Signal("INT"), simpleHttpServer);
        simpleHttpServer.start(8080, 0);
    }
}
