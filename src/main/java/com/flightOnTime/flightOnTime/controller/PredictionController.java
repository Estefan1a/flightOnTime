package com.flightOnTime.flightOnTime.controller;

import com.flightOnTime.flightOnTime.predictions.datosRegistroPrediccion;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/predicciones")
public class PredictionController {

    @PostMapping
    public void predict(@RequestBody datosRegistroPrediccion datos){
        System.out.println(datos);
    }

}
