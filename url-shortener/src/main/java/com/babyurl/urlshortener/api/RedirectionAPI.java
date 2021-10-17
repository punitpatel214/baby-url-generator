package com.babyurl.urlshortener.api;

import com.babyurl.urlshortener.model.Redirection;
import com.babyurl.urlshortener.service.RedirectionService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import java.net.URI;

@Controller
public class RedirectionAPI {
    private final RedirectionService redirectionService;

    public RedirectionAPI(RedirectionService redirectionService) {
        this.redirectionService = redirectionService;
    }

    @Get("/{key}")
    public HttpResponse<Object> redirect(String key) {
        return redirectionService.findRedirection(key)
            .map(Redirection::getUrl)
            .map(URI::create)
            .map(HttpResponse::redirect)
            .orElse(HttpResponse.notFound());
    }

}
