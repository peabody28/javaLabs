package com.example.springboot.operations;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component(value="counter")
@Scope("singleton")
public class CounterOperation {
    private static int count;

    public void Add()
    {
        count++;
    }

    public int GetCount()
    {
        return count;
    }
}
