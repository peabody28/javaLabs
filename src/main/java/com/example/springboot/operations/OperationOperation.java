package com.example.springboot.operations;

import com.example.springboot.entities.OperationEntity;
import com.example.springboot.enums.Operation;
import com.example.springboot.interfaces.entities.IOperation;
import com.example.springboot.interfaces.operations.IOperationOperation;
import com.example.springboot.interfaces.repositories.IOperationRepository;
import com.example.springboot.interfaces.repositories.impls.IOperationRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(value = "operationOperation")
public class OperationOperation implements IOperationOperation
{
    @Autowired
    public IOperationRepositoryImpl operationRepositoryImpl;

    public IOperation Addition() { return operationRepositoryImpl.get(Operation.Addition.name()); }
    public IOperation Subtraction() { return operationRepositoryImpl.get(Operation.Subtraction.name()); };
    public IOperation Multiplication() {return operationRepositoryImpl.get(Operation.Multiplication.name()); };
    public IOperation Division() { return operationRepositoryImpl.get(Operation.Division.name()); }
}
