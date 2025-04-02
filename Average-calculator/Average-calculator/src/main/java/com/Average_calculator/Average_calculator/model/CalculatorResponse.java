package com.example.AverageCalculatorApplication.model;

import java.util.List;
import lombok.Data;

@Data
public class CalculatorResponse {
    private List<Integer> windowPrevState;
    private List<Integer> windowCurrState;
    private int[] numbers;
    private double avg;
}