package com.shuleyetu.exception.database;

import com.shuleyetu.exception.LibraryException;

public class DatabaseException extends LibraryException{


public DatabaseException(String message){
    super(message);
}

public DatabaseException(String message,String errorCode){
    super(message,errorCode);
}

public DatabaseException(String message,String errorCode,String userMessage){
    super(message,errorCode,userMessage);
}
}