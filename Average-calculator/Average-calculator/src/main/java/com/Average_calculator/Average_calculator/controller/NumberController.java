package com.example.AverageCalculatorApplication.controller;

import com.example.averagecalculator.model.CalculatorResponse;
import com.example.averagecalculator.service.NumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
public class NumberController {
    private static final Logger LOGGER = Logger.getLogger(NumberController.class.getName());
    
    private final NumberService numberService;
    
    @Autowired
    public NumberController(NumberService numberService) {
        this.numberService = numberService;
    }
    
    @GetMapping("/numbers/{numberid}")
    public ResponseEntity<CalculatorResponse> getNumbers(@PathVariable String numberid) {
        LOGGER.info("Received request for number type: " + numberid);
        
        // Validate input - only accept p, f, e, or r
        if (!numberid.matches("[pfer]")) {
            LOGGER.warning("Invalid number type: " + numberid);
            return ResponseEntity.badRequest().build();
        }
        
        // Process the request and return the response
        CalculatorResponse response = numberService.processNumbers(numberid);
        return ResponseEntity.ok(response);
    }
}