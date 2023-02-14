package com.example.springboot.models.math;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StatisticsModel {
    @JsonProperty("requestCount")
    public int count;
}
