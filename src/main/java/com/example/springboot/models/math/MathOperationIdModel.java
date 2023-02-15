package com.example.springboot.models.math;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MathOperationIdModel {
    @JsonProperty("id")
    int id;

    public MathOperationIdModel(int id)
    {
        this.id = id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return id;
    }
}
