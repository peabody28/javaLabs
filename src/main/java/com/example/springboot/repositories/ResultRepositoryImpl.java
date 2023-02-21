package com.example.springboot.repositories;

import com.example.springboot.entities.MathOperationEntity;
import com.example.springboot.entities.ResultEntity;
import com.example.springboot.interfaces.repositories.ResultRepository;
import com.example.springboot.interfaces.repositories.ResultRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ResultRepositoryImpl implements ResultRepositoryCustom {

    @Lazy
    @Autowired
    private ResultRepository resultRepository;

    @Override
    public ResultEntity Create(MathOperationEntity mathOperation, Double res) {
        var entity = new ResultEntity(mathOperation, res);
        var object = resultRepository.save(entity);
        return object;
    }
}
