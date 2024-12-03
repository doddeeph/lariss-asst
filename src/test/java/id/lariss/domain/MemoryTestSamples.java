package id.lariss.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MemoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Memory getMemorySample1() {
        return new Memory().id(1L).name("name1").value("value1");
    }

    public static Memory getMemorySample2() {
        return new Memory().id(2L).name("name2").value("value2");
    }

    public static Memory getMemoryRandomSampleGenerator() {
        return new Memory().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).value(UUID.randomUUID().toString());
    }
}
