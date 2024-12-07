package id.lariss.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import id.lariss.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StrapColorDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StrapColorDTO.class);
        StrapColorDTO strapColorDTO1 = new StrapColorDTO();
        strapColorDTO1.setId(1L);
        StrapColorDTO strapColorDTO2 = new StrapColorDTO();
        assertThat(strapColorDTO1).isNotEqualTo(strapColorDTO2);
        strapColorDTO2.setId(strapColorDTO1.getId());
        assertThat(strapColorDTO1).isEqualTo(strapColorDTO2);
        strapColorDTO2.setId(2L);
        assertThat(strapColorDTO1).isNotEqualTo(strapColorDTO2);
        strapColorDTO1.setId(null);
        assertThat(strapColorDTO1).isNotEqualTo(strapColorDTO2);
    }
}
