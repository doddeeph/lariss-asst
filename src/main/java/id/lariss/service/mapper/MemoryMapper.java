package id.lariss.service.mapper;

import id.lariss.domain.Memory;
import id.lariss.service.dto.MemoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Memory} and its DTO {@link MemoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface MemoryMapper extends EntityMapper<MemoryDTO, Memory> {}
