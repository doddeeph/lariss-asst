package id.lariss.service.mapper;

import id.lariss.domain.Order;
import id.lariss.domain.OrderProduct;
import id.lariss.domain.ProductDetails;
import id.lariss.service.dto.OrderDTO;
import id.lariss.service.dto.OrderProductDTO;
import id.lariss.service.dto.ProductDetailsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrderProduct} and its DTO {@link OrderProductDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrderProductMapper extends EntityMapper<OrderProductDTO, OrderProduct> {
    @Mapping(target = "order", source = "order", qualifiedByName = "orderId")
    @Mapping(target = "productDetails", source = "productDetails", qualifiedByName = "productDetailsId")
    OrderProductDTO toDto(OrderProduct s);

    @Named("orderId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrderDTO toDtoOrderId(Order order);

    @Named("productDetailsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductDetailsDTO toDtoProductDetailsId(ProductDetails productDetails);
}
