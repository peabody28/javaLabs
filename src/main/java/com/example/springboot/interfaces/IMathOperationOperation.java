package com.example.springboot.interfaces;

import com.example.springboot.entities.MathOperationEntity;

import java.util.Collection;

public interface IMathOperationOperation {

    double Compute(MathOperationEntity entity);

    Collection<Double> Compute(Collection<MathOperationEntity> entities);
}
