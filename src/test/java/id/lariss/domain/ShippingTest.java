package id.lariss.domain;

import static id.lariss.domain.OrderTestSamples.*;
import static id.lariss.domain.ShippingTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import id.lariss.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ShippingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Shipping.class);
        Shipping shipping1 = getShippingSample1();
        Shipping shipping2 = new Shipping();
        assertThat(shipping1).isNotEqualTo(shipping2);

        shipping2.setId(shipping1.getId());
        assertThat(shipping1).isEqualTo(shipping2);

        shipping2 = getShippingSample2();
        assertThat(shipping1).isNotEqualTo(shipping2);
    }

    @Test
    void orderTest() {
        Shipping shipping = getShippingRandomSampleGenerator();
        Order orderBack = getOrderRandomSampleGenerator();

        shipping.setOrder(orderBack);
        assertThat(shipping.getOrder()).isEqualTo(orderBack);
        assertThat(orderBack.getShipping()).isEqualTo(shipping);

        shipping.order(null);
        assertThat(shipping.getOrder()).isNull();
        assertThat(orderBack.getShipping()).isNull();
    }
}
