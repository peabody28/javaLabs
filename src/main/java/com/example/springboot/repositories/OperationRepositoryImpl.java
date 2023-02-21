package com.example.springboot.repositories;

import com.example.springboot.interfaces.entities.IOperation;
import com.example.springboot.interfaces.repositories.IOperationRepository;
import com.example.springboot.interfaces.repositories.impls.IOperationRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component(value="operationRepositoryImpl")
public class OperationRepositoryImpl implements IOperationRepositoryImpl {

    @Lazy
    @Autowired
    private IOperationRepository operationRepository;

    @Override
    public IOperation get(String name) {
        return operationRepository.getByName(name);
    }
}
