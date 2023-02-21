package com.example.springboot.interfaces.repositories.impls;

import com.example.springboot.interfaces.entities.IMathOperation;
import com.example.springboot.interfaces.entities.IResult;

public interface IResultRepositoryImpl {
    IResult create(IMathOperation mathOperation, Double res);

    IResult get(IMathOperation mathOperation);
}
