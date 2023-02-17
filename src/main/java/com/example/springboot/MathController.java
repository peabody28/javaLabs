package com.example.springboot;

import com.example.springboot.cache.MathOperationInMemoryCache;
import com.example.springboot.interfaces.operations.IMathOperationOperation;
import com.example.springboot.interfaces.repositories.IMathOperationRepository;
import com.example.springboot.interfaces.repositories.IOperationRepository;
import com.example.springboot.interfaces.repositories.IResultRepository;
import com.example.springboot.models.math.*;
import com.example.springboot.operations.CounterOperation;
import com.example.springboot.operations.MathOperationResultAggregatorOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.springboot.constants.ValidationConstants;
import com.example.springboot.enums.Operation;
import com.example.springboot.models.ErrorModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@RestController
@SpringBootApplication
public class MathController {

    //region Operations
    @Autowired
    public IMathOperationOperation mathOperationOperation;

    @Autowired
    public CounterOperation counterOperation;

    //endregion

    //region Repositories
    @Autowired
    public IOperationRepository operationRepository;

    @Autowired
    public IMathOperationRepository mathOperationRepository;

    @Autowired
    public IResultRepository resultRepository;

    //endregion

    @Autowired
    public MathOperationInMemoryCache<MathOperationModel, MathOperationResultModel> cache;

    private final Logger logger = LoggerFactory.getLogger(MathController.class);

    private final Lock lock = new ReentrantLock();

    @GetMapping("/compute")
    public MathOperationResultModel compute(@ModelAttribute MathOperationModel model)
    {
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
            return response;
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

        var results = entities.stream().map(entity ->
        {
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
    public MathOperationIdModel computeAsync(@RequestBody MathOperationModel model)
    {
        var operationEntity = operationRepository.Object(model.getOperation().name());
        var entity = mathOperationRepository.Create(model.getFirst(), model.getSecond(), operationEntity);

        mathOperationOperation.ComputeAsync(entity);

        return new MathOperationIdModel(entity.getId());
    }

    @GetMapping("/result")
    public MathOperationResultModel result(@ModelAttribute MathOperationIdModel model)
    {
        var mathOperation = mathOperationRepository.Object(model.getId());

        var result = resultRepository.Object(mathOperation);

        return new MathOperationResultModel(result.getResult());
    }

    @GetMapping("/stat")
    public StatisticsModel stat()
    {
        var model = new StatisticsModel();

        model.count = counterOperation.GetCount();

        return model;
    }

    @ExceptionHandler({ ResponseStatusException.class })
    public ResponseEntity<Object> handleException(ResponseStatusException ex)
    {
        var errorModel = new ErrorModel();
        errorModel.Message = ex.getReason();

        logger.info("Exception: " + ex.getMessage());

        return new ResponseEntity<>(errorModel, ex.getStatusCode());
    }
}