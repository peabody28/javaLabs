package com.example.springboot.interfaces.repositories;

import com.example.springboot.entities.MathOperationEntity;
import com.example.springboot.entities.OperationEntity;
import org.springframework.stereotype.Service;

@Service
public interface MathOperationRepositoryCustom {
    MathOperationEntity Create(Double first, Double second, OperationEntity op);
}
