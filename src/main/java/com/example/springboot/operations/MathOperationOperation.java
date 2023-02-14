package com.example.springboot.operations;

import com.example.springboot.entities.MathOperationEntity;
import com.example.springboot.interfaces.IMathOperationOperation;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Component(value="mathOperationOperation")
public class MathOperationOperation implements IMathOperationOperation {
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
}
