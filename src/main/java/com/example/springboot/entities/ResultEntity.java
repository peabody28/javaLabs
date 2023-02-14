package com.example.springboot.entities;

public class ResultEntity {
    public int id;

    public MathOperationEntity mathOperation;

    public double result;

    public ResultEntity(int _id, MathOperationEntity _m, double _res)
    {
        id = _id;
        mathOperation = _m;
        result = _res;
    }
}
