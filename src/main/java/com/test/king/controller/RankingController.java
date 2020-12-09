package com.test.king.controller;

import com.sun.net.httpserver.HttpExchange;
import com.test.king.constants.HttpCode;
import com.test.king.constants.LogMessages;
import com.test.king.exceptions.InvalidIdException;
import com.test.king.exceptions.InvalidUriException;
import com.test.king.exceptions.RequestBodyReadException;
import com.test.king.exceptions.RequestSessionKeyReadException;
import com.test.king.service.SessionTokenService;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class RankingController {

    private static final Logger logger = Logger.getLogger(RankingController.class.getName());
    private static RankingController SINGLE_RANKING_CONTROLLER_INSTANCE;
    private final SessionTokenService sessionTokenService;


    private RankingController() {
        this.sessionTokenService = SessionTokenService.getInstance();
    }

    public static RankingController getInstance() {
        if (Objects.isNull(SINGLE_RANKING_CONTROLLER_INSTANCE)) {
            synchronized (RankingController.class) {
                SINGLE_RANKING_CONTROLLER_INSTANCE = new RankingController();
            }
        }
        return SINGLE_RANKING_CONTROLLER_INSTANCE;
    }

    public void handleRequest(HttpExchange httpExchange) {
        logger.log(Level.INFO, LogMessages.LOGIN_ENDPOINT_START.getMessage());
        logger.log(Level.INFO, LogMessages.METHOD.getMessage(), httpExchange.getRequestMethod());
        logger.log(Level.INFO, LogMessages.URI.getMessage(), httpExchange.getRequestURI());
        try {
            String requestMethod = httpExchange.getRequestMethod();
            switch (requestMethod.toUpperCase(Locale.ENGLISH)) {
                case "GET":
                    handleGetRequest(httpExchange);
                    break;
                case "POST":
                    handlePostRequest(httpExchange);
                    break;
                default:
                    handleResponse(httpExchange, HttpCode.METHOD_NOT_ALLOWED);
            }
        } catch (Exception exception) {
            logException(exception, httpExchange);
            handleResponse(httpExchange, HttpCode.BAD_REQUEST);
        }
}

    private void logException(Exception exception, HttpExchange httpExchange) {
        logger.log(Level.SEVERE, LogMessages.EXCEPTION_MESSAGE.getMessage(), exception.getMessage());
        if (exception instanceof NumberFormatException || exception instanceof InvalidIdException) {
            logger.log(Level.SEVERE, LogMessages.INVALID_ID.getMessage(), httpExchange.getRequestURI().getPath());
            return;
        }
        if (exception instanceof InvalidUriException) {
            logger.log(Level.SEVERE, LogMessages.INVALID_URI.getMessage(), httpExchange.getRequestURI().getPath());
            return;
        }
        if (exception instanceof RequestBodyReadException) {
            logger.log(Level.SEVERE, LogMessages.INVALID_BODY_REQUEST.getMessage());
            return;
        }
        if (exception instanceof RequestSessionKeyReadException) {
            logger.log(Level.SEVERE, LogMessages.INVALID_SESSION_KEY.getMessage(), httpExchange.getRequestURI().getQuery());
        }
    }

    /*
     *     Request: POST /<levelid>/score?sessionkey=<sessionkey>
     *     Request body: <score>
     *     Response: (nothing)
     *     <levelid> : 31 bit unsigned integer number
     *     <sessionkey> : A session key string retrieved from the login function.
     *     <score> : 31 bit unsigned integer number
     *     Example: POST http://localhost:8081/2/score?sessionkey=UICSNDK (with the post body: 1500)
     */
    private void handlePostRequest(HttpExchange httpExchange)
            throws InvalidUriException, InvalidIdException, RequestSessionKeyReadException, RequestBodyReadException {
        URI uri = httpExchange.getRequestURI();
        String[] segments = uri.getPath().split("/");
        validateUriSegments(segments);
        if (! segments[2].contains("score")) {
            throw new InvalidUriException();
        }
        String sessionKey = getSessionKey(uri.getQuery());
        int levelId = Integer.parseInt(segments[1]);
        int score = getScore(httpExchange.getRequestBody());
        sessionTokenService.saveUserScoreLevel(sessionKey, levelId, score);
        handleResponse(httpExchange, HttpCode.CREATED);
    }

    private String getSessionKey(String urlQuery) throws RequestSessionKeyReadException {
        try {
            String sessionKey = urlQuery.split("sessionkey=")[1];
            logger.log(Level.INFO, LogMessages.SESSION_KEY.getMessage(), sessionKey);
            return sessionKey;
        } catch (Exception exception) {
            throw new RequestSessionKeyReadException(exception.getMessage());
        }
    }

    private int getScore(InputStream requestBody) throws RequestBodyReadException {
        try {
            InputStreamReader isr = new InputStreamReader(requestBody, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);
            String bodyValue = br.readLine().trim();
            logger.log(Level.INFO, LogMessages.BODY_VALUE.getMessage(), bodyValue);
            int score = Integer.parseInt(bodyValue);
            if (score < 0) {
                throw new InvalidIdException();
            }
            return score;
        } catch (Exception exception) {
            throw new RequestBodyReadException(exception.getMessage());
        }
    }

    /*
     * Request: GET /<userid>/login
     * Response: <sessionkey>
     * <userid> : 31 bit unsigned integer number
     * <sessionkey> : A string representing session (valid for 10 minutes).
     * Example: http://localhost:8081/4711/login --> UICSNDK
     */
    private void handleGetRequest(HttpExchange httpExchange) throws InvalidUriException, InvalidIdException {
        URI uri = httpExchange.getRequestURI();
        String[] segments = uri.getPath().split("/");
        validateUriSegments(segments);
        int id = Integer.parseInt(segments[1]);
        String endpoint = segments[2];
        switch (endpoint) {
            case "login":
                sessionTokenService.login(id);
                break;
            case "highscorelist":
                sessionTokenService.highScoreList(id);
                break;
            default:
                throw new InvalidUriException();
        }
        handleResponse(httpExchange, HttpCode.OK);
    }

    private void validateUriSegments(String[] segments) throws InvalidUriException, InvalidIdException {
        if (segments.length != 3) {
            throw new InvalidUriException();
        }
        if (Integer.parseInt(segments[1]) < 0) {
            throw new InvalidIdException();
        }
    }

    private void handleResponse(HttpExchange httpExchange, HttpCode httpCode) {
        OutputStream outputStream = httpExchange.getResponseBody();
        try {
            httpExchange.sendResponseHeaders(httpCode.getCode(), httpCode.getMessage().length());
            httpExchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            outputStream.write(httpCode.getMessage().getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (IOException exception) {
            logger.log(Level.SEVERE, LogMessages.ERROR_CRITICAL.getMessage(), exception.getMessage());
        }
    }
}
