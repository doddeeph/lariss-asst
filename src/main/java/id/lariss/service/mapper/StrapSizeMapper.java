package id.lariss.service.mapper;

import id.lariss.domain.StrapSize;
import id.lariss.service.dto.StrapSizeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link StrapSize} and its DTO {@link StrapSizeDTO}.
 */
@Mapper(componentModel = "spring")
public interface StrapSizeMapper extends EntityMapper<StrapSizeDTO, StrapSize> {}
