package id.lariss.repository;

import id.lariss.domain.ProductDetails;
import java.util.List;

public interface ProductDetailsRepositoryCustom {
    List<ProductDetails> findAllByProductNameIn(List<String> names);

    List<ProductDetails> findLowestPriceByProductNameIn(List<String> names);
}
