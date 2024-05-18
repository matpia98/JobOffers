package com.joboffers.domain.offer;

import com.joboffers.domain.offer.dto.JobOfferResponse;
import java.util.Arrays;
import java.util.List;

class OfferFetcherImpl implements OfferFetcher {
    @Override
    public List<JobOfferResponse> fetchOffers() {
        return Arrays.asList(
                JobOfferResponse.builder()
                        .url("https://joboffers.com/offer/1")
                        .jobTitle("Junior Java Developer")
                        .companyName("Company A")
                        .salary(5000)
                        .build(),
                JobOfferResponse.builder()
                        .url("https://joboffers.com/offer/2")
                        .jobTitle("Java Developer")
                        .companyName("Company B")
                        .salary(10000)
                        .build(),
                JobOfferResponse.builder()
                        .url("https://joboffers.com/offer/3")
                        .jobTitle("Junior Java Software Engineer")
                        .companyName("Company C")
                        .salary(7000)
                        .build(),
                JobOfferResponse.builder()
                        .url("https://joboffers.com/offer/4")
                        .jobTitle("Java Backend Developer")
                        .companyName("Company D")
                        .salary(8000)
                        .build()
        );
    }
}
