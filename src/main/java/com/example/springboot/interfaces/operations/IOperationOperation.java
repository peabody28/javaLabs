package com.example.springboot.interfaces.operations;

import com.example.springboot.entities.OperationEntity;
import com.example.springboot.interfaces.entities.IOperation;

public interface IOperationOperation
{
    OperationEntity Addition();
    OperationEntity Subtraction();
    OperationEntity Multiplication();
    OperationEntity Division();
}
