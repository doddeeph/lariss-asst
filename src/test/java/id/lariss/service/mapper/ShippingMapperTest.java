package id.lariss.service.mapper;

import static id.lariss.domain.ShippingAsserts.*;
import static id.lariss.domain.ShippingTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ShippingMapperTest {

    private ShippingMapper shippingMapper;

    @BeforeEach
    void setUp() {
        shippingMapper = new ShippingMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getShippingSample1();
        var actual = shippingMapper.toEntity(shippingMapper.toDto(expected));
        assertShippingAllPropertiesEquals(expected, actual);
    }
}
