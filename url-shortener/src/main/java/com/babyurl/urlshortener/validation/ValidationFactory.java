package com.babyurl.urlshortener.validation;

import io.micronaut.context.annotation.Factory;
import io.micronaut.validation.validator.constraints.ConstraintValidator;
import jakarta.inject.Singleton;

import java.net.URL;

@Factory
public class ValidationFactory {

    @Singleton
    ConstraintValidator<ValidURL, String> urlValidator() {
        return (value, annotationMetadata, context) ->
                value == null || isValidURL(value);
    }

    private boolean isValidURL(String originalURL) {
        try {
            new URL(originalURL).toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
