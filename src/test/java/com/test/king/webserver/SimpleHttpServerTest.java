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
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

    @Test
    public void testInvalidSessionKeyReadRequest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/123/score?sessionkey1=UICSNDK"))
                .POST(HttpRequest.BodyPublishers.ofString("someScore"))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(HttpCode.BAD_REQUEST.getCode(), response.statusCode());
        assertEquals(HttpCode.BAD_REQUEST.getMessage(), response.body());
    }

    @Test
    public void testUnauthorizedResponse() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/123/score?sessionkey=SOMETOKEN"))
                .POST(HttpRequest.BodyPublishers.ofString("1500"))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(HttpCode.UNAUTHORIZED.getCode(), response.statusCode());
        assertEquals(HttpCode.UNAUTHORIZED.getMessage(), response.body());
    }

    @Test
    public void testLoginSuccess() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/123/login"))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(HttpCode.OK.getCode(), response.statusCode());
        assertTrue(response.body().length() > 0);
    }

    @Test
    public void testScorePostSuccess() throws IOException, InterruptedException {
        int userId = new Random().nextInt(100);
        int levelId = new Random().nextInt(100);
        int score = new Random().nextInt(100);

        //request login
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/"+userId+"/login"))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        String sessionKey = response.body();

        //post
        request = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/"+levelId+"/score?sessionkey="+sessionKey))
                .POST(HttpRequest.BodyPublishers.ofString(String.valueOf(score)))
                .build();
        response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(HttpCode.CREATED.getCode(), response.statusCode());
    }

    @Test
    public void testHighScoreListSuccess() throws IOException, InterruptedException {
        //request login
        int userId = 1;
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/"+userId+"/login"))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        String sessionKey = response.body();

        //insert
        int score = 1500;
        int levelId = 2;
        request = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/"+levelId+"/score?sessionkey="+sessionKey))
                .POST(HttpRequest.BodyPublishers.ofString(String.valueOf(score)))
                .build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        //ranking
        request = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/"+levelId+"/highscorelist"))
                .build();
        response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        String expectedResponse = userId+"="+score;
        assertEquals(HttpCode.OK.getCode(), response.statusCode());
        assertEquals(expectedResponse, response.body());
    }
}
