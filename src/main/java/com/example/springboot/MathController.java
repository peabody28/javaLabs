package com.example.springboot;

import com.example.springboot.cache.MathOperationInMemoryCache;
import com.example.springboot.interfaces.operations.IMathOperationOperation;
import com.example.springboot.interfaces.repositories.IMathOperationRepository;
import com.example.springboot.interfaces.repositories.IOperationRepository;
import com.example.springboot.interfaces.repositories.IResultRepository;
import com.example.springboot.interfaces.repositories.impls.IMathOperationRepositoryImpl;
import com.example.springboot.interfaces.repositories.impls.IOperationRepositoryImpl;
import com.example.springboot.interfaces.repositories.impls.IResultRepositoryImpl;
import com.example.springboot.models.math.*;
import com.example.springboot.operations.CounterOperation;
import com.example.springboot.operations.MathOperationResultAggregatorOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@RestController
public class MathController {

    //region Operations
    @Autowired
    public IMathOperationOperation mathOperationOperation;

    @Autowired
    public CounterOperation counterOperation;

    //endregion

    //region Repositories

    @Autowired

    private IOperationRepositoryImpl operationRepositoryImpl;

    @Autowired
    public IMathOperationRepositoryImpl mathOperationRepositoryImpl;

    @Autowired

    public IResultRepositoryImpl resultRepositoryImpl;

    //endregion

    @Autowired
    public MathOperationInMemoryCache<MathOperationModel, MathOperationResultModel> cache;

    private final Logger logger = LoggerFactory.getLogger(MathController.class);

    private final Lock lock = new ReentrantLock();

    @GetMapping("/compute")
    public MathOperationResultModel compute(@ModelAttribute MathOperationModel model)
    {
        //lock.lock();
        counterOperation.Add();
        //lock.unlock();
        var cacheValue = cache.Get(model);
        if(cacheValue != null) return cacheValue;

        var operationEntity = operationRepositoryImpl.get(model.getOperation().toString());
        var entity = mathOperationRepositoryImpl.create(model.getFirst(), model.getSecond(), operationEntity);

        double result = mathOperationOperation.Compute(entity);
        resultRepositoryImpl.create(entity, result);

        logger.info(String.format("%f %s %f", model.getFirst(), model.getOperation(), model.getSecond()));

        var response = new MathOperationResultModel(result);
        cache.Push(model, response);
        return response;
    }

    @PostMapping(value="/compute", consumes = "application/json", produces = "application/json")
    public MathOperationCollectionResultModel computeCollection(@RequestBody MathOperationCollectionModel model)
    {
        var entities = model.collection.stream().parallel().map(mOperation ->
        {
            var operationEntity = operationRepositoryImpl.get(mOperation.getOperation().name());
            return mathOperationRepositoryImpl.create(mOperation.getFirst(), mOperation.getSecond(), operationEntity);

        }).collect(Collectors.toCollection(ArrayList::new));

        var results = entities.stream().map(entity ->
        {
           var result = mathOperationOperation.Compute(entity);
           resultRepositoryImpl.create(entity, result);
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
        var operationEntity = operationRepositoryImpl.get(model.getOperation().name());
        var entity = mathOperationRepositoryImpl.create(model.getFirst(), model.getSecond(), operationEntity);

        mathOperationOperation.ComputeAsync(entity);

        return new MathOperationIdModel(entity.getId());
    }

    @GetMapping("/result")
    public MathOperationResultModel result(@ModelAttribute MathOperationIdModel model)
    {
        var mathOperation = mathOperationRepositoryImpl.get(model.getId());

        var result = resultRepositoryImpl.get(mathOperation);

        return new MathOperationResultModel(result.getResult());
    }

    @GetMapping("/stat")
    public StatisticsModel stat() {
        var model = new StatisticsModel();

        model.count = counterOperation.GetCount();

        return model;
    }
}