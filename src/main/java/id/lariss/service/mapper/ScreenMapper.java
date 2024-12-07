package id.lariss.service.mapper;

import id.lariss.domain.Screen;
import id.lariss.service.dto.ScreenDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Screen} and its DTO {@link ScreenDTO}.
 */
@Mapper(componentModel = "spring")
public interface ScreenMapper extends EntityMapper<ScreenDTO, Screen> {}
