package com.example.springboot.operations;

import com.example.springboot.enums.Operation;
import com.example.springboot.interfaces.IMathOperation;
import org.springframework.stereotype.Component;

@Component(value="mathOperation")
public class MathOperation implements IMathOperation {
    public double Compute(double first, double second, Operation op)
    {
        var result = first;
        switch (op) {
            case Addition -> result += second;
            case Subtraction -> result -= second;
            case Multiplication -> result *= second;
            case Division -> result /= second;
        };

        return result;
    }
}
