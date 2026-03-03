package com.shuleyetu.exception.database;

import com.shuleyetu.exception.LibraryException;

public class QueryExecutionException extends LibraryException {
    
    public QueryExecutionException(String message) {
        super(message);
    }
    
    public QueryExecutionException(String message, String errorCode) {
        super(message, errorCode);
    }
    
    public QueryExecutionException(String message, String errorCode, String userMessage) {
        super(message, errorCode, userMessage);
    }
}