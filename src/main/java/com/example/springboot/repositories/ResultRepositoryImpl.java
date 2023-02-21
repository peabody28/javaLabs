package com.example.springboot.repositories;

import com.example.springboot.entities.MathOperationEntity;
import com.example.springboot.entities.ResultEntity;
import com.example.springboot.interfaces.entities.IMathOperation;
import com.example.springboot.interfaces.entities.IResult;
import com.example.springboot.interfaces.repositories.IResultRepository;
import com.example.springboot.interfaces.repositories.impls.IResultRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component(value="resultRepositoryImpl")
public class ResultRepositoryImpl implements IResultRepositoryImpl {

    @Lazy
    @Autowired
    private IResultRepository resultRepository;

    @Override
    public IResult create(IMathOperation mathOperation, Double res) {
        var entity = new ResultEntity((MathOperationEntity) mathOperation, res);
        var object = resultRepository.save(entity);
        return object;
    }

    @Override
    public IResult get(IMathOperation mathOperation) {
        return resultRepository.getByMathOperation((MathOperationEntity) mathOperation);
    }

}
