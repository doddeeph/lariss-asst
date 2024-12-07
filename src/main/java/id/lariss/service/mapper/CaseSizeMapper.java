package id.lariss.service.mapper;

import id.lariss.domain.CaseSize;
import id.lariss.service.dto.CaseSizeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CaseSize} and its DTO {@link CaseSizeDTO}.
 */
@Mapper(componentModel = "spring")
public interface CaseSizeMapper extends EntityMapper<CaseSizeDTO, CaseSize> {}
