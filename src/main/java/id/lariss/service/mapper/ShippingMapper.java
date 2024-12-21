package id.lariss.service.mapper;

import id.lariss.domain.Shipping;
import id.lariss.service.dto.ShippingDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Shipping} and its DTO {@link ShippingDTO}.
 */
@Mapper(componentModel = "spring")
public interface ShippingMapper extends EntityMapper<ShippingDTO, Shipping> {}
