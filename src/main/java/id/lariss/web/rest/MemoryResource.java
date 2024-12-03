package id.lariss.web.rest;

import id.lariss.repository.MemoryRepository;
import id.lariss.service.MemoryService;
import id.lariss.service.dto.MemoryDTO;
import id.lariss.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link id.lariss.domain.Memory}.
 */
@RestController
@RequestMapping("/api/memories")
public class MemoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(MemoryResource.class);

    private static final String ENTITY_NAME = "memory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MemoryService memoryService;

    private final MemoryRepository memoryRepository;

    public MemoryResource(MemoryService memoryService, MemoryRepository memoryRepository) {
        this.memoryService = memoryService;
        this.memoryRepository = memoryRepository;
    }

    /**
     * {@code POST  /memories} : Create a new memory.
     *
     * @param memoryDTO the memoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new memoryDTO, or with status {@code 400 (Bad Request)} if the memory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MemoryDTO> createMemory(@Valid @RequestBody MemoryDTO memoryDTO) throws URISyntaxException {
        LOG.debug("REST request to save Memory : {}", memoryDTO);
        if (memoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new memory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        memoryDTO = memoryService.save(memoryDTO);
        return ResponseEntity.created(new URI("/api/memories/" + memoryDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, memoryDTO.getId().toString()))
            .body(memoryDTO);
    }

    /**
     * {@code PUT  /memories/:id} : Updates an existing memory.
     *
     * @param id the id of the memoryDTO to save.
     * @param memoryDTO the memoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated memoryDTO,
     * or with status {@code 400 (Bad Request)} if the memoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the memoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MemoryDTO> updateMemory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MemoryDTO memoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Memory : {}, {}", id, memoryDTO);
        if (memoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, memoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!memoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        memoryDTO = memoryService.update(memoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, memoryDTO.getId().toString()))
            .body(memoryDTO);
    }

    /**
     * {@code PATCH  /memories/:id} : Partial updates given fields of an existing memory, field will ignore if it is null
     *
     * @param id the id of the memoryDTO to save.
     * @param memoryDTO the memoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated memoryDTO,
     * or with status {@code 400 (Bad Request)} if the memoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the memoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the memoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MemoryDTO> partialUpdateMemory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MemoryDTO memoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Memory partially : {}, {}", id, memoryDTO);
        if (memoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, memoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!memoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MemoryDTO> result = memoryService.partialUpdate(memoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, memoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /memories} : get all the memories.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of memories in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MemoryDTO>> getAllMemories(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Memories");
        Page<MemoryDTO> page = memoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /memories/:id} : get the "id" memory.
     *
     * @param id the id of the memoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the memoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MemoryDTO> getMemory(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Memory : {}", id);
        Optional<MemoryDTO> memoryDTO = memoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(memoryDTO);
    }

    /**
     * {@code DELETE  /memories/:id} : delete the "id" memory.
     *
     * @param id the id of the memoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMemory(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Memory : {}", id);
        memoryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
