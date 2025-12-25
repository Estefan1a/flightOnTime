package com.flightOnTime.flightOnTime.exception;

public class OraclePredictionException extends RuntimeException {

    public OraclePredictionException(String message) {
        super(message);
    }

    public OraclePredictionException(String message, Throwable cause) {
        super(message, cause);
    }
}
