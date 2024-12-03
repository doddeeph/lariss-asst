package id.lariss.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import id.lariss.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProcessorDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProcessorDTO.class);
        ProcessorDTO processorDTO1 = new ProcessorDTO();
        processorDTO1.setId(1L);
        ProcessorDTO processorDTO2 = new ProcessorDTO();
        assertThat(processorDTO1).isNotEqualTo(processorDTO2);
        processorDTO2.setId(processorDTO1.getId());
        assertThat(processorDTO1).isEqualTo(processorDTO2);
        processorDTO2.setId(2L);
        assertThat(processorDTO1).isNotEqualTo(processorDTO2);
        processorDTO1.setId(null);
        assertThat(processorDTO1).isNotEqualTo(processorDTO2);
    }
}
