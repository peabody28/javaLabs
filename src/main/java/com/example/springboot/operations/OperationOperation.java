package com.example.springboot.operations;

import com.example.springboot.enums.Operation;
import com.example.springboot.interfaces.entities.IOperation;
import com.example.springboot.interfaces.operations.IOperationOperation;
import com.example.springboot.interfaces.repositories.IOperationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(value = "operationOperation")
public class OperationOperation implements IOperationOperation
{
    @Autowired
    public IOperationRepository operationRepository;

    public IOperation Addition() { return operationRepository.Object(Operation.Addition.name()); }
    public IOperation Subtraction() { return operationRepository.Object(Operation.Subtraction.name()); };
    public IOperation Multiplication() {return operationRepository.Object(Operation.Multiplication.name()); };
    public IOperation Division() { return operationRepository.Object(Operation.Division.name()); }
}
