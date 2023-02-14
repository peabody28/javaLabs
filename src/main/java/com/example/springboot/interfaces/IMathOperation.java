package com.example.springboot.interfaces;

import com.example.springboot.entities.MathOperationEntity;

import java.util.Collection;

public interface IMathOperation {

    double Compute(MathOperationEntity entity);

    Collection<Double> Compute(Collection<MathOperationEntity> entities);
}
