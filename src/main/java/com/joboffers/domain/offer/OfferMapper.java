package com.joboffers.domain.offer;

import com.joboffers.domain.offer.dto.JobOfferResponse;
import com.joboffers.domain.offer.dto.OfferResponseDto;

class OfferMapper {

    static Offer mapFromOfferResponseDtoToOffer(OfferResponseDto dto) {
        return Offer.builder()
                .url(dto.url())
                .position(dto.jobTitle())
                .companyName(dto.companyName())
                .salary(dto.salary())
                .build();
    }

    static OfferResponseDto mapFromOfferToOfferResponseDto(Offer offer) {
        return OfferResponseDto.builder()
                .id(offer.getId())
                .url(offer.getUrl())
                .jobTitle(offer.getPosition())
                .companyName(offer.getCompanyName())
                .salary(offer.getSalary())
                .build();
    }

    static Offer mapFromOfferResponseToOffer(JobOfferResponse jobOfferResponse) {
        return Offer.builder()
                .url(jobOfferResponse.offerUrl())
                .position(jobOfferResponse.title())
                .companyName(jobOfferResponse.company())
                .salary(jobOfferResponse.salary())
                .build();
    }
}
