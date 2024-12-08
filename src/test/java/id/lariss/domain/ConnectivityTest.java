package id.lariss.domain;

import static id.lariss.domain.ConnectivityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import id.lariss.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConnectivityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Connectivity.class);
        Connectivity connectivity1 = getConnectivitySample1();
        Connectivity connectivity2 = new Connectivity();
        assertThat(connectivity1).isNotEqualTo(connectivity2);

        connectivity2.setId(connectivity1.getId());
        assertThat(connectivity1).isEqualTo(connectivity2);

        connectivity2 = getConnectivitySample2();
        assertThat(connectivity1).isNotEqualTo(connectivity2);
    }
}
