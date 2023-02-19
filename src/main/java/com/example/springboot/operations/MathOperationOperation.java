package com.example.springboot.operations;

import com.example.springboot.MathController;
import com.example.springboot.entities.MathOperationEntity;
import com.example.springboot.interfaces.operations.IMathOperationOperation;
import com.example.springboot.interfaces.operations.IOperationOperation;
import com.example.springboot.interfaces.repositories.ResultRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Component(value="mathOperationOperation")
public class MathOperationOperation implements IMathOperationOperation
{
    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    public IOperationOperation operationOperation;

    Logger logger = LoggerFactory.getLogger(MathController.class);

    public double Compute(MathOperationEntity entity)
    {
        var first = entity.getFirst();
        var second = entity.getSecond();
        var operation = entity.getOperation();

        if(second == 0. && operation.getName().equals(operationOperation.Division().getName()))
            throw new RuntimeException("Invalid argument");

        if(operation.getName().equals(operationOperation.Addition().getName()))
            return first + second;
        else if(operation.getName().equals(operationOperation.Subtraction().getName()))
            return first - second;
        else if(operation.getName().equals(operationOperation.Multiplication().getName()))
            return first * second;
        else if(operation.getName().equals(operationOperation.Division().getName()))
            return first / second;

        throw new RuntimeException();
    }

    public Collection<Double> Compute(Collection<MathOperationEntity> entities)
    {
        return entities.stream().map(this::Compute).collect(Collectors.toCollection(ArrayList::new));
    }

    public CompletableFuture<Double> ComputeAsync(MathOperationEntity entity)
    {
        return CompletableFuture.supplyAsync(() -> {
            try {
                var result = Compute(entity);
                resultRepository.Create(entity, result);
                Thread.sleep(2000);
                logger.info("task completed");
                return result;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
