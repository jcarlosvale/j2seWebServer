package com.test.king.httphandler;

import com.sun.net.httpserver.HttpExchange;
import com.test.king.constants.HttpCode;
import com.test.king.constants.LogMessages;
import com.test.king.exceptions.*;
import com.test.king.service.SessionKeyService;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Http Handler used by the Web Server and responsible by process the GET and POST requests, applying validations and
 * calling the SessionKeyService to process. It is like a Controller Class.
 * It is a Singleton Class.
 */
public final class RankingHttpHandler {

    private static RankingHttpHandler rankingHttpHandler;
    private final Logger logger = Logger.getLogger(RankingHttpHandler.class.getName());
    private final SessionKeyService sessionKeyService;


    private RankingHttpHandler(final SessionKeyService sessionKeyService) {
        this.sessionKeyService = sessionKeyService;
    }

    public static RankingHttpHandler getInstance(final SessionKeyService sessionKeyService) {
        if (Objects.isNull(rankingHttpHandler)) {
            synchronized (RankingHttpHandler.class) {
                rankingHttpHandler = new RankingHttpHandler(sessionKeyService);
            }
        }
        return rankingHttpHandler;
    }

    /**
     * Main method to process the Http requests and generate the responses to WebServer.
     * Process only GET and POST methods requested and returns the expected OK Response.
     * In case of validation error or exception, an UNAUTHORIZED or BAD REQUEST response is generated.
     * @param httpExchange  Http Request/Response received.
     */
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
                    handleResponse(httpExchange, HttpCode.METHOD_NOT_ALLOWED, HttpCode.METHOD_NOT_ALLOWED.getMessage());
            }
        } catch (Exception exception) {
            logException(exception, httpExchange);
            if (exception instanceof InvalidSessionKeyException) {
                handleResponse(httpExchange, HttpCode.UNAUTHORIZED, HttpCode.UNAUTHORIZED.getMessage());
            } else {
                handleResponse(httpExchange, HttpCode.BAD_REQUEST, HttpCode.BAD_REQUEST.getMessage());
            }
        }
    }

    /*
     * Request: GET /<userid>/login
     * Response: <sessionkey>
     * <userid> : 31 bit unsigned integer number
     * <sessionkey> : A string representing session (valid for 10 minutes).
     * Example: http://localhost:8081/4711/login --> UICSNDK
     *
     * Request: GET /<levelid>/highscorelist
     * Response: CSV of <userid>=<score>
     * <levelid> : 31 bit unsigned integer number
     * <score> : 31 bit unsigned integer number
     * <userid> : 31 bit unsigned integer number
     * Example: http://localhost:8081/2/highscorelist -> 4711=1500,131=1220
     */

    /**
     * Method responsible to process GET requests: Login and High Score List endpoints.
     * Flow:
     * - Validates the URI format / segments expected via GET
     * - Identify if the Session Key Service will process the Login or High Score List method.
     * - Retrieves the HTTP response 200.
     * @param httpExchange  Http Request/Response received.
     * @throws InvalidUriException  invalid URI exception
     * @throws InvalidIdException   invalid ID exception
     */
    private void handleGetRequest(HttpExchange httpExchange) throws InvalidUriException, InvalidIdException {
        URI uri = httpExchange.getRequestURI();
        String[] segments = uri.getPath().split("/");
        validateUriSegments(segments);
        int id = Integer.parseInt(segments[1]);
        String endpoint = segments[2];
        String responseBody;
        switch (endpoint) {
            case "login":
                responseBody = sessionKeyService.login(id);
                break;
            case "highscorelist":
                responseBody = sessionKeyService.highScoreList(id);
                break;
            default:
                throw new InvalidUriException();
        }
        handleResponse(httpExchange, HttpCode.OK, responseBody);
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

    /**
     * Method responsible to process POST request: Save User Score in a Level.
     * Flow:
     * - Validates the URI format / segments expected via POST
     * - Validates the Body Request
     * - Call Session Key Service to process the score.
     * - Retrieves the HTTP response 201.
     * @param httpExchange  Http Request/Response received.
     * @throws InvalidUriException  invalid URI exception
     * @throws InvalidIdException   invalid ID exception
     * @throws RequestSessionKeyReadException   invalid Session Key read exception
     * @throws RequestBodyReadException invalid Body Request value exception
     * @throws InvalidSessionKeyException   invalid Session Key expired or nonexistent exception
     */
    private void handlePostRequest(HttpExchange httpExchange)
            throws InvalidUriException, InvalidIdException, RequestSessionKeyReadException, RequestBodyReadException,
            InvalidSessionKeyException {
        URI uri = httpExchange.getRequestURI();
        String[] segments = uri.getPath().split("/");
        validateUriSegments(segments);
        if (! segments[2].contains("score")) {
            throw new InvalidUriException();
        }
        String sessionKey = getSessionKey(uri.getQuery());
        int levelId = Integer.parseInt(segments[1]);
        int score = getScore(httpExchange.getRequestBody());
        sessionKeyService.saveUserScoreLevel(sessionKey, levelId, score);
        handleResponse(httpExchange, HttpCode.CREATED, HttpCode.CREATED.getMessage());
    }

    /**
     * Handles the Http Response providing the Body Response content and Http Return Code.
     * @param httpExchange  Http Request/Response received.
     * @param httpCode      Http code to be used in the Response.
     * @param responseBodyContent  Response Body content string.
     */
    private void handleResponse(HttpExchange httpExchange, HttpCode httpCode, String responseBodyContent) {
        OutputStream outputStream = httpExchange.getResponseBody();
        try {
            httpExchange.sendResponseHeaders(httpCode.getCode(), responseBodyContent.length());
            httpExchange.getResponseHeaders().set("Content-Type", "text/plain; charset=UTF-8");
            outputStream.write(responseBodyContent.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (IOException exception) {
            logger.log(Level.SEVERE, LogMessages.ERROR_CRITICAL.getMessage(), exception.getMessage());
        }
    }

    /**
     * Validates the URI segments:
     * - Length;
     * - Unsigned integer value
     * @param segments              segments to be validated
     * @throws InvalidUriException  invalid URI exception in case of number of segments
     * @throws InvalidIdException   invalid ID format exception
     */
    private void validateUriSegments(String[] segments) throws InvalidUriException, InvalidIdException {
        if (segments.length != 3) {
            throw new InvalidUriException();
        }
        if (Integer.parseInt(segments[1]) < 0) {
            throw new InvalidIdException();
        }
    }

    /**
     * Extract the session key from URI
     * @param urlQuery  URI query
     * @return  session key
     * @throws RequestSessionKeyReadException   thrown if the Session Key is not present.
     */
    private String getSessionKey(String urlQuery) throws RequestSessionKeyReadException {
        try {
            String sessionKey = urlQuery.split("sessionkey=")[1];
            logger.log(Level.INFO, LogMessages.SESSION_KEY.getMessage(), sessionKey);
            return sessionKey;
        } catch (Exception exception) {
            throw new RequestSessionKeyReadException(exception.getMessage());
        }
    }

    /**
     * Reads and validates the score from Body Request
     * @param requestBody   request body
     * @return  the validated score
     * @throws RequestBodyReadException thrown if the Score is not an unsigned integer.
     */
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

    /**
     * Util method to log the exception associating the message and message parameter.
     * @param exception Exception to be logged.
     * @param httpExchange  Http Request/Response containing the information to be logged.
     */
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
            return;
        }
        if (exception instanceof InvalidSessionKeyException) {
            logger.log(Level.SEVERE, LogMessages.INVALID_SESSION_KEY.getMessage(), httpExchange.getRequestURI().getQuery());
        }
    }
}
