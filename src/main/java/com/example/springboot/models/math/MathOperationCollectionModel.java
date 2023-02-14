package com.example.springboot.models.math;

import com.example.springboot.operations.MathOperation;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;

public class MathOperationCollectionModel {
    @JsonProperty("collection")
    public Collection<MathOperationModel> collection;

    public void setCollection(Collection<MathOperationModel> _c)
    {
        collection=_c;
    }
}
