package id.lariss.service.mapper;

import id.lariss.domain.StrapColor;
import id.lariss.service.dto.StrapColorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link StrapColor} and its DTO {@link StrapColorDTO}.
 */
@Mapper(componentModel = "spring")
public interface StrapColorMapper extends EntityMapper<StrapColorDTO, StrapColor> {}
