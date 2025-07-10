package com.gateway.exception;

public class TokenExpiredException extends RuntimeException {

    public TokenExpiredException(String message){
        super(message);
    }
}
