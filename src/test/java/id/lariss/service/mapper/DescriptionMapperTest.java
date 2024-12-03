package id.lariss.service.mapper;

import static id.lariss.domain.DescriptionAsserts.*;
import static id.lariss.domain.DescriptionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DescriptionMapperTest {

    private DescriptionMapper descriptionMapper;

    @BeforeEach
    void setUp() {
        descriptionMapper = new DescriptionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDescriptionSample1();
        var actual = descriptionMapper.toEntity(descriptionMapper.toDto(expected));
        assertDescriptionAllPropertiesEquals(expected, actual);
    }
}
