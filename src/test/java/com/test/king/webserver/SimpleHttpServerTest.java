package com.test.king.webserver;

import com.test.king.constants.HttpCode;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;

import static org.junit.Assert.assertEquals;

public class SimpleHttpServerTest {

    private static SimpleHttpServer simpleHttpServer;
    private static HttpClient httpClient;

    @BeforeClass
    public static void setup() throws IOException {
        if (Objects.isNull(simpleHttpServer)) {
            simpleHttpServer = new SimpleHttpServer(8080,10);
            simpleHttpServer.start();
            httpClient = HttpClient.newBuilder().build();
        }
    }

    @AfterClass
    public static void stop() {
        simpleHttpServer.stop();
    }

    @Test
    public void testInvalidHttpMethodDelete() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/1234/login"))
                .DELETE()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(HttpCode.METHOD_NOT_ALLOWED.getCode(), response.statusCode());
        assertEquals(HttpCode.METHOD_NOT_ALLOWED.getMessage(), response.body());
    }

    @Test
    public void testInvalidHttpMethodPut() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/1234/login"))
                .PUT(HttpRequest.BodyPublishers.ofString("someString"))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(HttpCode.METHOD_NOT_ALLOWED.getCode(), response.statusCode());
        assertEquals(HttpCode.METHOD_NOT_ALLOWED.getMessage(), response.body());
    }

    @Test
    public void testInvalidIdGetRequest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/1a23/login"))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(HttpCode.BAD_REQUEST.getCode(), response.statusCode());
        assertEquals(HttpCode.BAD_REQUEST.getMessage(), response.body());

        request = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/1a23/highscorelist"))
                .build();
        response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(HttpCode.BAD_REQUEST.getCode(), response.statusCode());
        assertEquals(HttpCode.BAD_REQUEST.getMessage(), response.body());

        request = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/-123/login"))
                .build();
        response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(HttpCode.BAD_REQUEST.getCode(), response.statusCode());
        assertEquals(HttpCode.BAD_REQUEST.getMessage(), response.body());

        request = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/-123/highscorelist"))
                .build();
        response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(HttpCode.BAD_REQUEST.getCode(), response.statusCode());
        assertEquals(HttpCode.BAD_REQUEST.getMessage(), response.body());
    }

    @Test
    public void testInvalidUriGetRequest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/123/someEndpoint"))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(HttpCode.BAD_REQUEST.getCode(), response.statusCode());
        assertEquals(HttpCode.BAD_REQUEST.getMessage(), response.body());

        request = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/123/123/login"))
                .build();
        response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(HttpCode.BAD_REQUEST.getCode(), response.statusCode());
        assertEquals(HttpCode.BAD_REQUEST.getMessage(), response.body());

        request = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/123/123/highscorelist"))
                .build();
        response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(HttpCode.BAD_REQUEST.getCode(), response.statusCode());
        assertEquals(HttpCode.BAD_REQUEST.getMessage(), response.body());
    }

    @Test
    public void testInvalidUriPostRequest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/123/someEndpoint?sessionkey=UICSNDK"))
                .POST(HttpRequest.BodyPublishers.ofString("1500"))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(HttpCode.BAD_REQUEST.getCode(), response.statusCode());
        assertEquals(HttpCode.BAD_REQUEST.getMessage(), response.body());

        request = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/123/123/score?sessionKEY=UICSNDK"))
                .POST(HttpRequest.BodyPublishers.ofString("1500"))
                .build();
        response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(HttpCode.BAD_REQUEST.getCode(), response.statusCode());
        assertEquals(HttpCode.BAD_REQUEST.getMessage(), response.body());

        request = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/123/123/score?sessionkey=UICSNDK"))
                .POST(HttpRequest.BodyPublishers.ofString("1500"))
                .build();
        response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(HttpCode.BAD_REQUEST.getCode(), response.statusCode());
        assertEquals(HttpCode.BAD_REQUEST.getMessage(), response.body());
    }

    @Test
    public void testInvalidIdPostRequest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/1a23/score?sessionkey=UICSNDK"))
                .POST(HttpRequest.BodyPublishers.ofString("1500"))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(HttpCode.BAD_REQUEST.getCode(), response.statusCode());
        assertEquals(HttpCode.BAD_REQUEST.getMessage(), response.body());

        request = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/-123/score?sessionkey=UICSNDK"))
                .POST(HttpRequest.BodyPublishers.ofString("1500"))
                .build();
        response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(HttpCode.BAD_REQUEST.getCode(), response.statusCode());
        assertEquals(HttpCode.BAD_REQUEST.getMessage(), response.body());
    }

    @Test
    public void testInvalidScorePostRequest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/123/score?sessionkey=UICSNDK"))
                .POST(HttpRequest.BodyPublishers.ofString("someScore"))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(HttpCode.BAD_REQUEST.getCode(), response.statusCode());
        assertEquals(HttpCode.BAD_REQUEST.getMessage(), response.body());

        request = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/123/score?sessionkey=UICSNDK"))
                .POST(HttpRequest.BodyPublishers.ofString("-1"))
                .build();
        response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(HttpCode.BAD_REQUEST.getCode(), response.statusCode());
        assertEquals(HttpCode.BAD_REQUEST.getMessage(), response.body());
    }
}
