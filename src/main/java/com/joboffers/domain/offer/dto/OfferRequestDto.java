package com.joboffers.domain.offer.dto;

import lombok.Builder;

@Builder
public record OfferRequestDto(
        String jobTitle,
        String companyName,
        String salary,
        String url
) {
}
