package com.shuleyetu.exception.database;

import com.shuleyetu.exception.LibraryException;

public class ConstraintViolationException extends LibraryException{

public ConstraintViolationException(String message){
    super(message);
}

public ConstraintViolationException(String message,String errorCode){
    super(message,errorCode);
}

public ConstraintViolationException(String message,String errorCode,String userMessage){
    super(message,errorCode,userMessage);
}





}