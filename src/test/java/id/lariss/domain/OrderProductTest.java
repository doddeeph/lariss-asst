package id.lariss.domain;

import static id.lariss.domain.OrderProductTestSamples.*;
import static id.lariss.domain.OrderTestSamples.*;
import static id.lariss.domain.ProductDetailsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import id.lariss.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderProductTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderProduct.class);
        OrderProduct orderProduct1 = getOrderProductSample1();
        OrderProduct orderProduct2 = new OrderProduct();
        assertThat(orderProduct1).isNotEqualTo(orderProduct2);

        orderProduct2.setId(orderProduct1.getId());
        assertThat(orderProduct1).isEqualTo(orderProduct2);

        orderProduct2 = getOrderProductSample2();
        assertThat(orderProduct1).isNotEqualTo(orderProduct2);
    }

    @Test
    void orderTest() {
        OrderProduct orderProduct = getOrderProductRandomSampleGenerator();
        Order orderBack = getOrderRandomSampleGenerator();

        orderProduct.setOrder(orderBack);
        assertThat(orderProduct.getOrder()).isEqualTo(orderBack);

        orderProduct.order(null);
        assertThat(orderProduct.getOrder()).isNull();
    }

    @Test
    void productDetailsTest() {
        OrderProduct orderProduct = getOrderProductRandomSampleGenerator();
        ProductDetails productDetailsBack = getProductDetailsRandomSampleGenerator();

        orderProduct.setProductDetails(productDetailsBack);
        assertThat(orderProduct.getProductDetails()).isEqualTo(productDetailsBack);

        orderProduct.productDetails(null);
        assertThat(orderProduct.getProductDetails()).isNull();
    }
}
