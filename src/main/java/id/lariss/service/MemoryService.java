package id.lariss.service;

import id.lariss.service.dto.MemoryDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link id.lariss.domain.Memory}.
 */
public interface MemoryService {
    /**
     * Save a memory.
     *
     * @param memoryDTO the entity to save.
     * @return the persisted entity.
     */
    MemoryDTO save(MemoryDTO memoryDTO);

    /**
     * Updates a memory.
     *
     * @param memoryDTO the entity to update.
     * @return the persisted entity.
     */
    MemoryDTO update(MemoryDTO memoryDTO);

    /**
     * Partially updates a memory.
     *
     * @param memoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MemoryDTO> partialUpdate(MemoryDTO memoryDTO);

    /**
     * Get all the memories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MemoryDTO> findAll(Pageable pageable);

    /**
     * Get the "id" memory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MemoryDTO> findOne(Long id);

    /**
     * Delete the "id" memory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
