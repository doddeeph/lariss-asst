package id.lariss.service.mapper;

import id.lariss.domain.Processor;
import id.lariss.service.dto.ProcessorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Processor} and its DTO {@link ProcessorDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProcessorMapper extends EntityMapper<ProcessorDTO, Processor> {}
