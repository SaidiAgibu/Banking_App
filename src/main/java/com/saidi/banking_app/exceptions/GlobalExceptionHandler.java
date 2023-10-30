package com.saidi.banking_app.exceptions;

import com.saidi.banking_app.response.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleAlreadyExistsException(AlreadyExistsException exception) {
        ExceptionResponse response = new ExceptionResponse();
        response.setStatus(HttpStatus.BAD_REQUEST);
        response.setMessage(exception.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFoundException(NotFoundException exception) {
        ExceptionResponse response = new ExceptionResponse();
        response.setStatus(HttpStatus.NOT_FOUND);
        response.setMessage(exception.getMessage());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AmountLessThanZeroException.class)
    public ResponseEntity<ExceptionResponse> handleAmountLessThanZeroException(AmountLessThanZeroException exception) {
        ExceptionResponse response = new ExceptionResponse();
        response.setStatus(HttpStatus.BAD_REQUEST);
        response.setMessage(exception.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IncorrectPinException.class)
    public ResponseEntity<ExceptionResponse> handleIncorrectPinException(IncorrectPinException exception) {
        ExceptionResponse response = new ExceptionResponse();
        response.setStatus(HttpStatus.BAD_REQUEST);
        response.setMessage(exception.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
