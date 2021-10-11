package com.babyurl.urlshortener.repositiry;

import com.babyurl.urlshortener.model.ShortenURLData;

public interface ShortenURLRepository {
    boolean save(ShortenURLData urlData);
}
