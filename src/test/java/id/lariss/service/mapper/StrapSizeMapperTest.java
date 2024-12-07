package id.lariss.service.mapper;

import static id.lariss.domain.StrapSizeAsserts.*;
import static id.lariss.domain.StrapSizeTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StrapSizeMapperTest {

    private StrapSizeMapper strapSizeMapper;

    @BeforeEach
    void setUp() {
        strapSizeMapper = new StrapSizeMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getStrapSizeSample1();
        var actual = strapSizeMapper.toEntity(strapSizeMapper.toDto(expected));
        assertStrapSizeAllPropertiesEquals(expected, actual);
    }
}
