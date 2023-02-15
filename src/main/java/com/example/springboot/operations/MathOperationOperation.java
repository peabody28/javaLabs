package com.example.springboot.operations;

import com.example.springboot.MathController;
import com.example.springboot.entities.MathOperationEntity;
import com.example.springboot.interfaces.IMathOperationOperation;
import com.example.springboot.repositories.ResultRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Component(value="mathOperationOperation")
public class MathOperationOperation implements IMathOperationOperation {

    @Autowired
    ResultRepository resultRepository;

    Logger logger = LoggerFactory.getLogger(MathController.class);

    public double Compute(MathOperationEntity entity)
    {
        var result = entity.first;
        // TODO: remove hardcode
        switch (entity.operation.name) {
            case "Addition" -> result += entity.second;
            case "Subtraction" -> result -= entity.second;
            case "Multiplication" -> result *= entity.second;
            case "Division" -> result /= entity.second;
        };

        return result;
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
