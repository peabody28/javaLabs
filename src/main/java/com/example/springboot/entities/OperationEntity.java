package com.example.springboot.entities;

import com.example.springboot.interfaces.entities.IOperation;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name="operation")
public class OperationEntity implements IOperation
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    @Transient
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "operation")
    private Set<MathOperationEntity> mathOperations;

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<MathOperationEntity> getMathOperations() {
        return mathOperations;
    }

    public void setMathOperations(Set<MathOperationEntity> mathOperations) {
        this.mathOperations = mathOperations;
    }

    public OperationEntity(int _id, String _name)
    {
        id = _id;
        name = _name;
    }

    public OperationEntity(){}
}
