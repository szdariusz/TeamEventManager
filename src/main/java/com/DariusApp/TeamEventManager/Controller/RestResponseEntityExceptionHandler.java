package com.DariusApp.TeamEventManager.Controller;

import com.DariusApp.TeamEventManager.Payload.Response.MessageResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Log4j2
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<?> handleAnyException(
            Exception ex, WebRequest request) {
        String bodyOfResponse = "Something went wrong! Try to refresh or please contact the developers!";
        log.info(request.toString(), request);
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(new MessageResponse(bodyOfResponse + "(" + ex.getMessage() + ")"), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
