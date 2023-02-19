package com.example.springboot.interfaces.repositories;

import com.example.springboot.entities.MathOperationEntity;
import com.example.springboot.entities.ResultEntity;
import org.springframework.stereotype.Service;

@Service
public interface ResultRepositoryCustom {
    ResultEntity Create(MathOperationEntity mathOperation, Double res);

    ResultEntity Get(MathOperationEntity mathOperation);
}
