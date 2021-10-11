package com.babyurl.urlshortener.model;

import java.time.Duration;
import java.time.LocalDateTime;

public class ShortenURLData {
    public final String key;
    public final String originalURL;
    public final LocalDateTime createTime;
    public final LocalDateTime expiryTime;


    public ShortenURLData(String key, String originalURL, Duration expiryDuration) {
        this.key = key;
        this.originalURL = originalURL;
        createTime = LocalDateTime.now();
        expiryTime = createTime.plusSeconds(expiryDuration.getSeconds());
    }
}
