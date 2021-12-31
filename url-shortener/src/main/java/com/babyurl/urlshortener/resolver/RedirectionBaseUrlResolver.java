package com.babyurl.urlshortener.resolver;

import io.micronaut.http.HttpRequest;

public interface RedirectionBaseUrlResolver {
    <B> String resolve(HttpRequest<B> httpRequest);
}
