package id.lariss.domain;

import static id.lariss.domain.StrapSizeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import id.lariss.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StrapSizeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StrapSize.class);
        StrapSize strapSize1 = getStrapSizeSample1();
        StrapSize strapSize2 = new StrapSize();
        assertThat(strapSize1).isNotEqualTo(strapSize2);

        strapSize2.setId(strapSize1.getId());
        assertThat(strapSize1).isEqualTo(strapSize2);

        strapSize2 = getStrapSizeSample2();
        assertThat(strapSize1).isNotEqualTo(strapSize2);
    }
}
