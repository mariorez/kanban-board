package org.seariver.kanbanboard.write.adapter.in;

import org.seariver.kanbanboard.write.domain.exception.DomainException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
public class WriteExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> onMethodArgumentNotValidException(MethodArgumentNotValidException exception) {

        Map<String, Object> detailedErrors = exception
            .getBindingResult()
            .getFieldErrors()
            .stream()
            .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

        return getResponseEntity("Invalid field", detailedErrors, BAD_REQUEST);
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<Object> onDomainException(DomainException exception) {

        return getResponseEntity(exception.getMessage(), exception.getErrors(), BAD_REQUEST);
    }

    private ResponseEntity<Object> getResponseEntity(String message, Map<String, Object> detailedErrors, HttpStatus status) {

        Map<String, Object> errorResult = new HashMap<>(Map.of("message", message));

        if (detailedErrors != null && !detailedErrors.isEmpty()) {
            errorResult.put("errors", detailedErrors);
        }

        if (logger.isWarnEnabled()) {
            logger.warn(errorResult.toString());
        }

        return new ResponseEntity<>(errorResult, status);
    }
}