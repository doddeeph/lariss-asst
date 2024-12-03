package id.lariss.service.mapper;

import static id.lariss.domain.StorageAsserts.*;
import static id.lariss.domain.StorageTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StorageMapperTest {

    private StorageMapper storageMapper;

    @BeforeEach
    void setUp() {
        storageMapper = new StorageMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getStorageSample1();
        var actual = storageMapper.toEntity(storageMapper.toDto(expected));
        assertStorageAllPropertiesEquals(expected, actual);
    }
}
