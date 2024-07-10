package org.example.geodata.controller;

import com.mongodb.MongoWriteException;
import org.example.geodata.exception.BaseException;
import org.example.geodata.message.ErrorObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorObject> handle(BaseException exception) {
        return createResponse(exception);
    }

    @ExceptionHandler(MongoWriteException.class)
    public ResponseEntity<ErrorObject> handle(MongoWriteException exception) {
        return createResponse(exception);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorObject> handle(Throwable exception) {
        return createResponse(exception);
    }

    private static ResponseEntity<ErrorObject> createResponse(Throwable exception) {
        return new ResponseEntity<>(new ErrorObject(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

}
