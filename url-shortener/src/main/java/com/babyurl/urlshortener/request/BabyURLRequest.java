package com.babyurl.urlshortener.request;

import com.babyurl.urlshortener.validation.ValidURL;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.micronaut.core.annotation.Introspected;
import lombok.Builder;
import lombok.Value;

@Value @Builder
@JsonDeserialize(builder = BabyURLRequest.BabyURLRequestBuilder.class)
@Introspected
public class BabyURLRequest {
    @JsonPOJOBuilder(withPrefix = "")
    public static class BabyURLRequestBuilder{}

    @ValidURL
    String url;
}
