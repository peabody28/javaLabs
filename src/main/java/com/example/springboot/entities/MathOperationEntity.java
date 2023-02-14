package com.example.springboot.entities;

public class MathOperationEntity {
    public int id;
    public double first;
    public double second;
    public OperationEntity operation;

    public MathOperationEntity(int _id, double _first, double _second, OperationEntity _op)
    {
        id = _id;
        first = _first;
        second = _second;
        operation = _op;
    }
}
