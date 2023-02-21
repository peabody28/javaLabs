package com.example.springboot.interfaces.repositories.impls;

import com.example.springboot.interfaces.entities.IMathOperation;
import com.example.springboot.interfaces.entities.IOperation;


public interface IMathOperationRepositoryImpl {
    IMathOperation create(Double first, Double second, IOperation op);

    IMathOperation get(Integer id);
}
