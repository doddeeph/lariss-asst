package id.lariss.ai.chat.client.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import id.lariss.service.ProductDetailsService;
import id.lariss.service.dto.ProductDetailsDTO;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
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
        Set<String> storages
    ) {}

    @Bean
    @Description("Get product details")
    public Function<ProductDetailsReq, ProductDetails> getProductDetails() {
        return request -> {
            String productName = request.productName();
            LOG.debug("getProductDetails -> product: {}", productName);
            List<ProductDetailsDTO> productDetailsDTOS = productDetailsService.findOneByProductName(productName);

            String product = CollectionUtils.isNotEmpty(productDetailsDTOS) ? productDetailsDTOS.get(0).getName() : null;
            BigDecimal lowestPrice = BigDecimal.valueOf(Double.MAX_VALUE);
            BigDecimal highestPrice = BigDecimal.valueOf(Double.MIN_VALUE);
            String thumbnail = null;
            String description = null;
            Set<String> colors = new HashSet<>();
            Set<String> processors = new HashSet<>();
            Set<String> memories = new HashSet<>();
            Set<String> storages = new HashSet<>();

            for (ProductDetailsDTO productDetailsDTO : productDetailsDTOS) {
                BigDecimal price = productDetailsDTO.getPrice();
                if (price.compareTo(lowestPrice) < 0) {
                    lowestPrice = price;
                }
                if (price.compareTo(highestPrice) > 0) {
                    highestPrice = price;
                }
                thumbnail = productDetailsDTO.getThumbnail();
                description = productDetailsDTO.getDescription().getValue();
                colors.add(productDetailsDTO.getColor().getName());
                processors.add(productDetailsDTO.getProcessor().getName());
                memories.add(productDetailsDTO.getMemory().getName());
                storages.add(productDetailsDTO.getStorage().getName());
            }

            return new ProductDetails(product, lowestPrice, highestPrice, thumbnail, description, colors, processors, memories, storages);
        };
    }
}
