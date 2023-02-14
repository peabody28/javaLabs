package com.example.springboot.operations;

import com.example.springboot.entities.MathOperationEntity;
import com.example.springboot.enums.Operation;
import com.example.springboot.interfaces.IMathOperation;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Component(value="mathOperation")
public class MathOperation implements IMathOperation {
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
