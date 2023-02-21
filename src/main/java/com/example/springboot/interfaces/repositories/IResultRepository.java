package com.example.springboot.interfaces.repositories;

import com.example.springboot.entities.MathOperationEntity;
import com.example.springboot.entities.ResultEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IResultRepository extends CrudRepository<ResultEntity, Integer>
{
    ResultEntity getByMathOperation(MathOperationEntity mathOperation);
}
