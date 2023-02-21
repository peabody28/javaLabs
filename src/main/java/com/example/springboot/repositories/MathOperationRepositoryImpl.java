package com.example.springboot.repositories;

import com.example.springboot.entities.MathOperationEntity;
import com.example.springboot.entities.OperationEntity;
import com.example.springboot.interfaces.repositories.MathOperationRepository;
import com.example.springboot.interfaces.repositories.MathOperationRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class MathOperationRepositoryImpl implements MathOperationRepositoryCustom {

    @Lazy
    @Autowired
    private MathOperationRepository mathOperationRepository;

    @Override
    public MathOperationEntity Create(Double first, Double second, OperationEntity op) {
        var entity = new MathOperationEntity(first, second, op);
        var object = mathOperationRepository.save(entity);
        return object;
    }
}
