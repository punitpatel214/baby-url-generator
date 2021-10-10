package com.babyurl.keygeneator.exception;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class KeyNotExceptionHandlerTest {

    @Test
    void shouldReturnHttpResponseWithNoContent() {
        KeyNotExceptionHandler keyNotExceptionHandler = new KeyNotExceptionHandler();
        HttpResponse<?> httpResponse = keyNotExceptionHandler.handle(Mockito.mock(HttpRequest.class), new KeyNotFoundException());
        assertEquals(HttpStatus.NO_CONTENT, httpResponse.getStatus());
    }
}