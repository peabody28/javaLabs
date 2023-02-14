package com.example.springboot;

import com.example.springboot.cache.MathOperationInMemoryCache;
import com.example.springboot.entities.MathOperationEntity;
import com.example.springboot.entities.OperationEntity;
import com.example.springboot.interfaces.IMathOperation;
import com.example.springboot.models.math.*;
import com.example.springboot.operations.CounterOperation;
import com.example.springboot.operations.MathOperationResultAggregatorOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.springboot.constants.ValidationConstants;
import com.example.springboot.enums.Operation;
import com.example.springboot.models.ErrorModel;
import org.apache.http.client.HttpResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@SpringBootApplication
public class MathController {
    @Autowired
    IMathOperation mathOperation;

    @Autowired
    CounterOperation counterOperation;

    @Autowired
    MathOperationInMemoryCache<MathOperationModel, MathOperationResultModel> cache;

    Logger logger = LoggerFactory.getLogger(MathController.class);

    Lock lock = new ReentrantLock();

    @GetMapping("/compute")
    public MathOperationResultModel compute(@ModelAttribute MathOperationModel model){
        lock.lock();
        counterOperation.Add();
        lock.unlock();

        var cacheValue = cache.Get(model);
        if(cacheValue != null) return cacheValue;

        if(model.getOperation().equals(Operation.Division) && model.getSecond() == 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ValidationConstants.ArgumentInvalidMessage);

        var operationEntity = new OperationEntity(model.getOperation().toString());
        // TODO: create entity in database
        var entity = new MathOperationEntity(model.getFirst(), model.getSecond(), operationEntity);

        try {

            double result = mathOperation.Compute(entity);
            logger.info(String.format("%f %s %f", model.getFirst(), model.getOperation(), model.getSecond()));

            var response = new MathOperationResultModel(result);
            cache.Push(model, response);
            return new MathOperationResultModel(result);
        }
        catch(Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ValidationConstants.ServerErrorMessage);
        }
    }

    @PostMapping(value="/compute", consumes = "application/json", produces = "application/json")
    public MathOperationCollectionResultModel computeCollection(@RequestBody MathOperationCollectionModel model)
    {
        var entities = model.collection.stream().parallel().map(mOperation ->
        {
            var operationEntity = new OperationEntity(mOperation.getOperation().toString());
            return new MathOperationEntity(mOperation.getFirst(), mOperation.getSecond(), operationEntity);
        }).collect(Collectors.toCollection(ArrayList::new));

        var results = mathOperation.Compute(entities);

        var stats = MathOperationResultAggregatorOperation.Aggreagate(entities, results);

        var resultsCollection = results.stream().map(MathOperationResultModel::new)
                .collect(Collectors.toCollection(ArrayList::new));

        return new MathOperationCollectionResultModel(resultsCollection, stats);
    }

    @GetMapping("/stat")
    public StatisticsModel stat() {
        var model = new StatisticsModel();

        model.count = counterOperation.GetCount();

        return model;
    }

    @ExceptionHandler({ ResponseStatusException.class })
    public ResponseEntity<Object> handleException(ResponseStatusException ex) {

        var errorModel = new ErrorModel();
        errorModel.Message = ex.getReason();

        logger.info("Exception: " + ex.getMessage());

        return new ResponseEntity<>(errorModel, ex.getStatusCode());
    }
}