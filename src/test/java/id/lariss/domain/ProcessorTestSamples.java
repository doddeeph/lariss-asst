package id.lariss.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProcessorTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Processor getProcessorSample1() {
        return new Processor().id(1L).name("name1").value("value1");
    }

    public static Processor getProcessorSample2() {
        return new Processor().id(2L).name("name2").value("value2");
    }

    public static Processor getProcessorRandomSampleGenerator() {
        return new Processor().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).value(UUID.randomUUID().toString());
    }
}
