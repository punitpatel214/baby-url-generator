package com.babyurl.urlshortener.resolver;

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.annotation.Value;
import io.micronaut.http.HttpRequest;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
@Requires(property="redirect.domain")
public class RedirectionUrlConfigResolver implements RedirectionUrlResolver  {
    private final String redirectUrl;

    @Inject
    public RedirectionUrlConfigResolver(@Value("${redirect.domain}") String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    @Override
    public <B> String resolve(HttpRequest<B> httpRequest) {
        return redirectUrl;
    }
}
