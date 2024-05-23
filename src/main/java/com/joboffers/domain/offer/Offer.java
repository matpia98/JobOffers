package com.joboffers.domain.offer;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Offer {
    private Long id;
    private String url;
    private String jobTitle;
    private String companyName;
    private String salary;
}
