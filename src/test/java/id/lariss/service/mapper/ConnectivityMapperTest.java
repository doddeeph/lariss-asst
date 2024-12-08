package id.lariss.service.mapper;

import static id.lariss.domain.ConnectivityAsserts.*;
import static id.lariss.domain.ConnectivityTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConnectivityMapperTest {

    private ConnectivityMapper connectivityMapper;

    @BeforeEach
    void setUp() {
        connectivityMapper = new ConnectivityMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getConnectivitySample1();
        var actual = connectivityMapper.toEntity(connectivityMapper.toDto(expected));
        assertConnectivityAllPropertiesEquals(expected, actual);
    }
}
