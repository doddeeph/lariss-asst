package id.lariss.ai.chat.client.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import id.lariss.service.ProductDetailsService;
import id.lariss.service.dto.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

@Configuration
public class ProductDetailsConfig {

    private static final Logger LOG = LoggerFactory.getLogger(ProductDetailsConfig.class);

    private final ProductDetailsService productDetailsService;

    public ProductDetailsConfig(ProductDetailsService productDetailsService) {
        this.productDetailsService = productDetailsService;
    }

    public record ProductDetailsReq(String productName) {}

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ProductDetails(
        String product,
        BigDecimal lowestPrice,
        BigDecimal highestPrice,
        String thumbnail,
        String description,
        Set<String> colors,
        Set<String> processors,
        Set<String> memories,
        Set<String> storages,
        Set<String> screens,
        Set<String> connectives,
        Set<String> materials,
        Set<String> caseSizes,
        Set<String> strapColors,
        Set<String> strapSizes
    ) {}

    @Bean
    @Description("Get product details")
    public Function<ProductDetailsReq, ProductDetails> getProductDetails() {
        return request -> {
            String productName = request.productName();
            LOG.debug("getProductDetails -> product: {}", productName);
            List<ProductDetailsDTO> productDetailsDTOS = productDetailsService.findAllByProductName(productName);
            return mapTpProductDetails(productDetailsDTOS);
        };
    }

    public record ProductDetailsByProductNameListReq(List<String> productNames) {}

    @Bean
    @Description("Get lowest price product details")
    public Function<ProductDetailsByProductNameListReq, ProductDetails> getLowestPriceProductDetails() {
        return request -> {
            List<String> productNames = request.productNames();
            LOG.debug("getLowestPriceProductDetails -> products: {}", String.join(",", productNames));
            List<ProductDetailsDTO> productDetailsDTOS = productDetailsService.findLowestPriceByProductNameIn(productNames);
            return mapTpProductDetails(productDetailsDTOS);
        };
    }

    private ProductDetails mapTpProductDetails(List<ProductDetailsDTO> productDetailsDTOS) {
        String product = CollectionUtils.isNotEmpty(productDetailsDTOS) ? productDetailsDTOS.get(0).getName() : null;
        BigDecimal lowestPrice = BigDecimal.valueOf(Double.MAX_VALUE);
        BigDecimal highestPrice = BigDecimal.valueOf(Double.MIN_VALUE);
        String thumbnail = null;
        String description = null;
        Set<String> colors = new HashSet<>();
        Set<String> processors = new HashSet<>();
        Set<String> memories = new HashSet<>();
        Set<String> storages = new HashSet<>();
        Set<String> screens = new HashSet<>();
        Set<String> connectives = new HashSet<>();
        Set<String> materials = new HashSet<>();
        Set<String> caseSizes = new HashSet<>();
        Set<String> strapColors = new HashSet<>();
        Set<String> strapSizes = new HashSet<>();

        for (ProductDetailsDTO productDetailsDTO : productDetailsDTOS) {
            BigDecimal price = productDetailsDTO.getPrice();
            if (price.compareTo(lowestPrice) < 0) {
                lowestPrice = price;
            }
            if (price.compareTo(highestPrice) > 0) {
                highestPrice = price;
            }
            thumbnail = productDetailsDTO.getThumbnail();
            description = Optional.ofNullable(productDetailsDTO.getDescription()).map(DescriptionDTO::getValue).orElse(null);
            colors.add(Optional.ofNullable(productDetailsDTO.getColor()).map(ColorDTO::getName).orElse(null));
            processors.add(Optional.ofNullable(productDetailsDTO.getProcessor()).map(ProcessorDTO::getName).orElse(null));
            memories.add(Optional.ofNullable(productDetailsDTO.getMemory()).map(MemoryDTO::getName).orElse(null));
            storages.add(Optional.ofNullable(productDetailsDTO.getStorage()).map(StorageDTO::getName).orElse(null));
            screens.add(Optional.ofNullable(productDetailsDTO.getScreen()).map(ScreenDTO::getName).orElse(null));
            connectives.add(Optional.ofNullable(productDetailsDTO.getConnectivity()).map(ConnectivityDTO::getName).orElse(null));
            materials.add(Optional.ofNullable(productDetailsDTO.getMaterial()).map(MaterialDTO::getName).orElse(null));
            caseSizes.add(Optional.ofNullable(productDetailsDTO.getCaseSize()).map(CaseSizeDTO::getName).orElse(null));
            strapColors.add(Optional.ofNullable(productDetailsDTO.getStrapColor()).map(StrapColorDTO::getName).orElse(null));
            strapSizes.add(Optional.ofNullable(productDetailsDTO.getStrapSize()).map(StrapSizeDTO::getName).orElse(null));
        }

        return new ProductDetails(
            product,
            lowestPrice,
            highestPrice,
            thumbnail,
            description,
            colors,
            processors,
            memories,
            storages,
            screens,
            connectives,
            materials,
            caseSizes,
            strapColors,
            strapSizes
        );
    }
}
