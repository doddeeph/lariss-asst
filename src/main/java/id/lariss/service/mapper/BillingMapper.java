package id.lariss.service.mapper;

import id.lariss.domain.Billing;
import id.lariss.service.dto.BillingDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Billing} and its DTO {@link BillingDTO}.
 */
@Mapper(componentModel = "spring")
public interface BillingMapper extends EntityMapper<BillingDTO, Billing> {}
