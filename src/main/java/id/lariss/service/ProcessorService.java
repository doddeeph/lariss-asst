package id.lariss.service;

import id.lariss.service.dto.ProcessorDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link id.lariss.domain.Processor}.
 */
public interface ProcessorService {
    /**
     * Save a processor.
     *
     * @param processorDTO the entity to save.
     * @return the persisted entity.
     */
    ProcessorDTO save(ProcessorDTO processorDTO);

    /**
     * Updates a processor.
     *
     * @param processorDTO the entity to update.
     * @return the persisted entity.
     */
    ProcessorDTO update(ProcessorDTO processorDTO);

    /**
     * Partially updates a processor.
     *
     * @param processorDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProcessorDTO> partialUpdate(ProcessorDTO processorDTO);

    /**
     * Get all the processors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProcessorDTO> findAll(Pageable pageable);

    /**
     * Get the "id" processor.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProcessorDTO> findOne(Long id);

    /**
     * Delete the "id" processor.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
