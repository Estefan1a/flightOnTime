package com.flightOnTime.flightOnTime.exception;

/**
 * Excepción personalizada que representa un error al comunicarse
 * con el servicio externo de predicción (oráculo).
 *
 * Se utiliza para encapsular fallos como:
 * - Errores de red
 * - Timeouts
 * - Respuestas inválidas del servicio de predicción
 *
 * Esta excepción es capturada por el {@link GlobalExceptionHandler}
 * para devolver una respuesta controlada al cliente (HTTP 503).
 */
public class OraclePredictionException extends RuntimeException {

    /**
     * Crea una excepción con un mensaje descriptivo.
     *
     * @param message descripción del error ocurrido
     */
    public OraclePredictionException(String message) {
        super(message);
    }

    /**
     * Crea una excepción con un mensaje descriptivo y la causa original.
     *
     * Útil para preservar el stack trace de errores externos
     * (ej: RestClientException).
     *
     * @param message descripción del error ocurrido
     * @param cause   causa original de la excepción
     */
    public OraclePredictionException(String message, Throwable cause) {
        super(message, cause);
    }
}

