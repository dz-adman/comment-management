package com.adman.craft.comments_mgmt.errorhandling;

public enum ErrorCode {
    INTERNAL_SERVER_ERROR("Internal Server Error"),
    UNKNOWN_ERROR("Unknown Error"),
    BAD_REQUEST("Validation failed! Bad Request"),
    USER_ALREADY_EXISTS("User Already Exists"),
    INVALID_CREDENTIALS("Invalid Credentials");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
