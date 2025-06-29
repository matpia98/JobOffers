package com.joboffers;

import com.joboffers.infrastructure.offer.http.OfferFetcherRestTemplateClientConfigurationProperties;
import com.joboffers.infrastructure.security.jwt.JwtConfigurationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableConfigurationProperties({OfferFetcherRestTemplateClientConfigurationProperties.class, JwtConfigurationProperties.class})
@EnableMongoRepositories
public class JobOffersSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobOffersSpringBootApplication.class, args);
    }
}
