package com.joboffers.domain.offer;

import com.joboffers.domain.offer.dto.OfferRequestDto;
import com.joboffers.domain.offer.dto.OfferResponseDto;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class OfferFacade {

    private final OfferValidator offerValidator;
    private final OfferRepository offerRepository;
    private final OfferService offerService;

    public OfferResponseDto saveOffer(OfferRequestDto offerRequestDto) {
        offerValidator.validate(offerRequestDto);
        offerValidator.validateUrl(offerRequestDto.url());
        Offer savedOffer = offerRepository.save(Offer.builder()
                .jobTitle(offerRequestDto.jobTitle())
                .companyName(offerRequestDto.companyName())
                .salary(offerRequestDto.salary())
                .url(offerRequestDto.url())
                .build());
        return OfferResponseDto.builder()
                .id(savedOffer.getId())
                .url(savedOffer.getUrl())
                .jobTitle(savedOffer.getJobTitle())
                .companyName(savedOffer.getCompanyName())
                .salary(savedOffer.getSalary())
                .build();
    }

    public List<OfferResponseDto> fetchAndSaveAllOffersIfNotExists() {
        return offerService.fetchAndSaveAllOffersIfNotExists();
    }

    public OfferResponseDto retrieveOfferById(Long id) {
        Offer offerById = offerRepository.findById(id)
                .orElseThrow(() -> new OfferNotFoundException("Offer not found"));
        return OfferResponseDto.builder()
                .id(offerById.getId())
                .url(offerById.getUrl())
                .jobTitle(offerById.getJobTitle())
                .companyName(offerById.getCompanyName())
                .salary(offerById.getSalary())
                .build();
    }

    public List<OfferResponseDto> findAllOffers() {
        return offerRepository.findAll()
                .stream()
                .map(OfferMapper::mapFromOfferToOfferResponseDto)
                .collect(Collectors.toList());
    }
}
