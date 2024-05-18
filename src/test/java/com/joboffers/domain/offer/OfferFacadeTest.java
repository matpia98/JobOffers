package com.joboffers.domain.offer;

import com.joboffers.domain.offer.dto.JobOfferResponse;
import com.joboffers.domain.offer.dto.OfferRequestDto;
import com.joboffers.domain.offer.dto.OfferResponseDto;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class OfferFacadeTest {

    private final OfferRepository offerRepository = new InMemoryOfferRepository();

    @Test
    public void should_correctly_save_offer() {
        // given
        final OfferFetcher offerFetcher = new InMemoryOfferFetcher(Collections.emptyList());
        OfferFacade offerFacade = OfferFacadeConfiguration.createForTest(offerRepository, offerFetcher);
        OfferRequestDto offerRequestDto = OfferRequestDto.builder()
                .jobTitle("Junior Java Developer")
                .companyName("Assecco")
                .salary(6000)
                .url("asdasd1")
                .build();

        // when
        OfferResponseDto offerResponseDto = offerFacade.saveOffer(offerRequestDto);

        // then
        assertThat(offerResponseDto.id()).isEqualTo(0L);
        assertThat(offerResponseDto.url()).isNotNull();
        assertThat(offerResponseDto.jobTitle()).isEqualTo("Junior Java Developer");
        assertThat(offerResponseDto.companyName()).isEqualTo("Assecco");
        assertThat(offerResponseDto.salary()).isEqualTo(6000);
    }

    @Test
    public void should_return_correct_url() {
        final OfferFetcher offerFetcher = new InMemoryOfferFetcher(Collections.emptyList());
        OfferFacade offerFacade = OfferFacadeConfiguration.createForTest(offerRepository, offerFetcher);
        OfferRequestDto offerRequestDto = OfferRequestDto.builder()
                .jobTitle("Junior Java Developer")
                .companyName("Assecco")
                .salary(6000)
                .url("jobfetcher.com")
                .build();

        // when
        String url = offerFacade.saveOffer(offerRequestDto).url();

        // then
        assertThat(url).isEqualTo(offerRequestDto.url());

    }

    @Test
    public void should_throw_duplicate_url_exception_when_with_offer_url_exists() {
        // given
        final OfferFetcher offerFetcher = new InMemoryOfferFetcher(Collections.emptyList());
        OfferFacade offerFacade = OfferFacadeConfiguration.createForTest(offerRepository, offerFetcher);
        OfferRequestDto offerRequestDto = OfferRequestDto.builder()
                .jobTitle("Junior Java Developer")
                .companyName("Assecco")
                .salary(6000)
                .url("juniorjobs.com/1")
                .build();
        offerFacade.saveOffer(offerRequestDto);

        OfferRequestDto offerRequestDto2 = OfferRequestDto.builder()
                .jobTitle("Assecco Junior Java Developer")
                .companyName("Assecco")
                .salary(5800)
                .url("juniorjobs.com/1")
                .build();

        // when
        Throwable throwable = catchThrowable(() -> offerFacade.saveOffer(offerRequestDto2));

        // then
        assertThat(throwable).isInstanceOf(DuplicateUrlException.class)
                .hasMessage("Offer with URL juniorjobs.com/1 already exists");
    }
    @Test
    public void should_throw_exception_when_field_is_null() {
        // given
        final OfferFetcher offerFetcher = new InMemoryOfferFetcher(Collections.emptyList());
        OfferFacade offerFacade = OfferFacadeConfiguration.createForTest(offerRepository, offerFetcher);
        OfferRequestDto offerRequestDto = OfferRequestDto.builder()
                .jobTitle("Junior Java Developer")
                .companyName("")
                .salary(6000)
                .build();

        // when
        Throwable exception = catchThrowable(() -> offerFacade.saveOffer(offerRequestDto));

        // then
        assertThat(exception).isInstanceOf(OfferNotValidException.class)
                .hasMessage("Company name cannot be null or empty");
    }

    @Test
    public void should_throw_exception_when_field_is_empty() {
        // given
        final OfferFetcher offerFetcher = new InMemoryOfferFetcher(Collections.emptyList());
        OfferFacade offerFacade = OfferFacadeConfiguration.createForTest(offerRepository, offerFetcher);
        OfferRequestDto offerRequestDto = OfferRequestDto.builder()
                .jobTitle(null)
                .companyName("Assecco")
                .salary(6000)
                .build();

        // when
        Throwable exception = catchThrowable(() -> offerFacade.saveOffer(offerRequestDto));

        // then
        assertThat(exception).isInstanceOf(OfferNotValidException.class)
                .hasMessage("Job title cannot be null or empty");
    }

    @Test
    public void should_add_4_offers_when_there_are_no_offers_in_database() {
        // given
        final OfferFetcher offerFetcher = new InMemoryOfferFetcher(List.of(
                new JobOfferResponse("id1", "abc", 5000, "1"),
                new JobOfferResponse("id2", "abc", 5000, "2"),
                new JobOfferResponse("id3", "abc", 5000, "3"),
                new JobOfferResponse("id4", "abc", 5000, "4")
                ));
        OfferFacade offerFacade = OfferFacadeConfiguration.createForTest(offerRepository, offerFetcher);

        // when
        List<OfferResponseDto> offers = offerFacade.fetchAndSaveAllOffersIfNotExists();

        // then
        assertThat(offers).hasSize(4);
        assertThat(offers.get(0).jobTitle()).isEqualTo("id1");
        assertThat(offers.get(1).jobTitle()).isEqualTo("id2");
        assertThat(offers.get(2).jobTitle()).isEqualTo("id3");
        assertThat(offers.get(3).jobTitle()).isEqualTo("id4");
    }

    @Test
    public void should_find_offer_by_id_when_offer_was_saved() {
        // given
        final OfferFetcher offerFetcher = new InMemoryOfferFetcher(Collections.emptyList());
        OfferFacade offerFacade = OfferFacadeConfiguration.createForTest(offerRepository, offerFetcher);
        OfferRequestDto offerRequestDto = OfferRequestDto.builder()
                .jobTitle("Junior Java Developer")
                .companyName("Assecco")
                .salary(6000)
                .url("asasasd")
                .build();
        offerFacade.saveOffer(offerRequestDto);

        // when
        OfferResponseDto offerDto = offerFacade.retrieveOfferById(0L);

        // then
        assertThat(offerDto.id()).isEqualTo(0L);
        assertThat(offerDto.url()).isNotNull();
        assertThat(offerDto.jobTitle()).isEqualTo("Junior Java Developer");
        assertThat(offerDto.companyName()).isEqualTo("Assecco");
        assertThat(offerDto.salary()).isEqualTo(6000);
    }

    @Test
    public void should_throw_offer_not_found_exception_when_offer_not_found() {
        // given
        final OfferFetcher offerFetcher = new InMemoryOfferFetcher(Collections.emptyList());
        OfferFacade offerFacade = OfferFacadeConfiguration.createForTest(offerRepository, offerFetcher);
        assertThat(offerFacade.findAllOffers()).isEmpty();

        // when
        Throwable throwable = catchThrowable(() -> offerFacade.retrieveOfferById(0L));

        // then
        assertThat(throwable).isInstanceOf(OfferNotFoundException.class)
                .hasMessage("Offer not found");
    }

    @Test
    public void should_save_only_2_offers_when_repository_had_4_added_with_offer_urls() {
        // given
        final OfferFetcher offerFetcher = new InMemoryOfferFetcher(List.of(
                new JobOfferResponse("id1", "abc", 5000, "1"),
                new JobOfferResponse("id2", "abc", 5000, "2"),
                new JobOfferResponse("id3", "abc", 5000, "3"),
                new JobOfferResponse("id4", "abc", 5000, "4"),
                new JobOfferResponse("id4", "abc", 5000, "randomurl.pl/2"),
                new JobOfferResponse("id4", "abc", 5000, "randomurl.pl/3")
                ));
        OfferFacade offerFacade = OfferFacadeConfiguration.createForTest(offerRepository, offerFetcher);
        offerFacade.saveOffer(new OfferRequestDto("id1", "abc", 5000, "1"));
        offerFacade.saveOffer(new OfferRequestDto("id1", "abc", 5000, "2"));
        offerFacade.saveOffer(new OfferRequestDto("id1", "abc", 5000, "3"));
        offerFacade.saveOffer(new OfferRequestDto("id1", "abc", 5000, "4"));

        // when
        List<OfferResponseDto> offers = offerFacade.fetchAndSaveAllOffersIfNotExists();

        // then
        assertThat(offers).hasSize(2);
        assertThat(offers).extracting(OfferResponseDto::url)
                .containsExactlyInAnyOrder("randomurl.pl/2", "randomurl.pl/3");
    }
}
