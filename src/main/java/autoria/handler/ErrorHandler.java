package autoria.handler;

import autoria.dto.ErrorResponse;
import autoria.exception.CustomException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;
import java.util.List;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<ErrorResponse> handleValidationException(ConstraintViolationException exception){

        List<String> messages = exception
                .getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .toList();

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date(System.currentTimeMillis()));
        errorResponse.setMessages(messages);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler({SQLIntegrityConstraintViolationException.class})
    public ResponseEntity<ErrorResponse> handleDatabaseException(SQLIntegrityConstraintViolationException exception){

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date(System.currentTimeMillis()));
        errorResponse.setMessages(List.of(exception.getMessage()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> handleMethodValidationException(MethodArgumentNotValidException exception){


        List<String> messages = exception
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date(System.currentTimeMillis()));
        errorResponse.setMessages(messages);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

    }

    @ExceptionHandler({CustomException.class})
    public ResponseEntity<ErrorResponse> handleException(CustomException exception){

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date(System.currentTimeMillis()));
        errorResponse.setMessages(List.of(exception.getMessage()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

    }


}
