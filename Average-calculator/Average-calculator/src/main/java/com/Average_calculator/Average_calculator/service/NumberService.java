package com.example.AverageCalculatorApplication.service;

import com.example.averagecalculator.model.CalculatorResponse;
import com.example.averagecalculator.model.NumberResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class NumberService {
    private static final Logger LOGGER = Logger.getLogger(NumberService.class.getName());
    
    @Value("${window.size}")
    private int windowSize;
    
    @Value("${api.prime-url}")
    private String primeUrl;
    
    @Value("${api.fibonacci-url}")
    private String fibonacciUrl;
    
    @Value("${api.even-url}")
    private String evenUrl;
    
    @Value("${api.random-url}")
    private String randomUrl;
    
    private final RestTemplate restTemplate;
    private final LinkedList<Integer> numberWindow = new LinkedList<>();
    
    @Autowired
    public NumberService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    public CalculatorResponse processNumbers(String numberType) {
        // Store the previous state of the window before making any changes
        List<Integer> prevState = new ArrayList<>(numberWindow);
        
        // Fetch numbers from the appropriate API based on number type
        NumberResponse response = fetchNumbers(numberType);
        int[] fetchedNumbers = new int[0]; // Default empty array
        
        if (response != null && response.getNumbers() != null) {
            fetchedNumbers = response.getNumbers();
            
            // Process new numbers (add unique numbers to window)
            for (int number : fetchedNumbers) {
                // Only add unique numbers to the window
                if (!numberWindow.contains(number)) {
                    // If window is full, remove the oldest number
                    if (numberWindow.size() >= windowSize) {
                        numberWindow.removeFirst();
                    }
                    // Add the new number to the end of the window
                    numberWindow.add(number);
                }
            }
        }
        
        // Calculate the average of numbers in the window
        double avg = 0.0;
        if (!numberWindow.isEmpty()) {
            avg = numberWindow.stream()
                    .mapToInt(Integer::intValue)
                    .average()
                    .orElse(0.0);
        }
        
        // Create and return the response
        CalculatorResponse calculatorResponse = new CalculatorResponse();
        calculatorResponse.setWindowPrevState(prevState);
        calculatorResponse.setWindowCurrState(new ArrayList<>(numberWindow));
        calculatorResponse.setNumbers(fetchedNumbers);
        calculatorResponse.setAvg(Math.round(avg * 100.0) / 100.0); // Round to 2 decimal places
        
        return calculatorResponse;
    }
    
    private NumberResponse fetchNumbers(String numberType) {
        String url;
        
        // Select the appropriate URL based on number type
        switch (numberType) {
            case "p":
                url = primeUrl;
                break;
            case "f":
                url = fibonacciUrl;
                break;
            case "e":
                url = evenUrl;
                break;
            case "r":
                url = randomUrl;
                break;
            default:
                LOGGER.warning("Invalid number type: " + numberType);
                return null;
        }
        
        try {
            // Make request to the external API
            LOGGER.info("Fetching numbers from: " + url);
            return restTemplate.getForObject(url, NumberResponse.class);
        } catch (RestClientException e) {
            // Log and handle timeout or other API errors
            LOGGER.warning("Error fetching from " + url + ": " + e.getMessage());
            return null;
        }
    }
}