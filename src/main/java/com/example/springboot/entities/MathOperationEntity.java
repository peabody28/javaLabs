package com.example.springboot.entities;

import com.example.springboot.interfaces.entities.IMathOperation;
import com.example.springboot.interfaces.entities.IOperation;

public class MathOperationEntity implements IMathOperation
{
    public int id;
    public double first;
    public double second;

    public IOperation operation;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public double getFirst() {
        return first;
    }

    @Override
    public double getSecond() {
        return second;
    }

    @Override
    public IOperation getOperation() {
        return operation;
    }

    public MathOperationEntity(int _id, double _first, double _second, IOperation _op)
    {
        id = _id;
        first = _first;
        second = _second;
        operation = _op;
    }
}
