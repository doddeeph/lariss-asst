package id.lariss.service.mapper;

import id.lariss.domain.Billing;
import id.lariss.domain.Customer;
import id.lariss.domain.Order;
import id.lariss.domain.Payment;
import id.lariss.domain.Shipping;
import id.lariss.service.dto.BillingDTO;
import id.lariss.service.dto.CustomerDTO;
import id.lariss.service.dto.OrderDTO;
import id.lariss.service.dto.PaymentDTO;
import id.lariss.service.dto.ShippingDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Order} and its DTO {@link OrderDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrderMapper extends EntityMapper<OrderDTO, Order> {
    @Mapping(target = "customer", source = "customer", qualifiedByName = "customerId")
    @Mapping(target = "shipping", source = "shipping", qualifiedByName = "shippingId")
    @Mapping(target = "billing", source = "billing", qualifiedByName = "billingId")
    @Mapping(target = "payment", source = "payment", qualifiedByName = "paymentId")
    OrderDTO toDto(Order s);

    @Named("customerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CustomerDTO toDtoCustomerId(Customer customer);

    @Named("shippingId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ShippingDTO toDtoShippingId(Shipping shipping);

    @Named("billingId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BillingDTO toDtoBillingId(Billing billing);

    @Named("paymentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PaymentDTO toDtoPaymentId(Payment payment);
}
