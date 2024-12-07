package id.lariss.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import id.lariss.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CaseSizeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CaseSizeDTO.class);
        CaseSizeDTO caseSizeDTO1 = new CaseSizeDTO();
        caseSizeDTO1.setId(1L);
        CaseSizeDTO caseSizeDTO2 = new CaseSizeDTO();
        assertThat(caseSizeDTO1).isNotEqualTo(caseSizeDTO2);
        caseSizeDTO2.setId(caseSizeDTO1.getId());
        assertThat(caseSizeDTO1).isEqualTo(caseSizeDTO2);
        caseSizeDTO2.setId(2L);
        assertThat(caseSizeDTO1).isNotEqualTo(caseSizeDTO2);
        caseSizeDTO1.setId(null);
        assertThat(caseSizeDTO1).isNotEqualTo(caseSizeDTO2);
    }
}
