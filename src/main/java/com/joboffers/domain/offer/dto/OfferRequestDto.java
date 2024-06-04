package com.joboffers.domain.offer.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record OfferRequestDto(

        @NotEmpty(message = "{jobTitle.not.empty}")
        @NotNull(message = "{jobTitle.not.null}")
        String jobTitle,

        @NotEmpty(message = "{companyName.not.empty}")
        @NotNull(message = "{companyName.not.null}")
        String companyName,

        @NotEmpty(message = "{salary.not.empty}")
        @NotNull(message = "{salary.not.null}")
        String salary,

        @NotEmpty(message = "{url.not.empty}")
        @NotNull(message = "{url.not.null}")
        String url
) {
}
