package id.lariss.domain;

import static id.lariss.domain.ProcessorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import id.lariss.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProcessorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Processor.class);
        Processor processor1 = getProcessorSample1();
        Processor processor2 = new Processor();
        assertThat(processor1).isNotEqualTo(processor2);

        processor2.setId(processor1.getId());
        assertThat(processor1).isEqualTo(processor2);

        processor2 = getProcessorSample2();
        assertThat(processor1).isNotEqualTo(processor2);
    }
}
