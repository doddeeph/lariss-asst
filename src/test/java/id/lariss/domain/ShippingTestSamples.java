package id.lariss.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ShippingTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Shipping getShippingSample1() {
        return new Shipping()
            .id(1L)
            .streetAddress("streetAddress1")
            .city("city1")
            .state("state1")
            .postalCode("postalCode1")
            .country("country1");
    }

    public static Shipping getShippingSample2() {
        return new Shipping()
            .id(2L)
            .streetAddress("streetAddress2")
            .city("city2")
            .state("state2")
            .postalCode("postalCode2")
            .country("country2");
    }

    public static Shipping getShippingRandomSampleGenerator() {
        return new Shipping()
            .id(longCount.incrementAndGet())
            .streetAddress(UUID.randomUUID().toString())
            .city(UUID.randomUUID().toString())
            .state(UUID.randomUUID().toString())
            .postalCode(UUID.randomUUID().toString())
            .country(UUID.randomUUID().toString());
    }
}
