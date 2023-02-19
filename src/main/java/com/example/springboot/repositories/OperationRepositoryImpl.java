package com.example.springboot.repositories;

import com.example.springboot.entities.OperationEntity;
import com.example.springboot.interfaces.repositories.OperationRepositoryCustom;
import com.example.springboot.interfaces.repositories.OperationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class OperationRepositoryImpl implements OperationRepositoryCustom {

    @Lazy
    @Autowired
    private OperationRepository operationRepository;

    @Override
    public OperationEntity Get(String name) {
        var collection = operationRepository.findAll();
        for(var item : collection)
            if(Objects.equals(item.getName(), name))
                return item;
        return null;
    }
}
