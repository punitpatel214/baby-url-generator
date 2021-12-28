package com.babyurl.keygeneator.idgenerator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RandomIdGeneratorTest {

    @Test
    void generateRandomNumber() {
        RandomIdGenerator randomIdGenerator = new RandomIdGenerator(6);
        String id = randomIdGenerator.generateId();
        assertNotNull(id);
        assertEquals(6, id.length());
    }
}