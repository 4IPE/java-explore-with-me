package ru.practicum.stats.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.stats.exception.model.ErrorModel;

import java.time.DateTimeException;


@RestControllerAdvice
public class ErrorHandler {


    @ExceptionHandler(DateTimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorModel badRequest(DateTimeException ex) {
        return new ErrorModel("400", "Don t correct request start and end time.", ex.getMessage());
    }


}
