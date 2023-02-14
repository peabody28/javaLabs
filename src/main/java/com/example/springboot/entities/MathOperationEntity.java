package com.example.springboot.entities;

public class MathOperationEntity {
    public double first;
    public double second;
    public OperationEntity operation;

    public MathOperationEntity(double _first, double _second, OperationEntity _op)
    {
        first = _first;
        second = _second;
        operation = _op;
    }
}
