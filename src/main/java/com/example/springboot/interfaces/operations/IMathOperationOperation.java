package com.example.springboot.interfaces.operations;

import com.example.springboot.entities.MathOperationEntity;
import com.example.springboot.interfaces.entities.IMathOperation;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public interface IMathOperationOperation {

    double Compute(MathOperationEntity entity);

    Collection<Double> Compute(Collection<MathOperationEntity> entities);

    CompletableFuture<Double> ComputeAsync(MathOperationEntity entity);
}
