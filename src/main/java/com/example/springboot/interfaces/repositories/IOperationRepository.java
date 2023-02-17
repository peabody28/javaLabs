package com.example.springboot.interfaces.repositories;

import com.example.springboot.interfaces.entities.IOperation;

public interface IOperationRepository
{
    IOperation Object(String name);
}
