package com.example.springboot.interfaces.operations;

import com.example.springboot.entities.MathOperationEntity;
import com.example.springboot.interfaces.entities.IMathOperation;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public interface IMathOperationOperation {

    double Compute(IMathOperation entity);

    Collection<Double> Compute(Collection<IMathOperation> entities);

    CompletableFuture<Double> ComputeAsync(IMathOperation entity);
}
