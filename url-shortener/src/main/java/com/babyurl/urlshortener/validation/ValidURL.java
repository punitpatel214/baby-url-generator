package com.babyurl.urlshortener.validation;

import javax.validation.Constraint;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { })
public @interface ValidURL {

    String message() default "({validatedValue}) is not valid";

}