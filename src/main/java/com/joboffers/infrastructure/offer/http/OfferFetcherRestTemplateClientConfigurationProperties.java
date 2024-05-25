package com.joboffers.infrastructure.offer.http;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "offer.http.client.config")
public record OfferFetcherRestTemplateClientConfigurationProperties(
        String uri,
        int port,
        int connectionTimeout,
        int readTimeout
) {

}