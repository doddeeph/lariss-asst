package id.lariss.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class StrapSizeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static StrapSize getStrapSizeSample1() {
        return new StrapSize().id(1L).name("name1").value("value1");
    }

    public static StrapSize getStrapSizeSample2() {
        return new StrapSize().id(2L).name("name2").value("value2");
    }

    public static StrapSize getStrapSizeRandomSampleGenerator() {
        return new StrapSize().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).value(UUID.randomUUID().toString());
    }
}
