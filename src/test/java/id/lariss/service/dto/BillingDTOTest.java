package id.lariss.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import id.lariss.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BillingDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BillingDTO.class);
        BillingDTO billingDTO1 = new BillingDTO();
        billingDTO1.setId(1L);
        BillingDTO billingDTO2 = new BillingDTO();
        assertThat(billingDTO1).isNotEqualTo(billingDTO2);
        billingDTO2.setId(billingDTO1.getId());
        assertThat(billingDTO1).isEqualTo(billingDTO2);
        billingDTO2.setId(2L);
        assertThat(billingDTO1).isNotEqualTo(billingDTO2);
        billingDTO1.setId(null);
        assertThat(billingDTO1).isNotEqualTo(billingDTO2);
    }
}
