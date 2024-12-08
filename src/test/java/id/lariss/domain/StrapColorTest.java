package id.lariss.domain;

import static id.lariss.domain.StrapColorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import id.lariss.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StrapColorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StrapColor.class);
        StrapColor strapColor1 = getStrapColorSample1();
        StrapColor strapColor2 = new StrapColor();
        assertThat(strapColor1).isNotEqualTo(strapColor2);

        strapColor2.setId(strapColor1.getId());
        assertThat(strapColor1).isEqualTo(strapColor2);

        strapColor2 = getStrapColorSample2();
        assertThat(strapColor1).isNotEqualTo(strapColor2);
    }
}
