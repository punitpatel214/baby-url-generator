package com.babyurl.urlshortener.api;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller
public class LongURLTestController {

    @Get("/test/long/url")
    public String testLongUrlRedirection() {
        return "redirectSuccess";
    }
}
