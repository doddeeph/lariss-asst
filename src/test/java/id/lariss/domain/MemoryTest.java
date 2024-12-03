package id.lariss.domain;

import static id.lariss.domain.MemoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import id.lariss.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MemoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Memory.class);
        Memory memory1 = getMemorySample1();
        Memory memory2 = new Memory();
        assertThat(memory1).isNotEqualTo(memory2);

        memory2.setId(memory1.getId());
        assertThat(memory1).isEqualTo(memory2);

        memory2 = getMemorySample2();
        assertThat(memory1).isNotEqualTo(memory2);
    }
}
