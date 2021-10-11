package com.babyurl.urlshortener.repositiry;

import com.babyurl.urlshortener.model.ShortenURLData;
import jakarta.inject.Singleton;

@Singleton
public class CassandraShortenURLRepository implements ShortenURLRepository {

    @Override
    public boolean save(ShortenURLData urlData) {
        return false;
    }
}
