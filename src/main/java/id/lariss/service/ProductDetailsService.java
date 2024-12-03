package id.lariss.service;

import id.lariss.service.dto.ProductDetailsDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link id.lariss.domain.ProductDetails}.
 */
public interface ProductDetailsService {
    /**
     * Save a productDetails.
     *
     * @param productDetailsDTO the entity to save.
     * @return the persisted entity.
     */
    ProductDetailsDTO save(ProductDetailsDTO productDetailsDTO);

    /**
     * Updates a productDetails.
     *
     * @param productDetailsDTO the entity to update.
     * @return the persisted entity.
     */
    ProductDetailsDTO update(ProductDetailsDTO productDetailsDTO);

    /**
     * Partially updates a productDetails.
     *
     * @param productDetailsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProductDetailsDTO> partialUpdate(ProductDetailsDTO productDetailsDTO);

    /**
     * Get all the productDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProductDetailsDTO> findAll(Pageable pageable);

    /**
     * Get all the productDetails with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProductDetailsDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" productDetails.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProductDetailsDTO> findOne(Long id);

    /**
     * Delete the "id" productDetails.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<ProductDetailsDTO> findOneByProductName(String name);
}
