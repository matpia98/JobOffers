package com.joboffers.domain.offer.dto;

import lombok.Builder;

@Builder
public record JobOfferResponse(
        String jobTitle,
        String companyName,
        Integer salary,
        String url
) {
}
