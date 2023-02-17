package com.example.springboot.operations;

import com.example.springboot.entities.MathOperationEntity;
import com.example.springboot.interfaces.entities.IMathOperation;
import com.example.springboot.models.math.MathOperationMemberStatistic;
import com.example.springboot.models.math.MathOperationModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class MathOperationResultAggregatorOperation {
    public static Collection<MathOperationMemberStatistic> Aggreagate(
            Collection<IMathOperation> entities, Collection<Double> results)
    {
        var stats = new ArrayList<MathOperationMemberStatistic>();

        // first stat
        var firsts = entities.stream().parallel().map(IMathOperation::getFirst)
                .collect(Collectors.toCollection(ArrayList::new));

        var middleFirstArg = firsts.stream().reduce(Double::sum).get() / firsts.size();

        stats.add(new MathOperationMemberStatistic("first", Collections.min(firsts), middleFirstArg, Collections.max(firsts)));

        // second stat
        var seconds = entities.stream().parallel().map(IMathOperation::getSecond)
                .collect(Collectors.toCollection(ArrayList::new));

        var middleSecondArg = seconds.stream().reduce(Double::sum).get() / seconds.size();

        stats.add(new MathOperationMemberStatistic("second", Collections.min(seconds), middleSecondArg, Collections.max(seconds)));

        // result stat
        var middleResult = results.stream().reduce(Double::sum).get() / results.size();

        stats.add(new MathOperationMemberStatistic("result", Collections.min(results), middleResult, Collections.max(results)));

        return stats;
    }
}
