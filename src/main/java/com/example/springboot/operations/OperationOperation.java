package com.example.springboot.operations;

import com.example.springboot.entities.OperationEntity;
import com.example.springboot.enums.Operation;
import com.example.springboot.interfaces.operations.IOperationOperation;
import com.example.springboot.interfaces.repositories.OperationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(value = "operationOperation")
public class OperationOperation implements IOperationOperation
{
    @Autowired
    public OperationRepository operationRepository;

    public OperationEntity Addition() { return operationRepository.getByName(Operation.Addition.name()); }
    public OperationEntity Subtraction() { return operationRepository.getByName(Operation.Subtraction.name()); };
    public OperationEntity Multiplication() {return operationRepository.getByName(Operation.Multiplication.name()); };
    public OperationEntity Division() { return operationRepository.getByName(Operation.Division.name()); }
}
