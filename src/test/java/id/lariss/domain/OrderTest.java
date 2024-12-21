package id.lariss.domain;

import static id.lariss.domain.BillingTestSamples.*;
import static id.lariss.domain.CustomerTestSamples.*;
import static id.lariss.domain.OrderProductTestSamples.*;
import static id.lariss.domain.OrderTestSamples.*;
import static id.lariss.domain.PaymentTestSamples.*;
import static id.lariss.domain.ShippingTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import id.lariss.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Order.class);
        Order order1 = getOrderSample1();
        Order order2 = new Order();
        assertThat(order1).isNotEqualTo(order2);

        order2.setId(order1.getId());
        assertThat(order1).isEqualTo(order2);

        order2 = getOrderSample2();
        assertThat(order1).isNotEqualTo(order2);
    }

    @Test
    void customerTest() {
        Order order = getOrderRandomSampleGenerator();
        Customer customerBack = getCustomerRandomSampleGenerator();

        order.setCustomer(customerBack);
        assertThat(order.getCustomer()).isEqualTo(customerBack);

        order.customer(null);
        assertThat(order.getCustomer()).isNull();
    }

    @Test
    void shippingTest() {
        Order order = getOrderRandomSampleGenerator();
        Shipping shippingBack = getShippingRandomSampleGenerator();

        order.setShipping(shippingBack);
        assertThat(order.getShipping()).isEqualTo(shippingBack);

        order.shipping(null);
        assertThat(order.getShipping()).isNull();
    }

    @Test
    void billingTest() {
        Order order = getOrderRandomSampleGenerator();
        Billing billingBack = getBillingRandomSampleGenerator();

        order.setBilling(billingBack);
        assertThat(order.getBilling()).isEqualTo(billingBack);

        order.billing(null);
        assertThat(order.getBilling()).isNull();
    }

    @Test
    void paymentTest() {
        Order order = getOrderRandomSampleGenerator();
        Payment paymentBack = getPaymentRandomSampleGenerator();

        order.setPayment(paymentBack);
        assertThat(order.getPayment()).isEqualTo(paymentBack);

        order.payment(null);
        assertThat(order.getPayment()).isNull();
    }

    @Test
    void orderProductTest() {
        Order order = getOrderRandomSampleGenerator();
        OrderProduct orderProductBack = getOrderProductRandomSampleGenerator();

        order.addOrderProduct(orderProductBack);
        assertThat(order.getOrderProducts()).containsOnly(orderProductBack);
        assertThat(orderProductBack.getOrder()).isEqualTo(order);

        order.removeOrderProduct(orderProductBack);
        assertThat(order.getOrderProducts()).doesNotContain(orderProductBack);
        assertThat(orderProductBack.getOrder()).isNull();

        order.orderProducts(new HashSet<>(Set.of(orderProductBack)));
        assertThat(order.getOrderProducts()).containsOnly(orderProductBack);
        assertThat(orderProductBack.getOrder()).isEqualTo(order);

        order.setOrderProducts(new HashSet<>());
        assertThat(order.getOrderProducts()).doesNotContain(orderProductBack);
        assertThat(orderProductBack.getOrder()).isNull();
    }
}
