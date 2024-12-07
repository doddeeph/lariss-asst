package id.lariss.service;

import id.lariss.service.dto.ScreenDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link id.lariss.domain.Screen}.
 */
public interface ScreenService {
    /**
     * Save a screen.
     *
     * @param screenDTO the entity to save.
     * @return the persisted entity.
     */
    ScreenDTO save(ScreenDTO screenDTO);

    /**
     * Updates a screen.
     *
     * @param screenDTO the entity to update.
     * @return the persisted entity.
     */
    ScreenDTO update(ScreenDTO screenDTO);

    /**
     * Partially updates a screen.
     *
     * @param screenDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ScreenDTO> partialUpdate(ScreenDTO screenDTO);

    /**
     * Get all the screens.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ScreenDTO> findAll(Pageable pageable);

    /**
     * Get the "id" screen.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ScreenDTO> findOne(Long id);

    /**
     * Delete the "id" screen.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
