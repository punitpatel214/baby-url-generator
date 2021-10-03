package com.babyurl;

import com.babyurl.repository.casandra.CassandraKeyGeneratorRepository;
import com.babyurl.repository.casandra.RandomIdGenerator;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import jakarta.inject.Inject;

@MicronautTest
class BabyUrlGeneratorTest {

    @Inject
    EmbeddedApplication<?> application;

    @Inject
    CassandraKeyGeneratorRepository cassandraKeyGeneratorRepository;

    @Test
    void testItWorks() {
        Assertions.assertTrue(application.isRunning());
        RandomIdGenerator randomIdGenerator = new RandomIdGenerator(6);
        int failureAttempt = 0;
        while (failureAttempt < 100) {
            boolean isInsert = cassandraKeyGeneratorRepository.insertKey(randomIdGenerator.generateId());
            if (!isInsert) {
                failureAttempt++;
                continue;
            }
            failureAttempt = 0;

        }
    }

}
