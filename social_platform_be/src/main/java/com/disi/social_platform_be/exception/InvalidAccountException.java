package com.disi.social_platform_be.exception;

public class InvalidAccountException extends RuntimeException{

    public InvalidAccountException(String message) {
        super(message);
    }
}
