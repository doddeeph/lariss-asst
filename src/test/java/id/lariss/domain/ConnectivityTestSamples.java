package id.lariss.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ConnectivityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Connectivity getConnectivitySample1() {
        return new Connectivity().id(1L).name("name1").value("value1");
    }

    public static Connectivity getConnectivitySample2() {
        return new Connectivity().id(2L).name("name2").value("value2");
    }

    public static Connectivity getConnectivityRandomSampleGenerator() {
        return new Connectivity().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).value(UUID.randomUUID().toString());
    }
}
