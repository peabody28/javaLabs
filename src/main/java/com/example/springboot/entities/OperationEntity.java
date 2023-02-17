package com.example.springboot.entities;

import com.example.springboot.interfaces.entities.IOperation;

public class OperationEntity implements IOperation
{
    public int id;
    public String name;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public OperationEntity(int _id, String _name)
    {
        id = _id;
        name = _name;
    }
}
