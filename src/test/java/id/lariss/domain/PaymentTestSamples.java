package id.lariss.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PaymentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Payment getPaymentSample1() {
        return new Payment().id(1L).cardNumber("cardNumber1").cvv("cvv1");
    }

    public static Payment getPaymentSample2() {
        return new Payment().id(2L).cardNumber("cardNumber2").cvv("cvv2");
    }

    public static Payment getPaymentRandomSampleGenerator() {
        return new Payment().id(longCount.incrementAndGet()).cardNumber(UUID.randomUUID().toString()).cvv(UUID.randomUUID().toString());
    }
}
