package com.example.springboot.entities;

import com.example.springboot.interfaces.entities.IResult;
import jakarta.persistence.*;

@Entity
@Table(name="result")
public class ResultEntity implements IResult
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @ManyToOne
    @JoinColumn(name = "math_operation_id", nullable = false)
    private MathOperationEntity mathOperation;

    @Column(name = "value")
    public Double result;

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public MathOperationEntity getMathOperation() {
        return mathOperation;
    }

    public void setMathOperation(MathOperationEntity mathOperation) {
        this.mathOperation = mathOperation;
    }

    @Override
    public Double getResult() {
        return result;
    }

    public void setResult(Double result) {
        this.result = result;
    }


    public ResultEntity(MathOperationEntity _m, double _res)
    {
        mathOperation = _m;
        result = _res;
    }

    public ResultEntity(){}
}
