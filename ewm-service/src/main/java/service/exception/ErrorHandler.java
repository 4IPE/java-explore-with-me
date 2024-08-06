package service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import service.exception.model.ErrorModel;
import service.exception.model.ImpossibilityOfAction;
import service.exception.model.NotFound;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(ImpossibilityOfAction.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorModel impossibleAct(ImpossibilityOfAction ex) {
        return new ErrorModel("409", "ImpossibilityOfAction", ex.getMessage());
    }

    @ExceptionHandler(NotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorModel notFound(NotFound ex) {
        return new ErrorModel("404", "NotFound", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorModel badRequest(MethodArgumentNotValidException ex) {
        return new ErrorModel("400", "Incorrectly made request.", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorModel exception(Exception ex) {
        return new ErrorModel("500", ex.getCause().toString(), ex.getMessage());
    }

}
