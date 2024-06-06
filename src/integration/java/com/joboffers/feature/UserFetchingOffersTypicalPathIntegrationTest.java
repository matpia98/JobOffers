package com.joboffers.feature;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.joboffers.BaseIntegrationTest;
import com.joboffers.SampleJobOfferResponse;
import com.joboffers.domain.offer.OfferFacade;
import com.joboffers.domain.offer.dto.OfferResponseDto;
import com.joboffers.infrastructure.offer.scheduler.OfferFetcherScheduler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class UserFetchingOffersTypicalPathIntegrationTest extends BaseIntegrationTest implements SampleJobOfferResponse {

    @Container
    public static final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));
    @DynamicPropertySource
    public static void propertyOverride(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    OfferFacade offerFacade;

    @Autowired
    OfferFetcherScheduler scheduler;

    @Test
    public void should_scheduler_fetch_offers_from_external_http_server_and_user_should_see_them() throws Exception {
        // step 1: there are no offers in external HTTP server (http://ec2-3-120-147-150.eu-central-1.compute.amazonaws.com:5057/offers)
        // given, when, then
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json;charset=UTF-8")
                        .withBody(bodyWithZeroOffersJson())));


        // step 2: scheduler ran 1st time and made GET to external server and system added 0 offers to database
        // given
        scheduler.fetchAndSaveAllOffersIfNotExists();

        // when
        List<OfferResponseDto> allOffers = offerFacade.findAllOffers();

        // then
        assertThat(allOffers).hasSize(0);


        //   step 3: user tried to get JWT token by requesting POST /token with username=someUser, password=somePassword and system returned UNAUTHORIZED(401)
        //   step 4: user made GET /offers with no jwt token and system returned UNAUTHORIZED(401)
        //   step 5: user made POST /register with username=someUser, password=somePassword and system registered user with status OK(200)
        //   step 6: user tried to get JWT token by requesting POST /token with username=someUser, password=somePassword and system returned OK(200) and jwttoken=AAAA.BBBB.CCC
        //   step 7: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 0 offers
        // given
        String offersUrl = "/offers";

        // when
        ResultActions perform = mockMvc.perform(get(offersUrl)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        MvcResult mvcResult = perform.andExpect(status().isOk()).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        List<OfferResponseDto> offerResponseDtoList = objectMapper.readValue(json, new TypeReference<>() {
        });
        assertThat(offerResponseDtoList).isEmpty();

        //   step 8: there are 2 new offers in external HTTP server
        // given, when, then
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json;charset=UTF-8")
                        .withBody(bodyWithTwoOffersJson())));

        //   step 9: scheduler ran 2nd time and made GET to external server and system added 2 new offers with ids: 1000 and 2000 to database
        // given, when
        List<OfferResponseDto> fetchTwoNewOffers = scheduler.fetchAndSaveAllOffersIfNotExists();

        // then
        assertThat(fetchTwoNewOffers).hasSize(2);

        //   step 10: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 2 offers with ids: 1000 and 2000
        // given, when
        ResultActions performGetOffersWithTwoOffersAdded = mockMvc.perform(get("/offers"));

        // then
        MvcResult performGetWithTwoOffersAddedMvcResult = performGetOffersWithTwoOffersAdded.andExpect(status().isOk()).andReturn();
        String jsonWithTwoOffers = performGetWithTwoOffersAddedMvcResult.getResponse().getContentAsString();
        List<OfferResponseDto> twoOffers = objectMapper.readValue(jsonWithTwoOffers, new TypeReference<>() {
        });
        assertThat(twoOffers).hasSize(2);
        OfferResponseDto expectedFirstOffer = fetchTwoNewOffers.get(0);
        OfferResponseDto expectedSecondOffer = fetchTwoNewOffers.get(1);
        assertThat(twoOffers).containsExactlyInAnyOrder(
                new OfferResponseDto(
                        expectedFirstOffer.id(),
                        expectedFirstOffer.url(),
                        expectedFirstOffer.jobTitle(),
                        expectedFirstOffer.companyName(),
                        expectedFirstOffer.salary()),
                new OfferResponseDto(
                        expectedSecondOffer.id(),
                        expectedSecondOffer.url(),
                        expectedSecondOffer.jobTitle(),
                        expectedSecondOffer.companyName(),
                        expectedSecondOffer.salary()
                ));

        //   step 11: user made GET /offers/9999 and system returned NOT_FOUND(404) with message “Offer with id 9999 not found”
        // given, when
        ResultActions performGetOfferWithNotExistingId = mockMvc.perform(get("/offers/9999"));

        // then
        performGetOfferWithNotExistingId.andExpect(status().isNotFound())
                .andExpect(content().json(
                        """
                                {
                                "message": "Offer with id 9999 not found",
                                "status": "NOT_FOUND"
                                }
                                """.trim()
                ));

        //   step 12: user made GET /offers/1000 and system returned OK(200) with offer
        // given
        String offerWithExistingIdUrl = "/offers/" + expectedFirstOffer.id();

        // when
        ResultActions performGetOfferWithExistingId = mockMvc.perform(get(offerWithExistingIdUrl)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        MvcResult performGetOfferWithExistingIdMvcResult = performGetOfferWithExistingId.andExpect(status().isOk()).andReturn();
        String jsonGetOfferWithExistingId = performGetOfferWithExistingIdMvcResult.getResponse().getContentAsString();
        OfferResponseDto existingOffer = objectMapper.readValue(jsonGetOfferWithExistingId, OfferResponseDto.class);
        assertThat(existingOffer).isEqualTo(
                new OfferResponseDto(
                        expectedFirstOffer.id(),
                        expectedFirstOffer.url(),
                        expectedFirstOffer.jobTitle(),
                        expectedFirstOffer.companyName(),
                        expectedFirstOffer.salary())
        );


        //   step 13: there are 2 new offers in external HTTP server
        // given, when, then
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json;charset=UTF-8")
                        .withBody(bodyWithFourOffersJson())));
        //   step 14: scheduler ran 3rd time and made GET to external server and system added 2 new offers with ids: 3000 and 4000 to database
        // given, when
        List<OfferResponseDto> newTwoOffersFetchedWithFourInAll = scheduler.fetchAndSaveAllOffersIfNotExists();
        // then
        assertThat(newTwoOffersFetchedWithFourInAll).hasSize(2);
        assertThat(offerFacade.findAllOffers()).hasSize(4);
        //   step 15: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 4 offers with ids: 1000,2000, 3000 and 4000
        // given, when
        ResultActions performGetWithFourOffers = mockMvc.perform(get("/offers").contentType(MediaType.APPLICATION_JSON));
        // then
        MvcResult performGetWithFourOffersMvcResult = performGetWithFourOffers.andExpect(status().isOk()).andReturn();
        String jsonFourOffers = performGetWithFourOffersMvcResult.getResponse().getContentAsString();
        List<OfferResponseDto> fourOffersResponse = objectMapper.readValue(jsonFourOffers, new TypeReference<List<OfferResponseDto>>() {
        });
        assertThat(fourOffersResponse).hasSize(4);
        OfferResponseDto expectedThirdOffer = newTwoOffersFetchedWithFourInAll.get(0);
        OfferResponseDto expectedFourthOffer = newTwoOffersFetchedWithFourInAll.get(1);

        assertThat(fourOffersResponse).containsExactlyInAnyOrder(
                new OfferResponseDto(
                        expectedFirstOffer.id(),
                        expectedFirstOffer.url(),
                        expectedFirstOffer.jobTitle(),
                        expectedFirstOffer.companyName(),
                        expectedFirstOffer.salary()),
                new OfferResponseDto(
                        expectedSecondOffer.id(),
                        expectedSecondOffer.url(),
                        expectedSecondOffer.jobTitle(),
                        expectedSecondOffer.companyName(),
                        expectedSecondOffer.salary()),
                new OfferResponseDto(
                        expectedThirdOffer.id(),
                        expectedThirdOffer.url(),
                        expectedThirdOffer.jobTitle(),
                        expectedThirdOffer.companyName(),
                        expectedThirdOffer.salary()),
                new OfferResponseDto(
                        expectedFourthOffer.id(),
                        expectedFourthOffer.url(),
                        expectedFourthOffer.jobTitle(),
                        expectedFourthOffer.companyName(),
                        expectedFourthOffer.salary())
        );

        //   step 16: user made POST /offers with header "Authorization: Bearer AAAA.BBBB.CCC" and offer as body and system returned CREATED(201) with saved offer
        // given
        // when
        String jsonPostOffer = mockMvc.perform(post("/offers")
                        .content("""
                                {
                                  "jobTitle": "Junior Java Developer",
                                  "companyName": "Junior Java Ready",
                                  "salary": "5000",
                                  "url": "randomurl.com/adadada"
                                }
                                """)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated()).andReturn()
                .getResponse().getContentAsString();

        // then
        OfferResponseDto responsePostOffer = objectMapper.readValue(jsonPostOffer, OfferResponseDto.class);
        assertAll(
                () -> assertThat(responsePostOffer.id()).isNotEmpty(),
                () -> assertThat(responsePostOffer.url()).isEqualTo("randomurl.com/adadada"),
                () -> assertThat(responsePostOffer.jobTitle()).isEqualTo("Junior Java Developer"),
                () -> assertThat(responsePostOffer.companyName()).isEqualTo("Junior Java Ready"),
                () -> assertThat(responsePostOffer.salary()).isEqualTo("5000")
        );
        //   step 17: user made GET /offers with header "Authorization: Bearer AAAA.BBBB.CCC" and system returned OK(200) with 1 offer
        // given, when
        ResultActions performGetOffers = mockMvc.perform(get("/offers")
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        // then
        String oneOfferJson = performGetOffers.andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<OfferResponseDto> parsedJsonWithOneOffer = objectMapper.readValue(oneOfferJson, new TypeReference<>() {
        });
        assertThat(parsedJsonWithOneOffer).hasSize(5);
        assertThat(parsedJsonWithOneOffer.stream().map(OfferResponseDto::id)).contains(responsePostOffer.id());
    }
}
