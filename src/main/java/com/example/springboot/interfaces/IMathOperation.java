package com.example.springboot.interfaces;

import com.example.springboot.enums.Operation;

public interface IMathOperation {

    double Compute(double first, double second, Operation op);
}
