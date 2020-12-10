package com.test.king.constants;

/**
 * Log messages constants.
 */
public enum LogMessages {
    STARTING_SERVER("Starting server"),
    SERVER_STARTED("Server started using port {0} thread pool size {1}"),
    STOP_SERVER_ORIENTATION("Use Control-C to stop this server"),
    STOPPING_SERVER("Stopping the server soon..."),
    SERVER_STOPPED("Stop successfully."),
    LOGIN_ENDPOINT_START("Executing the Login Endpoint..."),
    METHOD("Method: {0}"),
    URI("URI: {0}"),
    BODY_VALUE("Body Value: {0}"),
    SESSION_KEY("Session Key: {0}"),
    ERROR_CRITICAL("Critical Error: {0}"),
    INVALID_ID("Invalid id format in the path: {0}"),
    INVALID_URI("Invalid URI: {0}"),
    INVALID_SESSION_KEY("Invalid Session Key in the query: {0}"),
    INVALID_BODY_REQUEST("Invalid body request"),
    LOGIN_SERVICE_START("Generating Session Key to user: {0}"),
    LOGIN_SERVICE_END("Generated the SessionKeyDto: {0}"),
    EXCEPTION_MESSAGE("Exception message: {0}");

    private final String message;

    LogMessages(String message) {
        this.message = message;
    }

    private String getCurrentThreadName() {
        return Thread.currentThread().getName();
    }

    public String getMessage() {
        return String.format("[%s] - %s", getCurrentThreadName(), message);
    }
}
