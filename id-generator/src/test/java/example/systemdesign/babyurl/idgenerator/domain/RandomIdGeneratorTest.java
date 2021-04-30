package example.systemdesign.babyurl.idgenerator.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RandomIdGeneratorTest {

    @ParameterizedTest
    @ValueSource(ints = {5,6,7,8})
    void shouldGenerateRandomId(int length) {
        IdGenerator idGenerator = new RandomIdGenerator(length);
        String id = idGenerator.generateId();
        assertEquals(length, id.length());
    }
}