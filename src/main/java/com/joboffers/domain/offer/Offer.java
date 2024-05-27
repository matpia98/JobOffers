package com.joboffers.domain.offer;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@Document(collection = "offers")
public class Offer {
    @Id
    private String id;
    @Field("position")
    private String position;
    @Field("company")
    private String companyName;
    @Field("salary")
    private String salary;
    @Field("url")
    @Indexed(unique = true)
    private String url;
}
