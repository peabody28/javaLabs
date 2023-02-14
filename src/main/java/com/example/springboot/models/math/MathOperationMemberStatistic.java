package com.example.springboot.models.math;

public class MathOperationMemberStatistic {
    public String name;

    public double min;

    public double middle;

    public double max;

    public MathOperationMemberStatistic(String _name, double _min, double _middle, double _max)
    {
        name = _name;
        min = _min;
        max = _max;
        middle = _middle;
    }
}
