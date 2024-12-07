package id.lariss.web.rest;

import id.lariss.repository.StrapSizeRepository;
import id.lariss.service.StrapSizeService;
import id.lariss.service.dto.StrapSizeDTO;
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
 * REST controller for managing {@link id.lariss.domain.StrapSize}.
 */
@RestController
@RequestMapping("/api/strap-sizes")
public class StrapSizeResource {

    private static final Logger LOG = LoggerFactory.getLogger(StrapSizeResource.class);

    private static final String ENTITY_NAME = "strapSize";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StrapSizeService strapSizeService;

    private final StrapSizeRepository strapSizeRepository;

    public StrapSizeResource(StrapSizeService strapSizeService, StrapSizeRepository strapSizeRepository) {
        this.strapSizeService = strapSizeService;
        this.strapSizeRepository = strapSizeRepository;
    }

    /**
     * {@code POST  /strap-sizes} : Create a new strapSize.
     *
     * @param strapSizeDTO the strapSizeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new strapSizeDTO, or with status {@code 400 (Bad Request)} if the strapSize has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<StrapSizeDTO> createStrapSize(@Valid @RequestBody StrapSizeDTO strapSizeDTO) throws URISyntaxException {
        LOG.debug("REST request to save StrapSize : {}", strapSizeDTO);
        if (strapSizeDTO.getId() != null) {
            throw new BadRequestAlertException("A new strapSize cannot already have an ID", ENTITY_NAME, "idexists");
        }
        strapSizeDTO = strapSizeService.save(strapSizeDTO);
        return ResponseEntity.created(new URI("/api/strap-sizes/" + strapSizeDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, strapSizeDTO.getId().toString()))
            .body(strapSizeDTO);
    }

    /**
     * {@code PUT  /strap-sizes/:id} : Updates an existing strapSize.
     *
     * @param id the id of the strapSizeDTO to save.
     * @param strapSizeDTO the strapSizeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated strapSizeDTO,
     * or with status {@code 400 (Bad Request)} if the strapSizeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the strapSizeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<StrapSizeDTO> updateStrapSize(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody StrapSizeDTO strapSizeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update StrapSize : {}, {}", id, strapSizeDTO);
        if (strapSizeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, strapSizeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!strapSizeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        strapSizeDTO = strapSizeService.update(strapSizeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, strapSizeDTO.getId().toString()))
            .body(strapSizeDTO);
    }

    /**
     * {@code PATCH  /strap-sizes/:id} : Partial updates given fields of an existing strapSize, field will ignore if it is null
     *
     * @param id the id of the strapSizeDTO to save.
     * @param strapSizeDTO the strapSizeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated strapSizeDTO,
     * or with status {@code 400 (Bad Request)} if the strapSizeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the strapSizeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the strapSizeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StrapSizeDTO> partialUpdateStrapSize(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody StrapSizeDTO strapSizeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update StrapSize partially : {}, {}", id, strapSizeDTO);
        if (strapSizeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, strapSizeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!strapSizeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StrapSizeDTO> result = strapSizeService.partialUpdate(strapSizeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, strapSizeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /strap-sizes} : get all the strapSizes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of strapSizes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<StrapSizeDTO>> getAllStrapSizes(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of StrapSizes");
        Page<StrapSizeDTO> page = strapSizeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /strap-sizes/:id} : get the "id" strapSize.
     *
     * @param id the id of the strapSizeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the strapSizeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StrapSizeDTO> getStrapSize(@PathVariable("id") Long id) {
        LOG.debug("REST request to get StrapSize : {}", id);
        Optional<StrapSizeDTO> strapSizeDTO = strapSizeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(strapSizeDTO);
    }

    /**
     * {@code DELETE  /strap-sizes/:id} : delete the "id" strapSize.
     *
     * @param id the id of the strapSizeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStrapSize(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete StrapSize : {}", id);
        strapSizeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
