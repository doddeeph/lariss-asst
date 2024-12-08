package id.lariss.service.mapper;

import id.lariss.domain.Connectivity;
import id.lariss.service.dto.ConnectivityDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Connectivity} and its DTO {@link ConnectivityDTO}.
 */
@Mapper(componentModel = "spring")
public interface ConnectivityMapper extends EntityMapper<ConnectivityDTO, Connectivity> {}
