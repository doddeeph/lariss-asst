package id.lariss.service.mapper;

import static id.lariss.domain.ColorAsserts.*;
import static id.lariss.domain.ColorTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ColorMapperTest {

    private ColorMapper colorMapper;

    @BeforeEach
    void setUp() {
        colorMapper = new ColorMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getColorSample1();
        var actual = colorMapper.toEntity(colorMapper.toDto(expected));
        assertColorAllPropertiesEquals(expected, actual);
    }
}
