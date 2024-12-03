package id.lariss.service;

import id.lariss.service.dto.StorageDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link id.lariss.domain.Storage}.
 */
public interface StorageService {
    /**
     * Save a storage.
     *
     * @param storageDTO the entity to save.
     * @return the persisted entity.
     */
    StorageDTO save(StorageDTO storageDTO);

    /**
     * Updates a storage.
     *
     * @param storageDTO the entity to update.
     * @return the persisted entity.
     */
    StorageDTO update(StorageDTO storageDTO);

    /**
     * Partially updates a storage.
     *
     * @param storageDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<StorageDTO> partialUpdate(StorageDTO storageDTO);

    /**
     * Get all the storages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<StorageDTO> findAll(Pageable pageable);

    /**
     * Get the "id" storage.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<StorageDTO> findOne(Long id);

    /**
     * Delete the "id" storage.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
