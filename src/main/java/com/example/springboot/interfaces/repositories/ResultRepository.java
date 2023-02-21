package com.example.springboot.interfaces.repositories;

import com.example.springboot.entities.MathOperationEntity;
import com.example.springboot.entities.ResultEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultRepository extends CrudRepository<ResultEntity, Integer>, ResultRepositoryCustom
{
    ResultEntity getByMathOperation(MathOperationEntity mathOperation);
}
