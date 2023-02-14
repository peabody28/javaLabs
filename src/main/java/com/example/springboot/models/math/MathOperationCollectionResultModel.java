package com.example.springboot.models.math;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class MathOperationCollectionResultModel {
    @JsonProperty("results")
    public Collection<MathOperationResultModel> results;

    @JsonProperty("memberStatistics")
    public Collection<MathOperationMemberStatistic> stats;


    public MathOperationCollectionResultModel(Collection<MathOperationResultModel> _results,
                                              Collection<MathOperationMemberStatistic> _stats)
    {
        results = _results;
        stats = _stats;
    }
}
