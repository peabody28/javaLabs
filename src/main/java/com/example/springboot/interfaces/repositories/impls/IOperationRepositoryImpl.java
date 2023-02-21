package com.example.springboot.interfaces.repositories.impls;

import com.example.springboot.interfaces.entities.IOperation;

public interface IOperationRepositoryImpl {

    IOperation get(String name);
}
