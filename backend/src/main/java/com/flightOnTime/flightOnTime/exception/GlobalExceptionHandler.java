package com.flightOnTime.flightOnTime.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
/**
 * Manejador global de excepciones para la API REST.
 *
 * Centraliza el tratamiento de errores comunes y garantiza
 * respuestas JSON consistentes para el frontend o consumidores externos.
 *
 * Beneficios:
 * - Evita lógica repetida de manejo de errores en los controllers
 * - Mejora la experiencia del cliente con mensajes claros
 * - Facilita el debug y mantenimiento del sistema
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja errores de validación de DTOs anotados con Bean Validation.
     *
     * Se dispara cuando un @RequestBody con @Valid no cumple
     * las restricciones definidas (ej: @NotNull, @Future, @Positive).
     *
     * Respuesta:
     * - HTTP 400 Bad Request
     * - JSON con pares campo -> mensaje de error
     *
     * Ejemplo:
     * {
     *   "origen": "El aeropuerto de origen es obligatorio",
     *   "fechaPartida": "La fecha de salida debe ser posterior a la fecha actual"
     * }
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(
            MethodArgumentNotValidException ex
    ) {
        Map<String, String> errors = new HashMap<>();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(
                    fieldError.getField(),
                    fieldError.getDefaultMessage()
            );
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errors);
    }

    /**
     * Maneja errores producidos al comunicarse con el servicio externo
     * de predicción (oráculo / microservicio de Data Science).
     *
     * Se utiliza cuando el oráculo no responde, falla o devuelve un error.
     *
     * Respuesta:
     * - HTTP 503 Service Unavailable
     * - JSON con mensaje descriptivo del problema
     */
    @ExceptionHandler(OraclePredictionException.class)
    public ResponseEntity<Map<String, String>> handleOracle(
            OraclePredictionException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of("error", ex.getMessage()));
    }

    /**
     * Maneja cualquier excepción no controlada previamente.
     *
     * Evita exponer detalles internos del sistema al cliente
     * y devuelve un mensaje genérico de error.
     *
     * Respuesta:
     * - HTTP 500 Internal Server Error
     * - Mensaje genérico para el consumidor
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneral(
            Exception ex
    ) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Error interno del servidor"));
    }
}
