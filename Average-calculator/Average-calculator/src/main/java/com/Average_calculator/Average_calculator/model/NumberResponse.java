package com.example.AverageCalculatorApplication.model;

import java.util.Arrays;
import lombok.Data;

@Data
public class NumberResponse {
    private int[] numbers;
    
    @Override
    public String toString() {
        return "NumberResponse{" +
                "numbers=" + Arrays.toString(numbers) +
                '}';
    }
}