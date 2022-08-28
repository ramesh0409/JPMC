package com.jpmc.theater.exception;

public class NoDataFoundException extends RuntimeException{

    public NoDataFoundException(String message){
        super(message);
    }

    public NoDataFoundException(String message,Throwable error){
        super(message,error);
    }

    public static void throwException(String entity,Object key){
        String errorMessage=String.format("No %s found for the given input %s",entity,key);
        throw new NoDataFoundException(errorMessage);
    }
}
