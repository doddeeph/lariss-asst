package id.lariss.repository.impl;

import id.lariss.domain.ProductDetails;
import id.lariss.repository.ProductDetailsRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductDetailsRepositoryImpl implements ProductDetailsRepositoryCustom {

    private static final Logger LOG = LoggerFactory.getLogger(ProductDetailsRepositoryImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    private static final String SELECT_CLAUSE =
        "SELECT productDetails " +
        "FROM ProductDetails productDetails " +
        "LEFT JOIN FETCH productDetails.product " +
        "LEFT JOIN FETCH productDetails.description " +
        "LEFT JOIN FETCH productDetails.color " +
        "LEFT JOIN FETCH productDetails.processor " +
        "LEFT JOIN FETCH productDetails.memory " +
        "LEFT JOIN FETCH productDetails.storage " +
        "LEFT JOIN FETCH productDetails.screen " +
        "LEFT JOIN FETCH productDetails.connectivity " +
        "LEFT JOIN FETCH productDetails.material " +
        "LEFT JOIN FETCH productDetails.caseSize " +
        "LEFT JOIN FETCH productDetails.strapColor " +
        "LEFT JOIN FETCH productDetails.strapSize ";

    @Override
    public List<ProductDetails> findAllByProductNameIn(List<String> names) {
        String q = SELECT_CLAUSE + buildQueryWhereLike(names);
        Query query = entityManager.createQuery(q);
        IntStream.range(0, names.size()).forEach(i -> query.setParameter("name" + i, names.get(i).toLowerCase()));
        return query.getResultList();
    }

    @Override
    public List<ProductDetails> findLowestPriceByProductNameIn(List<String> names) {
        BigDecimal minPrice = getLowestPriceByProductNameIn(names);
        String q = SELECT_CLAUSE + buildQueryWhereLike(names) + " AND productDetails.price = " + minPrice;
        Query query = entityManager.createQuery(q);
        IntStream.range(0, names.size()).forEach(i -> query.setParameter("name" + i, names.get(i).toLowerCase()));
        return query.getResultList();
    }

    private BigDecimal getLowestPriceByProductNameIn(List<String> names) {
        String minPriceQuery =
            "SELECT min(productDetails.price) " +
            "FROM ProductDetails productDetails " +
            "LEFT JOIN productDetails.product " +
            buildQueryWhereLike(names);
        Query query = entityManager.createQuery(minPriceQuery);
        IntStream.range(0, names.size()).forEach(i -> query.setParameter("name" + i, names.get(i).toLowerCase()));
        return (BigDecimal) query.getSingleResult();
    }

    private String buildQueryWhereLike(List<String> names) {
        return (
            "WHERE (" +
            IntStream.range(0, names.size())
                .mapToObj(i -> "LOWER(productDetails.product.name) LIKE CONCAT('%',:name" + i + ",'%')")
                .collect(Collectors.joining(" OR ")) +
            ")"
        );
    }
}
