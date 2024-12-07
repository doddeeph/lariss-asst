package id.lariss.domain;

import static id.lariss.domain.CaseSizeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import id.lariss.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CaseSizeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CaseSize.class);
        CaseSize caseSize1 = getCaseSizeSample1();
        CaseSize caseSize2 = new CaseSize();
        assertThat(caseSize1).isNotEqualTo(caseSize2);

        caseSize2.setId(caseSize1.getId());
        assertThat(caseSize1).isEqualTo(caseSize2);

        caseSize2 = getCaseSizeSample2();
        assertThat(caseSize1).isNotEqualTo(caseSize2);
    }
}
