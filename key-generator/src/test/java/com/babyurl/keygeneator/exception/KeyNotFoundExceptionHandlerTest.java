package com.babyurl.keygeneator.exception;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class KeyNotFoundExceptionHandlerTest {

    @Test
    void shouldReturnHttpResponseWithNoContent() {
        KeyNotFoundExceptionHandler keyNotFoundExceptionHandler = new KeyNotFoundExceptionHandler();
        HttpResponse<?> httpResponse = keyNotFoundExceptionHandler.handle(Mockito.mock(HttpRequest.class), new KeyNotFoundException());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, httpResponse.getStatus());
    }
}