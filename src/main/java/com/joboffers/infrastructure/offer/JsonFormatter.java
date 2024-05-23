package com.joboffers.infrastructure.offer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonFormatter {

    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public static String format(String json) throws JsonProcessingException {
        Object jsonObject = objectMapper.readValue(json, Object.class);
        return objectMapper.writeValueAsString(jsonObject);
    }

    public static String format(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }
}
