package id.lariss.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class StorageTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Storage getStorageSample1() {
        return new Storage().id(1L).name("name1").value("value1");
    }

    public static Storage getStorageSample2() {
        return new Storage().id(2L).name("name2").value("value2");
    }

    public static Storage getStorageRandomSampleGenerator() {
        return new Storage().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).value(UUID.randomUUID().toString());
    }
}
