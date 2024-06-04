package com.joboffers.controller.error;

import com.joboffers.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OfferUrlDuplicateErrorIntegrationTest extends BaseIntegrationTest {

    @Test
    public void should_return_409_conflict_when_added_second_offer_with_same_url() throws Exception {
        // step 1:
        // given, when
        ResultActions perform = mockMvc.perform(post("/offers")
                .content("""
                        {
                          "jobTitle": "Junior Java Developer",
                          "companyName": "Junior Java Ready",
                          "salary": "5000",
                          "url": "https://randomurl.com/1234"
                        }
                        """)
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        // then
        perform.andExpect(status().isCreated());

        // step 2:
        ResultActions perform2 = mockMvc.perform(post("/offers")
                .content("""
                        {
                          "jobTitle": "Junior Java Developer2",
                          "companyName": "Junior Java Ready2",
                          "salary": "6000",
                          "url": "https://randomurl.com/1234"
                        }
                        """)
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        // then
        perform2.andExpect(status().isConflict());

    }
}
