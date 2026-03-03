package com.shuleyetu.exception;

public class LibraryException extends Exception {
    
    private String errorCode;
    private String userMessage;
    
    public LibraryException(String message) {
        super(message);
        this.errorCode = "UNKNOWN";
        this.userMessage = message;
    }
    
    public LibraryException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "UNKNOWN";
        this.userMessage = message;
    }
    
    public LibraryException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.userMessage = message;
    }
    
    public LibraryException(String message, String errorCode, String userMessage) {
        super(message);
        this.errorCode = errorCode;
        this.userMessage = userMessage;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public String getUserMessage() {
        return userMessage;
    }
}