package com.example.springboot.entities;

import com.example.springboot.interfaces.entities.IMathOperation;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name="mathOperation")
public class MathOperationEntity implements IMathOperation
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Double first;
    private Double second;

    @ManyToOne
    @JoinColumn(name = "operation_id", nullable = false)
    private OperationEntity operation;

    @Transient
    @OneToMany(mappedBy = "mathOperation")
    private Set<ResultEntity> results;

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public Double getFirst() {
        return first;
    }

    public void setFirst(Double first) {
        this.first = first;
    }

    @Override
    public Double getSecond() {
        return second;
    }

    public void setSecond(Double second) {
        this.second = second;
    }

    @Override
    public OperationEntity getOperation() {
        return operation;
    }

    public void setOperation(OperationEntity operation) {
        this.operation = operation;
    }

    public Set<ResultEntity> getResults() {
        return results;
    }

    public void setResults(Set<ResultEntity> results) {
        this.results = results;
    }

    public MathOperationEntity(int _id, double _first, double _second, OperationEntity _op)
    {
        id = _id;
        first = _first;
        second = _second;
        operation = _op;
    }

    public MathOperationEntity(double _first, double _second, OperationEntity _op)
    {
        first = _first;
        second = _second;
        operation = _op;
    }

    public MathOperationEntity(){}
}
