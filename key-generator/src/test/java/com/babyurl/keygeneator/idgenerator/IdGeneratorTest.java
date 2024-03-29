package com.babyurl.keygeneator.idgenerator;

import com.babyurl.keygeneator.repository.casandra.CassandraKeyGeneratorRepository;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/*
* Sample Test class to generate keys offline
*
*/
@MicronautTest
@Disabled("POC for generate random keys")
class IdGeneratorTest {

    @Inject
    CassandraKeyGeneratorRepository cassandraKeyGeneratorRepository;

    private final RandomIdGenerator randomIdGenerator = new RandomIdGenerator(6);


    @Test
    void generateKeys() {
        int failureAttempt = 0;
        while (failureAttempt < 100) {
            boolean isInsert = cassandraKeyGeneratorRepository.insertKey(randomIdGenerator.generateId());
            if (!isInsert) {
                failureAttempt++;
                continue;
            }
            failureAttempt = 0;
        }
        assertNotNull(cassandraKeyGeneratorRepository.getKey());
    }

}
