package com.example.springboot;

import com.example.springboot.cache.MathOperationInMemoryCache;
import com.example.springboot.interfaces.IMathOperationOperation;
import com.example.springboot.models.math.*;
import com.example.springboot.operations.CounterOperation;
import com.example.springboot.operations.MathOperationResultAggregatorOperation;
import com.example.springboot.repositories.MathOperationRepository;
import com.example.springboot.repositories.OperationRepository;
import com.example.springboot.repositories.ResultRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.springboot.constants.ValidationConstants;
import com.example.springboot.enums.Operation;
import com.example.springboot.models.ErrorModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@RestController
@SpringBootApplication
public class MathController {
    @Autowired
    IMathOperationOperation mathOperationOperation;

    @Autowired
    CounterOperation counterOperation;

    @Autowired
    OperationRepository operationRepository;

    @Autowired
    MathOperationRepository mathOperationRepository;

    @Autowired
    ResultRepository resultRepository;

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

        var operationEntity = operationRepository.Object(model.getOperation().toString());
        var entity = mathOperationRepository.Create(model.getFirst(), model.getSecond(), operationEntity);

        try {

            double result = mathOperationOperation.Compute(entity);
            resultRepository.Create(entity, result);
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
            var operationEntity = operationRepository.Object(mOperation.getOperation().name());
            return mathOperationRepository.Create(mOperation.getFirst(), mOperation.getSecond(), operationEntity);
        }).collect(Collectors.toCollection(ArrayList::new));

        var results = entities.stream().map(entity -> {
           var result = mathOperationOperation.Compute(entity);
           resultRepository.Create(entity, result);
           return result;
        }).collect(Collectors.toCollection(ArrayList::new));

        var stats = MathOperationResultAggregatorOperation.Aggreagate(entities, results);

        var resultsCollection = results.stream().map(MathOperationResultModel::new)
                .collect(Collectors.toCollection(ArrayList::new));

        return new MathOperationCollectionResultModel(resultsCollection, stats);
    }

    @PostMapping(value="/computeAsync", consumes = "application/json", produces = "application/json")
    public MathOperationIdModel computeCollection(@RequestBody MathOperationModel model)
    {
        var operationEntity = operationRepository.Object(model.getOperation().name());
        var entity = mathOperationRepository.Create(model.getFirst(), model.getSecond(), operationEntity);

        mathOperationOperation.ComputeAsync(entity);

        return new MathOperationIdModel(entity.id);
    }

    @GetMapping("/result")
    public MathOperationResultModel result(@ModelAttribute MathOperationIdModel model) {

        var mathOperation = mathOperationRepository.Object(model.getId());
        var result = resultRepository.Object(mathOperation);

        return new MathOperationResultModel(result.result);
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