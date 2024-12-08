package id.lariss.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import id.lariss.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ScreenDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ScreenDTO.class);
        ScreenDTO screenDTO1 = new ScreenDTO();
        screenDTO1.setId(1L);
        ScreenDTO screenDTO2 = new ScreenDTO();
        assertThat(screenDTO1).isNotEqualTo(screenDTO2);
        screenDTO2.setId(screenDTO1.getId());
        assertThat(screenDTO1).isEqualTo(screenDTO2);
        screenDTO2.setId(2L);
        assertThat(screenDTO1).isNotEqualTo(screenDTO2);
        screenDTO1.setId(null);
        assertThat(screenDTO1).isNotEqualTo(screenDTO2);
    }
}
