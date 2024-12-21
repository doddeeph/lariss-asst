package id.lariss.domain;

import static id.lariss.domain.OrderProductTestSamples.*;
import static id.lariss.domain.OrderTestSamples.*;
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
