package id.lariss.service.mapper;

import id.lariss.domain.Color;
import id.lariss.service.dto.ColorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Color} and its DTO {@link ColorDTO}.
 */
@Mapper(componentModel = "spring")
public interface ColorMapper extends EntityMapper<ColorDTO, Color> {}
