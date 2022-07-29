package com.example.txtlserver.exception;


import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException{


    private static final long serialVersionUID = 1L;
    private HttpStatus status;
    public CustomException(HttpStatus status,String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}