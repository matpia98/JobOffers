package com.joboffers.infrastructure.offer.http;

import com.joboffers.domain.offer.OfferFetcher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class OfferClientConfig {

    @Bean
    public RestTemplateResponseErrorHandler restTemplateResponseErrorHandler() {
        return new RestTemplateResponseErrorHandler();
    }


    @Bean
    public RestTemplate restTemplate(RestTemplateResponseErrorHandler restTemplateResponseErrorHandler,
                                     OfferFetcherRestTemplateClientConfigurationProperties properties) {
        return new RestTemplateBuilder()
                .errorHandler(restTemplateResponseErrorHandler)
                .setConnectTimeout(Duration.ofMillis(properties.connectionTimeout()))
                .setReadTimeout(Duration.ofMillis(properties.readTimeout()))
                .build();
    }


    public RestTemplate restTemplateTestConfig(RestTemplateResponseErrorHandler restTemplateResponseErrorHandler,
                                     int connectionTimeout, int readTimeout) {
        return new RestTemplateBuilder()
                .errorHandler(restTemplateResponseErrorHandler)
                .setConnectTimeout(Duration.ofMillis(connectionTimeout))
                .setReadTimeout(Duration.ofMillis(readTimeout))
                .build();
    }


    public OfferFetcher offerFetcherRestTemplateClientTestConfig(RestTemplate restTemplate,
                                                                 String uri, int port) {
        return new OfferHttpClient(restTemplate, uri, port);
    }

    @Bean
    public OfferFetcher offerFetcherRestTemplateClient(RestTemplate restTemplate,
                                                       OfferFetcherRestTemplateClientConfigurationProperties properties) {
        return new OfferHttpClient(restTemplate, properties.uri(), properties.port());
    }
}
