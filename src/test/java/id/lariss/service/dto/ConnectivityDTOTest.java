package id.lariss.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import id.lariss.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConnectivityDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConnectivityDTO.class);
        ConnectivityDTO connectivityDTO1 = new ConnectivityDTO();
        connectivityDTO1.setId(1L);
        ConnectivityDTO connectivityDTO2 = new ConnectivityDTO();
        assertThat(connectivityDTO1).isNotEqualTo(connectivityDTO2);
        connectivityDTO2.setId(connectivityDTO1.getId());
        assertThat(connectivityDTO1).isEqualTo(connectivityDTO2);
        connectivityDTO2.setId(2L);
        assertThat(connectivityDTO1).isNotEqualTo(connectivityDTO2);
        connectivityDTO1.setId(null);
        assertThat(connectivityDTO1).isNotEqualTo(connectivityDTO2);
    }
}
