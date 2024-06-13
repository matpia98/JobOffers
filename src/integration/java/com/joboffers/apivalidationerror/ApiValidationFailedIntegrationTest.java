package com.joboffers.apivalidationerror;

import com.joboffers.BaseIntegrationTest;
import com.joboffers.infrastructure.apivalidation.ApiValidationErrorDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ApiValidationFailedIntegrationTest extends BaseIntegrationTest {

    @Test
    @WithMockUser
    public void should_return_400_bad_request_and_validation_message_when_empty_and_null_in_offer_save_request() throws Exception {
        // given
        ResultActions perform = mockMvc.perform(post("/offers")
                .content("""
                        {
                          "jobTitle": "",
                          "companyName": "",
                          "salary": ""
                        }
                        """.trim()
                ).contentType(MediaType.APPLICATION_JSON));

        // then
        MvcResult mvcResult = perform.andExpect(status().isBadRequest()).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        ApiValidationErrorDto result = objectMapper.readValue(json, ApiValidationErrorDto.class);
        assertThat(result.messages()).containsExactlyInAnyOrder(
                "salary must not be empty",
                "companyName must not be empty",
                "jobTitle must not be empty",
                "url must not be null",
                "url must not be empty");
    }

    @Test
    @WithMockUser
    public void should_return_400_bad_request_and_validation_message_when_request_does_not_have_any_values() throws Exception {
        // given
        ResultActions perform = mockMvc.perform(post("/offers")
                .content("""
                        {}
                        """.trim()
                ).contentType(MediaType.APPLICATION_JSON));

        // then
        MvcResult mvcResult = perform.andExpect(status().isBadRequest()).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        ApiValidationErrorDto result = objectMapper.readValue(json, ApiValidationErrorDto.class);
        assertThat(result.messages()).containsExactlyInAnyOrder(
                "salary must not be null",
                "salary must not be empty",
                "jobTitle must not be null",
                "jobTitle must not be empty",
                "companyName must not be null",
                "companyName must not be empty",
                "url must not be null",
                "url must not be empty");
    }
}
