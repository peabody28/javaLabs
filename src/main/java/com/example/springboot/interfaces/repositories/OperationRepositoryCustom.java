package com.example.springboot.interfaces.repositories;

import com.example.springboot.entities.OperationEntity;
import org.springframework.stereotype.Service;

@Service
public interface OperationRepositoryCustom {
    OperationEntity Get(String name);
}
