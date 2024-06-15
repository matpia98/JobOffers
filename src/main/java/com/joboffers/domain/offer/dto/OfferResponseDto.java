package com.joboffers.domain.offer.dto;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record OfferResponseDto(
        String id,
        String url,
        String jobTitle,
        String companyName,
        String salary
) implements Serializable {
}
