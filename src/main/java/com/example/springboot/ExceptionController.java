package com.example.springboot;

import com.example.springboot.models.ErrorModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ExceptionController {

    private final Logger logger = LoggerFactory.getLogger(MathController.class);

    @ExceptionHandler({ ResponseStatusException.class })
    public ResponseEntity<Object> handleException(ResponseStatusException ex)
    {
        var errorModel = new ErrorModel();
        errorModel.Message = ex.getReason();

        logger.error("Exception: " + ex.getMessage());

        return new ResponseEntity<>(errorModel, ex.getStatusCode());
    }

    @ExceptionHandler({ RuntimeException.class })
    public ResponseEntity<Object> handleExceptionSecond(RuntimeException ex)
    {
        var errorModel = new ErrorModel();
        errorModel.Message = ex.getMessage();

        logger.error("Exception: " + ex.getMessage());

        return new ResponseEntity<>(errorModel, HttpStatusCode.valueOf(500));
    }
}
