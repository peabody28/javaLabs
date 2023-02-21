package com.example.springboot;

import com.example.springboot.cache.MathOperationInMemoryCache;
import com.example.springboot.interfaces.operations.IMathOperationOperation;
import com.example.springboot.interfaces.repositories.MathOperationRepository;
import com.example.springboot.interfaces.repositories.OperationRepository;
import com.example.springboot.interfaces.repositories.ResultRepository;
import com.example.springboot.models.math.*;
import com.example.springboot.operations.CounterOperation;
import com.example.springboot.operations.MathOperationResultAggregatorOperation;
import com.example.springboot.repositories.MathOperationRepositoryImpl;
import com.example.springboot.repositories.ResultRepositoryImpl;
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

    private OperationRepository operationRepository;

    @Autowired
    public MathOperationRepository mathOperationRepository;

    @Autowired

    public ResultRepository resultRepository;

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

        var operationEntity = operationRepository.getByName(model.getOperation().toString());
        var entity = mathOperationRepository.Create(model.getFirst(), model.getSecond(), operationEntity);

        double result = mathOperationOperation.Compute(entity);
        resultRepository.Create(entity, result);

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
            var operationEntity = operationRepository.getByName(mOperation.getOperation().name());
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
        var operationEntity = operationRepository.getByName(model.getOperation().name());
        var entity = mathOperationRepository.Create(model.getFirst(), model.getSecond(), operationEntity);

        mathOperationOperation.ComputeAsync(entity);

        return new MathOperationIdModel(entity.getId());
    }

    @GetMapping("/result")
    public MathOperationResultModel result(@ModelAttribute MathOperationIdModel model)
    {
        var mathOperation = mathOperationRepository.findById(model.getId()).get();

        var result = resultRepository.getByMathOperation(mathOperation);

        return new MathOperationResultModel(result.getResult());
    }

    @GetMapping("/stat")
    public StatisticsModel stat() {
        var model = new StatisticsModel();

        model.count = counterOperation.GetCount();

        return model;
    }
}