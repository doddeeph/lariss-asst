package id.lariss.service.mapper;

import static id.lariss.domain.MemoryAsserts.*;
import static id.lariss.domain.MemoryTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MemoryMapperTest {

    private MemoryMapper memoryMapper;

    @BeforeEach
    void setUp() {
        memoryMapper = new MemoryMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMemorySample1();
        var actual = memoryMapper.toEntity(memoryMapper.toDto(expected));
        assertMemoryAllPropertiesEquals(expected, actual);
    }
}
