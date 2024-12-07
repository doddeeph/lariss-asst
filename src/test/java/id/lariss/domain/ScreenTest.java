package id.lariss.domain;

import static id.lariss.domain.ScreenTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import id.lariss.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ScreenTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Screen.class);
        Screen screen1 = getScreenSample1();
        Screen screen2 = new Screen();
        assertThat(screen1).isNotEqualTo(screen2);

        screen2.setId(screen1.getId());
        assertThat(screen1).isEqualTo(screen2);

        screen2 = getScreenSample2();
        assertThat(screen1).isNotEqualTo(screen2);
    }
}
