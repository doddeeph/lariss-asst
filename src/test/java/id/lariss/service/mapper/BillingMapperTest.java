package id.lariss.service.mapper;

import static id.lariss.domain.BillingAsserts.*;
import static id.lariss.domain.BillingTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BillingMapperTest {

    private BillingMapper billingMapper;

    @BeforeEach
    void setUp() {
        billingMapper = new BillingMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getBillingSample1();
        var actual = billingMapper.toEntity(billingMapper.toDto(expected));
        assertBillingAllPropertiesEquals(expected, actual);
    }
}
