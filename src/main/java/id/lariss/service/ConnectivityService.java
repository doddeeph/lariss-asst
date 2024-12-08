package id.lariss.service;

import id.lariss.service.dto.ConnectivityDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link id.lariss.domain.Connectivity}.
 */
public interface ConnectivityService {
    /**
     * Save a connectivity.
     *
     * @param connectivityDTO the entity to save.
     * @return the persisted entity.
     */
    ConnectivityDTO save(ConnectivityDTO connectivityDTO);

    /**
     * Updates a connectivity.
     *
     * @param connectivityDTO the entity to update.
     * @return the persisted entity.
     */
    ConnectivityDTO update(ConnectivityDTO connectivityDTO);

    /**
     * Partially updates a connectivity.
     *
     * @param connectivityDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ConnectivityDTO> partialUpdate(ConnectivityDTO connectivityDTO);

    /**
     * Get all the connectivities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ConnectivityDTO> findAll(Pageable pageable);

    /**
     * Get the "id" connectivity.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ConnectivityDTO> findOne(Long id);

    /**
     * Delete the "id" connectivity.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
