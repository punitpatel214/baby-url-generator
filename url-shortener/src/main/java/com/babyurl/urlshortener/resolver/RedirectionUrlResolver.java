package com.babyurl.urlshortener.resolver;

import io.micronaut.http.HttpRequest;

public interface RedirectionUrlResolver {
    <B> String resolve(HttpRequest<B> httpRequest);
}
