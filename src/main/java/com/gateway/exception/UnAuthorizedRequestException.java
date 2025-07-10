package com.gateway.exception;

public class UnAuthorizedRequestException extends RuntimeException{

    public UnAuthorizedRequestException(String message){
        super(message);
    }
}
