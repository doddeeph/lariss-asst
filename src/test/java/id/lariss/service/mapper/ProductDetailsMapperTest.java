package id.lariss.service.mapper;

import static id.lariss.domain.ProductDetailsAsserts.*;
import static id.lariss.domain.ProductDetailsTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductDetailsMapperTest {

    private ProductDetailsMapper productDetailsMapper;

    @BeforeEach
    void setUp() {
        productDetailsMapper = new ProductDetailsMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProductDetailsSample1();
        var actual = productDetailsMapper.toEntity(productDetailsMapper.toDto(expected));
        assertProductDetailsAllPropertiesEquals(expected, actual);
    }
}
