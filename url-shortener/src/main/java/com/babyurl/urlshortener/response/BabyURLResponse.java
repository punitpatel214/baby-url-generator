package com.babyurl.urlshortener.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;

@Builder
@JsonDeserialize(builder = BabyURLResponse.BabyURLResponseBuilder.class)
@Getter
public class BabyURLResponse {
    @JsonPOJOBuilder(withPrefix = "")
    public static class BabyURLResponseBuilder{}

    private final String url;
    private final String babyURL;
}
