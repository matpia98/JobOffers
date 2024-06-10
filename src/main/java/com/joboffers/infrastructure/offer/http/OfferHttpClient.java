package com.joboffers.infrastructure.offer.http;

import com.joboffers.domain.offer.OfferFetcher;
import com.joboffers.domain.offer.dto.JobOfferResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@Log4j2
public class OfferHttpClient implements OfferFetcher {

    private final RestTemplate restTemplate;
    private final String uri;
    private final int port;
    @Override
    public List<JobOfferResponse> fetchOffers() {
        log.info("Started fetching offers using http client");
        String urlForService = getUrlForService("/offers");
        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<HttpHeaders> requestEntity = new HttpEntity<>(httpHeaders);
        String url = UriComponentsBuilder.fromHttpUrl(urlForService)
                .toUriString();
        try {
            ResponseEntity<List<JobOfferResponse>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    new ParameterizedTypeReference<>() {
                    });
            List<JobOfferResponse> body = response.getBody();
            if (body == null) {
                log.info("Response Body was null returning empty list");
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            log.info("Response body returned: " + body);
            return response.getBody();
        } catch (ResourceAccessException e) {
            log.error("Error while fetching offers using http client: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
//        catch (JsonProcessingException e) {
//            log.error("Error while parsing body to pretty JSON: " + e.getMessage());
//            return Collections.emptyList();
//        }
    }

    private String getUrlForService(String service) {
        return uri + ":" + port + service;
    }
}
