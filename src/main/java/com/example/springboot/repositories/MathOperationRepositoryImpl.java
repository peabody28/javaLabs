package com.example.springboot.repositories;

import com.example.springboot.entities.MathOperationEntity;
import com.example.springboot.entities.OperationEntity;
import com.example.springboot.interfaces.entities.IMathOperation;
import com.example.springboot.interfaces.entities.IOperation;
import com.example.springboot.interfaces.repositories.IMathOperationRepository;
import com.example.springboot.interfaces.repositories.impls.IMathOperationRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component(value="mathOperationRepositoryImpl")
public class MathOperationRepositoryImpl implements IMathOperationRepositoryImpl {

    @Lazy
    @Autowired
    private IMathOperationRepository mathOperationRepository;

    @Override
    public IMathOperation create(Double first, Double second, IOperation op) {
        var entity = new MathOperationEntity(first, second, (OperationEntity) op);
        var object = mathOperationRepository.save(entity);
        return object;
    }

    @Override
    public IMathOperation get(Integer id) {
        var optionalEntity = mathOperationRepository.findById(id);
        return optionalEntity.orElse(null);
    }
}
