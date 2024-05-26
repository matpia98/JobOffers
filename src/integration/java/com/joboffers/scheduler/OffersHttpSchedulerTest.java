package com.joboffers.scheduler;

import com.joboffers.BaseIntegrationTest;
import com.joboffers.JobOffersSpringBootApplication;
import com.joboffers.domain.offer.OfferFetcher;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import java.time.Duration;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = JobOffersSpringBootApplication.class, properties = "scheduling.enabled=true")
public class OffersHttpSchedulerTest extends BaseIntegrationTest {

    @SpyBean
    OfferFetcher offerHttpClient;
    @Test
    public void should_run_http_client_offers_fetching_exactly_given_times() {
        await()
                .atMost(Duration.ofSeconds(10))
                .untilAsserted(() -> verify(offerHttpClient, times(10)).fetchOffers());
    }
}
