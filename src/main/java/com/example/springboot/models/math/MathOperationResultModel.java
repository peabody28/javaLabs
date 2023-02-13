package com.example.springboot.models.math;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MathOperationResultModel {
    @JsonProperty("value")
    public double value;

    public MathOperationResultModel(double _val)
    {
        value = _val;
    }
}
