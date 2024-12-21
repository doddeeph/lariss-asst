package id.lariss.domain;

import static id.lariss.domain.CustomerTestSamples.*;
import static id.lariss.domain.OrderTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import id.lariss.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CustomerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Customer.class);
        Customer customer1 = getCustomerSample1();
        Customer customer2 = new Customer();
        assertThat(customer1).isNotEqualTo(customer2);

        customer2.setId(customer1.getId());
        assertThat(customer1).isEqualTo(customer2);

        customer2 = getCustomerSample2();
        assertThat(customer1).isNotEqualTo(customer2);
    }

    @Test
    void orderTest() {
        Customer customer = getCustomerRandomSampleGenerator();
        Order orderBack = getOrderRandomSampleGenerator();

        customer.setOrder(orderBack);
        assertThat(customer.getOrder()).isEqualTo(orderBack);
        assertThat(orderBack.getCustomer()).isEqualTo(customer);

        customer.order(null);
        assertThat(customer.getOrder()).isNull();
        assertThat(orderBack.getCustomer()).isNull();
    }
}
