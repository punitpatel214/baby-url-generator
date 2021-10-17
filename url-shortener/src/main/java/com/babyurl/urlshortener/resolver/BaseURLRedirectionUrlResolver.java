package com.babyurl.urlshortener.resolver;

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.annotation.Value;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.server.util.HttpHostResolver;
import jakarta.inject.Singleton;

@Singleton
@Requires(missingProperty = "redirect.domain")
public class BaseURLRedirectionUrlResolver implements RedirectionUrlResolver {
    private HttpHostResolver httpHostResolver;
    private String contextPath;

    public BaseURLRedirectionUrlResolver(HttpHostResolver httpHostResolver,
                                         @Value("${micronaut.server.context-path:}") String contextPath) {
        this.httpHostResolver = httpHostResolver;
        this.contextPath = contextPath;
    }

    @Override
    public <B> String resolve(HttpRequest<B> httpRequest) {
        return httpHostResolver.resolve(httpRequest) + contextPath + "/";
    }
}
