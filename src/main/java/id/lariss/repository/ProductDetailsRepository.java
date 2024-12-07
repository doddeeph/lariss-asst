package id.lariss.repository;

import id.lariss.domain.ProductDetails;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductDetails entity.
 */
@Repository
public interface ProductDetailsRepository extends JpaRepository<ProductDetails, Long> {
    default Optional<ProductDetails> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ProductDetails> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ProductDetails> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select productDetails from ProductDetails productDetails left join fetch productDetails.product left join fetch productDetails.description left join fetch productDetails.color left join fetch productDetails.processor left join fetch productDetails.memory left join fetch productDetails.storage left join fetch productDetails.screen left join fetch productDetails.connectivity left join fetch productDetails.material left join fetch productDetails.caseSize left join fetch productDetails.strapColor left join fetch productDetails.strapSize",
        countQuery = "select count(productDetails) from ProductDetails productDetails"
    )
    Page<ProductDetails> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select productDetails from ProductDetails productDetails left join fetch productDetails.product left join fetch productDetails.description left join fetch productDetails.color left join fetch productDetails.processor left join fetch productDetails.memory left join fetch productDetails.storage left join fetch productDetails.screen left join fetch productDetails.connectivity left join fetch productDetails.material left join fetch productDetails.caseSize left join fetch productDetails.strapColor left join fetch productDetails.strapSize"
    )
    List<ProductDetails> findAllWithToOneRelationships();

    @Query(
        "select productDetails from ProductDetails productDetails left join fetch productDetails.product left join fetch productDetails.description left join fetch productDetails.color left join fetch productDetails.processor left join fetch productDetails.memory left join fetch productDetails.storage left join fetch productDetails.screen left join fetch productDetails.connectivity left join fetch productDetails.material left join fetch productDetails.caseSize left join fetch productDetails.strapColor left join fetch productDetails.strapSize where productDetails.id =:id"
    )
    Optional<ProductDetails> findOneWithToOneRelationships(@Param("id") Long id);
}
