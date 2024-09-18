package dev.peter.Analysis.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ResourceNotFoundException extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = HttpClientErrorException.NotFound.class)
    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "This is page not found";
        return handleExceptionInternal
                (ex, bodyOfResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }


}
