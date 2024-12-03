package id.lariss.service.mapper;

import static id.lariss.domain.ProcessorAsserts.*;
import static id.lariss.domain.ProcessorTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProcessorMapperTest {

    private ProcessorMapper processorMapper;

    @BeforeEach
    void setUp() {
        processorMapper = new ProcessorMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProcessorSample1();
        var actual = processorMapper.toEntity(processorMapper.toDto(expected));
        assertProcessorAllPropertiesEquals(expected, actual);
    }
}
