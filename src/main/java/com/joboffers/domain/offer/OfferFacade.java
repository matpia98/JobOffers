package com.joboffers.domain.offer;

import com.joboffers.domain.offer.dto.OfferRequestDto;
import com.joboffers.domain.offer.dto.OfferResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;

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
                .position(offerRequestDto.jobTitle())
                .companyName(offerRequestDto.companyName())
                .salary(offerRequestDto.salary())
                .url(offerRequestDto.url())
                .build());
        return OfferMapper.mapFromOfferToOfferResponseDto(savedOffer);
    }

    public List<OfferResponseDto> fetchAndSaveAllOffersIfNotExists() {
        return offerService.fetchAndSaveAllOffersIfNotExists();
    }

    public OfferResponseDto retrieveOfferById(String id) {
        Offer offerById = offerRepository.findById(id)
                .orElseThrow(() -> new OfferNotFoundException("Offer with id " + id + " not found"));
        return OfferMapper.mapFromOfferToOfferResponseDto(offerById);
    }

    @Cacheable("jobOffers")
    public List<OfferResponseDto> findAllOffers() {
        return offerRepository.findAll()
                .stream()
                .map(OfferMapper::mapFromOfferToOfferResponseDto)
                .collect(Collectors.toList());

    }
}
