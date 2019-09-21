package example.controller;

import example.exception.BalanceException;
import example.exception.DuplicateException;
import example.exception.ScaleException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ControllerAdvice
public class MyExceptionHandler {
    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<String> method(DuplicateException e){
        return new ResponseEntity<>(
                "Field (" + e.getErrorField() + ") is duplicated",
                HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Bad amount")
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public void exception1(){
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Insufficient funds in account")
    @ExceptionHandler(BalanceException.class)
    public void exception2(){
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Scale cannot be greater than 2")
    @ExceptionHandler(ScaleException.class)
    public void exception3(){
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String[]> exception5(MethodArgumentNotValidException e){
        String[] s = new String[e.getBindingResult().getFieldErrorCount()];
        List<FieldError> errors = e.getBindingResult().getFieldErrors();
        int i = 0;
        for(FieldError error:errors){
            if(error.getCode().equals("NotEmpty")){
                s[i++] = "Field (" + error.getField() + ") should not be empty";
            }
            if(error.getCode().equals("NotNull")){
                s[i++] = "Field (" + error.getField() + ") should not be null";
            }
            if(error.getCode().equals("Positive")){
                s[i++] = "Field (" + error.getField() + ") should be positive";
            }
        }
        return new ResponseEntity<>(s, HttpStatus.BAD_REQUEST);
    }
}
