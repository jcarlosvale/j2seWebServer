package com.test.king.webserver;

import com.test.king.constants.HttpCode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.Assert.assertEquals;

public class SimpleHttpServerTest {

    private SimpleHttpServer simpleHttpServer;
    private HttpClient httpClient;

    @Before
    public void setup() throws IOException {
        simpleHttpServer = new SimpleHttpServer(8080,10);
        simpleHttpServer.start();

        httpClient = HttpClient.newBuilder().build();
    }

    @After
    public void stop() {
        simpleHttpServer.stop();
    }

    @Test
    public void someGet() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/1234/login")).build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(HttpCode.OK.getCode(), response.statusCode());
        assertEquals(HttpCode.OK.getMessage(), response.body());
    }

}
