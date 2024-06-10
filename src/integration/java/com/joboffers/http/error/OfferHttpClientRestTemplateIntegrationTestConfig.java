package com.joboffers.http.error;

import com.joboffers.domain.offer.OfferFetcher;
import com.joboffers.infrastructure.offer.http.OfferClientConfig;
import org.springframework.web.client.RestTemplate;

public class OfferHttpClientRestTemplateIntegrationTestConfig extends OfferClientConfig {

    public OfferFetcher remoteOfferFetcherRestTemplateClient(int port, int connectionTimeout, int readTimeout) {
        RestTemplate restTemplate = restTemplateTestConfig(restTemplateResponseErrorHandler(),connectionTimeout,readTimeout);
        return offerFetcherRestTemplateClientTestConfig(restTemplate, "http://localhost", port);
    }

}
