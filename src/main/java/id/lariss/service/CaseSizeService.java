package id.lariss.service;

import id.lariss.service.dto.CaseSizeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link id.lariss.domain.CaseSize}.
 */
public interface CaseSizeService {
    /**
     * Save a caseSize.
     *
     * @param caseSizeDTO the entity to save.
     * @return the persisted entity.
     */
    CaseSizeDTO save(CaseSizeDTO caseSizeDTO);

    /**
     * Updates a caseSize.
     *
     * @param caseSizeDTO the entity to update.
     * @return the persisted entity.
     */
    CaseSizeDTO update(CaseSizeDTO caseSizeDTO);

    /**
     * Partially updates a caseSize.
     *
     * @param caseSizeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CaseSizeDTO> partialUpdate(CaseSizeDTO caseSizeDTO);

    /**
     * Get all the caseSizes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CaseSizeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" caseSize.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CaseSizeDTO> findOne(Long id);

    /**
     * Delete the "id" caseSize.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
