package id.lariss.ai.chat.client.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import id.lariss.service.ProductService;
import id.lariss.service.dto.ProductDTO;
import java.util.List;
import java.util.function.Function;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

@Configuration
public class ProductConfig {

    private static final Logger LOG = LoggerFactory.getLogger(ProductConfig.class);

    private final ProductService productService;

    public ProductConfig(ProductService productService) {
        this.productService = productService;
    }

    public record ProductReq(String categoryName) {}

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Product(String name, String category) {}

    @Bean
    @Description("Get products")
    public Function<ProductReq, List<Product>> getProducts() {
        return request -> {
            String categoryName = request.categoryName;
            LOG.debug("getProducts -> category: {}", categoryName);
            if (StringUtils.isBlank(categoryName)) {
                return mapToProducts(productService.findAll());
            } else {
                return mapToProducts(productService.findAllByCategoryName(categoryName));
            }
        };
    }

    private List<Product> mapToProducts(List<ProductDTO> products) {
        return products.stream().map(p -> new Product(p.getName(), p.getCategory().getName())).toList();
    }
}
