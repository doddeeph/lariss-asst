package id.lariss.domain;

import static id.lariss.domain.DescriptionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import id.lariss.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DescriptionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Description.class);
        Description description1 = getDescriptionSample1();
        Description description2 = new Description();
        assertThat(description1).isNotEqualTo(description2);

        description2.setId(description1.getId());
        assertThat(description1).isEqualTo(description2);

        description2 = getDescriptionSample2();
        assertThat(description1).isNotEqualTo(description2);
    }
}
