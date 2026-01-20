package com.flightOnTime.flightOnTime.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class FlaskTestController {


    @GetMapping("/test-flask")
    public ResponseEntity<String> testFlask() {


        RestTemplate restTemplate = new RestTemplate();

        String response = restTemplate.getForObject(
                "http://localhost:5000/health",
                String.class
        );

        return ResponseEntity.ok(response);
    }
}
