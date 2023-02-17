package com.example.springboot.entities;

import com.example.springboot.interfaces.entities.IMathOperation;
import com.example.springboot.interfaces.entities.IResult;

public class ResultEntity implements IResult
{
    public int id;
    public IMathOperation mathOperation;
    public double result;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public IMathOperation getMathOperation() {
        return mathOperation;
    }

    @Override
    public double getResult() {
        return result;
    }

    public ResultEntity(int _id, IMathOperation _m, double _res)
    {
        id = _id;
        mathOperation = _m;
        result = _res;
    }

}
