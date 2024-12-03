package id.lariss.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProductDetailsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ProductDetails getProductDetailsSample1() {
        return new ProductDetails().id(1L).name("name1").thumbnail("thumbnail1");
    }

    public static ProductDetails getProductDetailsSample2() {
        return new ProductDetails().id(2L).name("name2").thumbnail("thumbnail2");
    }

    public static ProductDetails getProductDetailsRandomSampleGenerator() {
        return new ProductDetails()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .thumbnail(UUID.randomUUID().toString());
    }
}
