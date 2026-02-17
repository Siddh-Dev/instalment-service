package com.creditcard.instalment.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * Configuration class for RestTemplate bean used to call downstream APIs.
 */
@Configuration
public class RestTemplateConfig {

    /**
     * Creates a RestTemplate bean with sensible timeout defaults.
     *
     * @param builder Spring's RestTemplateBuilder (auto-configured)
     * @return configured RestTemplate instance
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {

        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5_000);   // ms
        factory.setReadTimeout(10_000);     // ms
        return new RestTemplate(factory);

    }
}
