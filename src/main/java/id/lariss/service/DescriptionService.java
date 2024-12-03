package id.lariss.service;

import id.lariss.service.dto.DescriptionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link id.lariss.domain.Description}.
 */
public interface DescriptionService {
    /**
     * Save a description.
     *
     * @param descriptionDTO the entity to save.
     * @return the persisted entity.
     */
    DescriptionDTO save(DescriptionDTO descriptionDTO);

    /**
     * Updates a description.
     *
     * @param descriptionDTO the entity to update.
     * @return the persisted entity.
     */
    DescriptionDTO update(DescriptionDTO descriptionDTO);

    /**
     * Partially updates a description.
     *
     * @param descriptionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DescriptionDTO> partialUpdate(DescriptionDTO descriptionDTO);

    /**
     * Get all the descriptions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DescriptionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" description.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DescriptionDTO> findOne(Long id);

    /**
     * Delete the "id" description.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
