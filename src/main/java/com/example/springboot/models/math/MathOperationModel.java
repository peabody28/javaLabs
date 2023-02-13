package com.example.springboot.models.math;

import com.example.springboot.enums.Operation;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MathOperationModel {

    @JsonProperty("first")
    double first;

    @JsonProperty("second")

    double second;

    @JsonProperty("operation")

    Operation operation;


    public double getFirst() {
        return first;
    }

    public double getSecond() {
        return second;
    }

    public Operation getOperation()
    {
        return operation;
    }

    public void setFirst(double val) {
        first = val;
    }

    public void setSecond(double val) {
       second = val;
    }

    public void setOperation(Operation val)
    {
        operation = val;
    }
}