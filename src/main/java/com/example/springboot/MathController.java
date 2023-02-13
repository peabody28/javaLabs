package com.example.springboot;

import com.example.springboot.cache.MathOperationInMemoryCache;
import com.example.springboot.interfaces.IMathOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.springboot.constants.ValidationConstants;
import com.example.springboot.enums.Operation;
import com.example.springboot.models.ErrorModel;
import com.example.springboot.models.math.MathOperationModel;
import com.example.springboot.models.math.MathOperationResultModel;
import org.apache.http.client.HttpResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@SpringBootApplication
public class MathController {
    @Autowired
    IMathOperation mathOperation;

    @Autowired
    MathOperationInMemoryCache<MathOperationModel, MathOperationResultModel> cache;

    Logger logger = LoggerFactory.getLogger(MathController.class);

    @GetMapping("/compute")
    public MathOperationResultModel hello(@ModelAttribute MathOperationModel model) throws HttpResponseException {

        var cacheValue = cache.Get(model);
        if(cacheValue != null) return cacheValue;

        if(model.getOperation().equals(Operation.Division) && model.getSecond() == 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ValidationConstants.ArgumentInvalidMessage);

        try {
            double result = mathOperation.Compute(model.getFirst(), model.getSecond(), model.getOperation());
            logger.info(String.format("%f %s %f", model.getFirst(), model.getOperation(), model.getSecond()));

            var response = new MathOperationResultModel(result);
            cache.Push(model, response);
            return new MathOperationResultModel(result);
        }
        catch(Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ValidationConstants.ServerErrorMessage);
        }
    }

    @ExceptionHandler({ ResponseStatusException.class })
    public ResponseEntity<Object> handleException(ResponseStatusException ex) {

        var errorModel = new ErrorModel();
        errorModel.Message = ex.getReason();

        logger.info("Exception: " + ex.getMessage());

        return new ResponseEntity<>(errorModel, ex.getStatusCode());
    }
}