package id.lariss.service.mapper;

import id.lariss.domain.Color;
import id.lariss.domain.Description;
import id.lariss.domain.Memory;
import id.lariss.domain.Processor;
import id.lariss.domain.Product;
import id.lariss.domain.ProductDetails;
import id.lariss.domain.Storage;
import id.lariss.service.dto.ColorDTO;
import id.lariss.service.dto.DescriptionDTO;
import id.lariss.service.dto.MemoryDTO;
import id.lariss.service.dto.ProcessorDTO;
import id.lariss.service.dto.ProductDTO;
import id.lariss.service.dto.ProductDetailsDTO;
import id.lariss.service.dto.StorageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProductDetails} and its DTO {@link ProductDetailsDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductDetailsMapper extends EntityMapper<ProductDetailsDTO, ProductDetails> {
    @Mapping(target = "product", source = "product", qualifiedByName = "productName")
    @Mapping(target = "description", source = "description", qualifiedByName = "descriptionName")
    @Mapping(target = "color", source = "color", qualifiedByName = "colorName")
    @Mapping(target = "processor", source = "processor", qualifiedByName = "processorName")
    @Mapping(target = "memory", source = "memory", qualifiedByName = "memoryName")
    @Mapping(target = "storage", source = "storage", qualifiedByName = "storageName")
    ProductDetailsDTO toDto(ProductDetails s);

    @Named("productName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ProductDTO toDtoProductName(Product product);

    @Named("descriptionName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    DescriptionDTO toDtoDescriptionName(Description description);

    @Named("colorName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ColorDTO toDtoColorName(Color color);

    @Named("processorName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ProcessorDTO toDtoProcessorName(Processor processor);

    @Named("memoryName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    MemoryDTO toDtoMemoryName(Memory memory);

    @Named("storageName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    StorageDTO toDtoStorageName(Storage storage);
}
