package com.flightOnTime.flightOnTime.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuración de infraestructura HTTP para la aplicación.
 *
 * Esta clase expone un {@link RestTemplate} como bean de Spring,
 * permitiendo su inyección en componentes que necesiten realizar
 * llamadas HTTP a servicios externos.
 *
 * En este proyecto se utiliza principalmente para:
 * - Comunicación con el microservicio de predicción (oráculo)
 * - Integración con servicios externos vía REST
 *
 * Centralizar la creación del {@link RestTemplate} facilita:
 * - Su reutilización en toda la aplicación
 * - La futura incorporación de interceptores, timeouts o configuración
 *   de seguridad sin modificar los clientes que lo utilizan
 */
@Configuration
public class RestTemplateConfig {

    /**
     * Crea y registra un {@link RestTemplate} en el contexto de Spring.
     *
     * @return instancia configurada de {@link RestTemplate}
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
