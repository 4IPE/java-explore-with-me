package service.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import service.exception.model.ErrorModel;
import service.exception.model.ImpossibilityOfActionException;
import service.exception.model.NotFoundException;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(ImpossibilityOfActionException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorModel impossibleAct(ImpossibilityOfActionException ex) {
        return new ErrorModel("409", "ImpossibilityOfAction", ex.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorModel uniqueFail(DataIntegrityViolationException ex) {
        return new ErrorModel("409", "Unique Fail", ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorModel uniqueFail(HttpMessageNotReadableException ex) {
        return new ErrorModel("409", "Message error", ex.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorModel notFound(NotFoundException ex) {
        return new ErrorModel("404", "NotFound", ex.getMessage());
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorModel badRequest(HandlerMethodValidationException ex) {
        return new ErrorModel("400", "Incorrectly made request.", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorModel badRequest(MethodArgumentNotValidException ex) {
        return new ErrorModel("400", "Incorrectly made request.", ex.getMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorModel badRequestRequestParam(MissingServletRequestParameterException ex) {
        return new ErrorModel("400", "Incorrectly made request.", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorModel exception(Exception ex) {
        return new ErrorModel("500", ex.getClass().toString(), ex.getMessage());
    }

}
