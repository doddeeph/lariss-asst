package id.lariss.web.rest;

import id.lariss.repository.DescriptionRepository;
import id.lariss.service.DescriptionService;
import id.lariss.service.dto.DescriptionDTO;
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
 * REST controller for managing {@link id.lariss.domain.Description}.
 */
@RestController
@RequestMapping("/api/descriptions")
public class DescriptionResource {

    private static final Logger LOG = LoggerFactory.getLogger(DescriptionResource.class);

    private static final String ENTITY_NAME = "description";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DescriptionService descriptionService;

    private final DescriptionRepository descriptionRepository;

    public DescriptionResource(DescriptionService descriptionService, DescriptionRepository descriptionRepository) {
        this.descriptionService = descriptionService;
        this.descriptionRepository = descriptionRepository;
    }

    /**
     * {@code POST  /descriptions} : Create a new description.
     *
     * @param descriptionDTO the descriptionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new descriptionDTO, or with status {@code 400 (Bad Request)} if the description has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DescriptionDTO> createDescription(@Valid @RequestBody DescriptionDTO descriptionDTO) throws URISyntaxException {
        LOG.debug("REST request to save Description : {}", descriptionDTO);
        if (descriptionDTO.getId() != null) {
            throw new BadRequestAlertException("A new description cannot already have an ID", ENTITY_NAME, "idexists");
        }
        descriptionDTO = descriptionService.save(descriptionDTO);
        return ResponseEntity.created(new URI("/api/descriptions/" + descriptionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, descriptionDTO.getId().toString()))
            .body(descriptionDTO);
    }

    /**
     * {@code PUT  /descriptions/:id} : Updates an existing description.
     *
     * @param id the id of the descriptionDTO to save.
     * @param descriptionDTO the descriptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated descriptionDTO,
     * or with status {@code 400 (Bad Request)} if the descriptionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the descriptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DescriptionDTO> updateDescription(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DescriptionDTO descriptionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Description : {}, {}", id, descriptionDTO);
        if (descriptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, descriptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!descriptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        descriptionDTO = descriptionService.update(descriptionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, descriptionDTO.getId().toString()))
            .body(descriptionDTO);
    }

    /**
     * {@code PATCH  /descriptions/:id} : Partial updates given fields of an existing description, field will ignore if it is null
     *
     * @param id the id of the descriptionDTO to save.
     * @param descriptionDTO the descriptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated descriptionDTO,
     * or with status {@code 400 (Bad Request)} if the descriptionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the descriptionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the descriptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DescriptionDTO> partialUpdateDescription(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DescriptionDTO descriptionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Description partially : {}, {}", id, descriptionDTO);
        if (descriptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, descriptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!descriptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DescriptionDTO> result = descriptionService.partialUpdate(descriptionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, descriptionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /descriptions} : get all the descriptions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of descriptions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DescriptionDTO>> getAllDescriptions(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Descriptions");
        Page<DescriptionDTO> page = descriptionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /descriptions/:id} : get the "id" description.
     *
     * @param id the id of the descriptionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the descriptionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DescriptionDTO> getDescription(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Description : {}", id);
        Optional<DescriptionDTO> descriptionDTO = descriptionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(descriptionDTO);
    }

    /**
     * {@code DELETE  /descriptions/:id} : delete the "id" description.
     *
     * @param id the id of the descriptionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDescription(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Description : {}", id);
        descriptionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
