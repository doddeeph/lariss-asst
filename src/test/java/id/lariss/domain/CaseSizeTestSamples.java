package id.lariss.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CaseSizeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CaseSize getCaseSizeSample1() {
        return new CaseSize().id(1L).name("name1").value("value1");
    }

    public static CaseSize getCaseSizeSample2() {
        return new CaseSize().id(2L).name("name2").value("value2");
    }

    public static CaseSize getCaseSizeRandomSampleGenerator() {
        return new CaseSize().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).value(UUID.randomUUID().toString());
    }
}
