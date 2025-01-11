package id.lariss.service;

import id.lariss.service.dto.BillingDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link id.lariss.domain.Billing}.
 */
public interface BillingService {
    /**
     * Save a billing.
     *
     * @param billingDTO the entity to save.
     * @return the persisted entity.
     */
    BillingDTO save(BillingDTO billingDTO);

    /**
     * Updates a billing.
     *
     * @param billingDTO the entity to update.
     * @return the persisted entity.
     */
    BillingDTO update(BillingDTO billingDTO);

    /**
     * Partially updates a billing.
     *
     * @param billingDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BillingDTO> partialUpdate(BillingDTO billingDTO);

    /**
     * Get all the billings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BillingDTO> findAll(Pageable pageable);

    /**
     * Get all the BillingDTO where Order is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<BillingDTO> findAllWhereOrderIsNull();

    /**
     * Get the "id" billing.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BillingDTO> findOne(Long id);

    /**
     * Delete the "id" billing.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
