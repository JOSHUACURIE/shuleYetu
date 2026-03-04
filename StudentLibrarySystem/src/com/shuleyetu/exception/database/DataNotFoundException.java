package com.shuleyetu.exception.database;
import com.shuleyetu.exception.LibraryException;


public class DataNotFoundException extends LibraryException{
    
public DataNotFoundException(String message){
    super(message);
}

public DataNotFoundException(String message,String errorCode){
    super(message,errorCode);
}
public DataNotFoundException(String message,String errorCode,String userMessage){
    super(message,errorCode,userMessage);
}


}