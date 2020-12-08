package com.test.king;

import com.test.king.webserver.SimpleHttpServer;

import java.io.IOException;

public class RankingApiApplication {

    public static void main(String[] args) throws IOException {
        new SimpleHttpServer(8080, 10).start();
    }
}
