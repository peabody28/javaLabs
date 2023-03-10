package com.example.springboot.interfaces.repositories;

import com.example.springboot.entities.MathOperationEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IMathOperationRepository extends CrudRepository<MathOperationEntity, Integer>
{

}
