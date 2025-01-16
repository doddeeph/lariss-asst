package id.lariss.service.mapper;

import id.lariss.domain.Order;
import id.lariss.domain.OrderItem;
import id.lariss.domain.ProductDetails;
import id.lariss.service.dto.OrderDTO;
import id.lariss.service.dto.OrderItemDTO;
import id.lariss.service.dto.ProductDetailsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrderItem} and its DTO {@link OrderItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrderItemMapper extends EntityMapper<OrderItemDTO, OrderItem> {
    @Mapping(target = "order", source = "order", qualifiedByName = "orderId")
    @Mapping(target = "productDetails", source = "productDetails", qualifiedByName = "productDetailsId")
    OrderItemDTO toDto(OrderItem s);

    @Named("orderId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrderDTO toDtoOrderId(Order order);

    @Named("productDetailsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductDetailsDTO toDtoProductDetailsId(ProductDetails productDetails);
}
