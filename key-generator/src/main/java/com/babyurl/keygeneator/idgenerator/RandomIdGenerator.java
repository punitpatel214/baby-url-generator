package com.babyurl.keygeneator.idgenerator;

import jakarta.inject.Singleton;

import java.util.Random;
import java.util.stream.IntStream;

@Singleton
public class RandomIdGenerator {
    private static final char[] ALPHA_NUMERIC = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private int idLength;

    public RandomIdGenerator(int idLength) {
        this.idLength = idLength;
    }

    public String generateId() {
        StringBuilder builder = new StringBuilder(idLength);
        IntStream.range(0, idLength)
                .mapToObj(index -> ALPHA_NUMERIC[new Random().nextInt(ALPHA_NUMERIC.length)])
                .forEach(builder::append);

        return builder.toString();
    }

}