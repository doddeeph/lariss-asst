package id.lariss.domain;

import static id.lariss.domain.ColorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import id.lariss.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ColorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Color.class);
        Color color1 = getColorSample1();
        Color color2 = new Color();
        assertThat(color1).isNotEqualTo(color2);

        color2.setId(color1.getId());
        assertThat(color1).isEqualTo(color2);

        color2 = getColorSample2();
        assertThat(color1).isNotEqualTo(color2);
    }
}
