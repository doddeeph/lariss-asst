package id.lariss.service;

import id.lariss.service.dto.StrapColorDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link id.lariss.domain.StrapColor}.
 */
public interface StrapColorService {
    /**
     * Save a strapColor.
     *
     * @param strapColorDTO the entity to save.
     * @return the persisted entity.
     */
    StrapColorDTO save(StrapColorDTO strapColorDTO);

    /**
     * Updates a strapColor.
     *
     * @param strapColorDTO the entity to update.
     * @return the persisted entity.
     */
    StrapColorDTO update(StrapColorDTO strapColorDTO);

    /**
     * Partially updates a strapColor.
     *
     * @param strapColorDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<StrapColorDTO> partialUpdate(StrapColorDTO strapColorDTO);

    /**
     * Get all the strapColors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<StrapColorDTO> findAll(Pageable pageable);

    /**
     * Get the "id" strapColor.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<StrapColorDTO> findOne(Long id);

    /**
     * Delete the "id" strapColor.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
