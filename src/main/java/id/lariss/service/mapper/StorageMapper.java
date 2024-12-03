package id.lariss.service.mapper;

import id.lariss.domain.Storage;
import id.lariss.service.dto.StorageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Storage} and its DTO {@link StorageDTO}.
 */
@Mapper(componentModel = "spring")
public interface StorageMapper extends EntityMapper<StorageDTO, Storage> {}
