package id.lariss.web.rest;

import id.lariss.repository.ProcessorRepository;
import id.lariss.service.ProcessorService;
import id.lariss.service.dto.ProcessorDTO;
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
 * REST controller for managing {@link id.lariss.domain.Processor}.
 */
@RestController
@RequestMapping("/api/processors")
public class ProcessorResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProcessorResource.class);

    private static final String ENTITY_NAME = "processor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProcessorService processorService;

    private final ProcessorRepository processorRepository;

    public ProcessorResource(ProcessorService processorService, ProcessorRepository processorRepository) {
        this.processorService = processorService;
        this.processorRepository = processorRepository;
    }

    /**
     * {@code POST  /processors} : Create a new processor.
     *
     * @param processorDTO the processorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new processorDTO, or with status {@code 400 (Bad Request)} if the processor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProcessorDTO> createProcessor(@Valid @RequestBody ProcessorDTO processorDTO) throws URISyntaxException {
        LOG.debug("REST request to save Processor : {}", processorDTO);
        if (processorDTO.getId() != null) {
            throw new BadRequestAlertException("A new processor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        processorDTO = processorService.save(processorDTO);
        return ResponseEntity.created(new URI("/api/processors/" + processorDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, processorDTO.getId().toString()))
            .body(processorDTO);
    }

    /**
     * {@code PUT  /processors/:id} : Updates an existing processor.
     *
     * @param id the id of the processorDTO to save.
     * @param processorDTO the processorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated processorDTO,
     * or with status {@code 400 (Bad Request)} if the processorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the processorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProcessorDTO> updateProcessor(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProcessorDTO processorDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Processor : {}, {}", id, processorDTO);
        if (processorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, processorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!processorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        processorDTO = processorService.update(processorDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, processorDTO.getId().toString()))
            .body(processorDTO);
    }

    /**
     * {@code PATCH  /processors/:id} : Partial updates given fields of an existing processor, field will ignore if it is null
     *
     * @param id the id of the processorDTO to save.
     * @param processorDTO the processorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated processorDTO,
     * or with status {@code 400 (Bad Request)} if the processorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the processorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the processorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProcessorDTO> partialUpdateProcessor(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProcessorDTO processorDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Processor partially : {}, {}", id, processorDTO);
        if (processorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, processorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!processorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProcessorDTO> result = processorService.partialUpdate(processorDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, processorDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /processors} : get all the processors.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of processors in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProcessorDTO>> getAllProcessors(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Processors");
        Page<ProcessorDTO> page = processorService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /processors/:id} : get the "id" processor.
     *
     * @param id the id of the processorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the processorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProcessorDTO> getProcessor(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Processor : {}", id);
        Optional<ProcessorDTO> processorDTO = processorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(processorDTO);
    }

    /**
     * {@code DELETE  /processors/:id} : delete the "id" processor.
     *
     * @param id the id of the processorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProcessor(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Processor : {}", id);
        processorService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
