package id.lariss.service;

import id.lariss.service.dto.StrapSizeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link id.lariss.domain.StrapSize}.
 */
public interface StrapSizeService {
    /**
     * Save a strapSize.
     *
     * @param strapSizeDTO the entity to save.
     * @return the persisted entity.
     */
    StrapSizeDTO save(StrapSizeDTO strapSizeDTO);

    /**
     * Updates a strapSize.
     *
     * @param strapSizeDTO the entity to update.
     * @return the persisted entity.
     */
    StrapSizeDTO update(StrapSizeDTO strapSizeDTO);

    /**
     * Partially updates a strapSize.
     *
     * @param strapSizeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<StrapSizeDTO> partialUpdate(StrapSizeDTO strapSizeDTO);

    /**
     * Get all the strapSizes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<StrapSizeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" strapSize.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<StrapSizeDTO> findOne(Long id);

    /**
     * Delete the "id" strapSize.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
