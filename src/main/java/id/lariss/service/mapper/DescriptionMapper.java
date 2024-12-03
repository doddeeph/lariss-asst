package id.lariss.service.mapper;

import id.lariss.domain.Description;
import id.lariss.service.dto.DescriptionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Description} and its DTO {@link DescriptionDTO}.
 */
@Mapper(componentModel = "spring")
public interface DescriptionMapper extends EntityMapper<DescriptionDTO, Description> {}
