package com.example.springboot.interfaces.repositories;

import com.example.springboot.entities.OperationEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationRepository extends CrudRepository<OperationEntity, Integer>
{
    OperationEntity getByName(String name);
}
