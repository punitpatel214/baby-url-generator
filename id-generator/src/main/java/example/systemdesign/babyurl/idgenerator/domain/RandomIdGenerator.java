package example.systemdesign.babyurl.idgenerator.domain;

import javax.inject.Singleton;
import java.util.Random;
import java.util.stream.IntStream;

@Singleton
public class RandomIdGenerator implements IdGenerator {
    private static final char[] ALPHA_NUMERIC = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static final Random RANDOM = new Random();

    private int idLength = 6;

    public RandomIdGenerator() {
    }

    public RandomIdGenerator(int idLength) {
        this.idLength = idLength;
    }

    @Override
    public String generateId() {
        StringBuilder builder = new StringBuilder(idLength);
        IntStream.range(0, idLength)
                .mapToObj(index -> ALPHA_NUMERIC[RANDOM.nextInt(ALPHA_NUMERIC.length)])
                .forEach(builder::append);

        return builder.toString();
    }

}
