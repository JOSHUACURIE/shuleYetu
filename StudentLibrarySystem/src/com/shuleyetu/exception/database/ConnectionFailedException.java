package com.shuleyetu.exception.database;

import com.shuleyetu.exception.LibraryException;

public class ConnectionFailedException extends LibraryException {
    
    public ConnectionFailedException(String message) {
        super(message);
    }
    
    public ConnectionFailedException(String message, String errorCode) {
        super(message, errorCode);
    }
    
    public ConnectionFailedException(String message, String errorCode, String userMessage) {
        super(message, errorCode, userMessage);
    }
}