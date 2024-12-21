package id.lariss.domain;

import static id.lariss.domain.BillingTestSamples.*;
import static id.lariss.domain.OrderTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import id.lariss.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BillingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Billing.class);
        Billing billing1 = getBillingSample1();
        Billing billing2 = new Billing();
        assertThat(billing1).isNotEqualTo(billing2);

        billing2.setId(billing1.getId());
        assertThat(billing1).isEqualTo(billing2);

        billing2 = getBillingSample2();
        assertThat(billing1).isNotEqualTo(billing2);
    }

    @Test
    void orderTest() {
        Billing billing = getBillingRandomSampleGenerator();
        Order orderBack = getOrderRandomSampleGenerator();

        billing.setOrder(orderBack);
        assertThat(billing.getOrder()).isEqualTo(orderBack);
        assertThat(orderBack.getBilling()).isEqualTo(billing);

        billing.order(null);
        assertThat(billing.getOrder()).isNull();
        assertThat(orderBack.getBilling()).isNull();
    }
}
