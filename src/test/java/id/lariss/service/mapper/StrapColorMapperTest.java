package id.lariss.service.mapper;

import static id.lariss.domain.StrapColorAsserts.*;
import static id.lariss.domain.StrapColorTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StrapColorMapperTest {

    private StrapColorMapper strapColorMapper;

    @BeforeEach
    void setUp() {
        strapColorMapper = new StrapColorMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getStrapColorSample1();
        var actual = strapColorMapper.toEntity(strapColorMapper.toDto(expected));
        assertStrapColorAllPropertiesEquals(expected, actual);
    }
}
