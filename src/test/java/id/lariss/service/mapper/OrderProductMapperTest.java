package id.lariss.service.mapper;

import static id.lariss.domain.OrderProductAsserts.*;
import static id.lariss.domain.OrderProductTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderProductMapperTest {

    private OrderProductMapper orderProductMapper;

    @BeforeEach
    void setUp() {
        orderProductMapper = new OrderProductMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getOrderProductSample1();
        var actual = orderProductMapper.toEntity(orderProductMapper.toDto(expected));
        assertOrderProductAllPropertiesEquals(expected, actual);
    }
}
