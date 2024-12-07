package id.lariss.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import id.lariss.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StrapSizeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StrapSizeDTO.class);
        StrapSizeDTO strapSizeDTO1 = new StrapSizeDTO();
        strapSizeDTO1.setId(1L);
        StrapSizeDTO strapSizeDTO2 = new StrapSizeDTO();
        assertThat(strapSizeDTO1).isNotEqualTo(strapSizeDTO2);
        strapSizeDTO2.setId(strapSizeDTO1.getId());
        assertThat(strapSizeDTO1).isEqualTo(strapSizeDTO2);
        strapSizeDTO2.setId(2L);
        assertThat(strapSizeDTO1).isNotEqualTo(strapSizeDTO2);
        strapSizeDTO1.setId(null);
        assertThat(strapSizeDTO1).isNotEqualTo(strapSizeDTO2);
    }
}
