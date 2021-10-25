package com.babyurl.urlshortener.api;

import com.babyurl.urlshortener.model.Redirection;
import com.babyurl.urlshortener.service.RedirectionService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import static io.micronaut.http.HttpResponse.*;

@Controller
public class RedirectionAPI {
    private final RedirectionService redirectionService;

    public RedirectionAPI(RedirectionService redirectionService) {
        this.redirectionService = redirectionService;
    }

    @Get("/{key}")
    public HttpResponse<Object> redirectURL(String key) {
        return redirectionService.findRedirection(key)
            .map(this::redirectionResponse)
            .orElse(notFound());
    }

    private HttpResponse<Object> redirectionResponse(Redirection redirection) {
        if (redirection.isExpired()) {
            return status(HttpStatus.GONE).body("URL is expired");
        }
        return redirect(redirection.getURI())
                .header("Cache-Control", "no-store");
    }

}
