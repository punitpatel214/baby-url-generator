package com.babyurl.urlshortener.repositiry;

import com.babyurl.urlshortener.model.Redirection;
import com.babyurl.urlshortener.model.ShortenURLData;

import java.util.Optional;

public interface ShortenURLRepository {
    boolean save(ShortenURLData urlData);

    Optional<Redirection> find(String key);
}
