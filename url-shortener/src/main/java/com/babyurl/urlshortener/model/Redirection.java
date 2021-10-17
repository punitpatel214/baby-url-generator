package com.babyurl.urlshortener.model;

import java.time.LocalDateTime;

public class Redirection {
    private final String key;
    private final String url;
    private final LocalDateTime expiryTime;

    public Redirection(String key, String url, LocalDateTime expiryTime) {
        this.key = key;
        this.url = url;
        this.expiryTime = expiryTime;
    }

    public String getKey() {
        return key;
    }

    public String getUrl() {
        return url;
    }

    public LocalDateTime getExpiryTime() {
        return expiryTime;
    }
}
