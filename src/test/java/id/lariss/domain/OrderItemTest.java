package id.lariss.domain;

import static id.lariss.domain.OrderItemTestSamples.*;
import static id.lariss.domain.OrderTestSamples.*;
import static id.lariss.domain.ProductDetailsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import id.lariss.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderItem.class);
        OrderItem orderItem1 = getOrderItemSample1();
        OrderItem orderItem2 = new OrderItem();
        assertThat(orderItem1).isNotEqualTo(orderItem2);

        orderItem2.setId(orderItem1.getId());
        assertThat(orderItem1).isEqualTo(orderItem2);

        orderItem2 = getOrderItemSample2();
        assertThat(orderItem1).isNotEqualTo(orderItem2);
    }

    @Test
    void orderTest() {
        OrderItem orderItem = getOrderItemRandomSampleGenerator();
        Order orderBack = getOrderRandomSampleGenerator();

        orderItem.setOrder(orderBack);
        assertThat(orderItem.getOrder()).isEqualTo(orderBack);

        orderItem.order(null);
        assertThat(orderItem.getOrder()).isNull();
    }

    @Test
    void productDetailsTest() {
        OrderItem orderItem = getOrderItemRandomSampleGenerator();
        ProductDetails productDetailsBack = getProductDetailsRandomSampleGenerator();

        orderItem.setProductDetails(productDetailsBack);
        assertThat(orderItem.getProductDetails()).isEqualTo(productDetailsBack);

        orderItem.productDetails(null);
        assertThat(orderItem.getProductDetails()).isNull();
    }
}
