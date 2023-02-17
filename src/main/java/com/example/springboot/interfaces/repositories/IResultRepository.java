package com.example.springboot.interfaces.repositories;

import com.example.springboot.interfaces.entities.IMathOperation;
import com.example.springboot.interfaces.entities.IResult;

public interface IResultRepository
{
    IResult Create(IMathOperation mathOperation, double result);

    IResult Object(IMathOperation mathOperation);
}
