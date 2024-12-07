package id.lariss.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ScreenTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Screen getScreenSample1() {
        return new Screen().id(1L).name("name1").value("value1");
    }

    public static Screen getScreenSample2() {
        return new Screen().id(2L).name("name2").value("value2");
    }

    public static Screen getScreenRandomSampleGenerator() {
        return new Screen().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).value(UUID.randomUUID().toString());
    }
}
