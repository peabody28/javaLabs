package com.example.springboot.interfaces.repositories;

import com.example.springboot.interfaces.entities.IMathOperation;
import com.example.springboot.interfaces.entities.IOperation;

public interface IMathOperationRepository
{
    IMathOperation Create(double first, double second, IOperation operation);

    IMathOperation Object(int id);
}
