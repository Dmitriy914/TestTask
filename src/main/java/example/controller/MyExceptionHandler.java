package example.controller;

import example.exception.AmountException;
import example.exception.BalanceException;
import example.exception.DuplicateException;
import example.exception.ScaleException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class MyExceptionHandler {
    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<String> Method(DuplicateException e){
        return new ResponseEntity<>(
                "Field (" + e.getErrorField() + ") is duplicated",
                HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Bad amount")
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public void Exception1(){
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Insufficient funds in account")
    @ExceptionHandler(BalanceException.class)
    public void Exception2(){
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Scale cannot be greater than 2")
    @ExceptionHandler(ScaleException.class)
    public void Exception3(){
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Amount must be greater than 0")
    @ExceptionHandler(AmountException.class)
    public void Exception4(){
    }
}
