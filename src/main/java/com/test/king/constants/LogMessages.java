package com.test.king.constants;

public enum LogMessages {
    STARTING_SERVER("Starting server"),
    SERVER_STARTED("Server started using port {0}"),
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
    EXCEPTION_MESSAGE("Exception message: {0}"),
    //TODO: REMOVE IT
    MOCK_HANDLE("Mock handle test");

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
