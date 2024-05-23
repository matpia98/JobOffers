package com.joboffers.domain.offer.dto;

import lombok.Builder;

@Builder
public record OfferResponseDto(
        Long id,
        String url,
        String jobTitle,
        String companyName,
        String salary
) {
}
