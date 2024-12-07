package id.lariss.service.mapper;

import static id.lariss.domain.CaseSizeAsserts.*;
import static id.lariss.domain.CaseSizeTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CaseSizeMapperTest {

    private CaseSizeMapper caseSizeMapper;

    @BeforeEach
    void setUp() {
        caseSizeMapper = new CaseSizeMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCaseSizeSample1();
        var actual = caseSizeMapper.toEntity(caseSizeMapper.toDto(expected));
        assertCaseSizeAllPropertiesEquals(expected, actual);
    }
}
