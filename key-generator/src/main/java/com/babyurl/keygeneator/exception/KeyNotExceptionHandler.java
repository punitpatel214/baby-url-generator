package com.babyurl.keygeneator.exception;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;

@Produces
@Singleton
@Requires(classes = {KeyNotFoundException.class, ExceptionHandler.class})
public class KeyNotExceptionHandler implements ExceptionHandler<KeyNotFoundException, HttpResponse<?>> {

    @Override
    public HttpResponse<?> handle(HttpRequest request, KeyNotFoundException exception) {
       return HttpResponse.noContent();
    }

}