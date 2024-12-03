package id.lariss.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import id.lariss.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DescriptionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DescriptionDTO.class);
        DescriptionDTO descriptionDTO1 = new DescriptionDTO();
        descriptionDTO1.setId(1L);
        DescriptionDTO descriptionDTO2 = new DescriptionDTO();
        assertThat(descriptionDTO1).isNotEqualTo(descriptionDTO2);
        descriptionDTO2.setId(descriptionDTO1.getId());
        assertThat(descriptionDTO1).isEqualTo(descriptionDTO2);
        descriptionDTO2.setId(2L);
        assertThat(descriptionDTO1).isNotEqualTo(descriptionDTO2);
        descriptionDTO1.setId(null);
        assertThat(descriptionDTO1).isNotEqualTo(descriptionDTO2);
    }
}
