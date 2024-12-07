package id.lariss.web.rest;

import id.lariss.repository.StrapColorRepository;
import id.lariss.service.StrapColorService;
import id.lariss.service.dto.StrapColorDTO;
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
 * REST controller for managing {@link id.lariss.domain.StrapColor}.
 */
@RestController
@RequestMapping("/api/strap-colors")
public class StrapColorResource {

    private static final Logger LOG = LoggerFactory.getLogger(StrapColorResource.class);

    private static final String ENTITY_NAME = "strapColor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StrapColorService strapColorService;

    private final StrapColorRepository strapColorRepository;

    public StrapColorResource(StrapColorService strapColorService, StrapColorRepository strapColorRepository) {
        this.strapColorService = strapColorService;
        this.strapColorRepository = strapColorRepository;
    }

    /**
     * {@code POST  /strap-colors} : Create a new strapColor.
     *
     * @param strapColorDTO the strapColorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new strapColorDTO, or with status {@code 400 (Bad Request)} if the strapColor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<StrapColorDTO> createStrapColor(@Valid @RequestBody StrapColorDTO strapColorDTO) throws URISyntaxException {
        LOG.debug("REST request to save StrapColor : {}", strapColorDTO);
        if (strapColorDTO.getId() != null) {
            throw new BadRequestAlertException("A new strapColor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        strapColorDTO = strapColorService.save(strapColorDTO);
        return ResponseEntity.created(new URI("/api/strap-colors/" + strapColorDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, strapColorDTO.getId().toString()))
            .body(strapColorDTO);
    }

    /**
     * {@code PUT  /strap-colors/:id} : Updates an existing strapColor.
     *
     * @param id the id of the strapColorDTO to save.
     * @param strapColorDTO the strapColorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated strapColorDTO,
     * or with status {@code 400 (Bad Request)} if the strapColorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the strapColorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<StrapColorDTO> updateStrapColor(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody StrapColorDTO strapColorDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update StrapColor : {}, {}", id, strapColorDTO);
        if (strapColorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, strapColorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!strapColorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        strapColorDTO = strapColorService.update(strapColorDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, strapColorDTO.getId().toString()))
            .body(strapColorDTO);
    }

    /**
     * {@code PATCH  /strap-colors/:id} : Partial updates given fields of an existing strapColor, field will ignore if it is null
     *
     * @param id the id of the strapColorDTO to save.
     * @param strapColorDTO the strapColorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated strapColorDTO,
     * or with status {@code 400 (Bad Request)} if the strapColorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the strapColorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the strapColorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StrapColorDTO> partialUpdateStrapColor(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody StrapColorDTO strapColorDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update StrapColor partially : {}, {}", id, strapColorDTO);
        if (strapColorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, strapColorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!strapColorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StrapColorDTO> result = strapColorService.partialUpdate(strapColorDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, strapColorDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /strap-colors} : get all the strapColors.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of strapColors in body.
     */
    @GetMapping("")
    public ResponseEntity<List<StrapColorDTO>> getAllStrapColors(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of StrapColors");
        Page<StrapColorDTO> page = strapColorService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /strap-colors/:id} : get the "id" strapColor.
     *
     * @param id the id of the strapColorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the strapColorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StrapColorDTO> getStrapColor(@PathVariable("id") Long id) {
        LOG.debug("REST request to get StrapColor : {}", id);
        Optional<StrapColorDTO> strapColorDTO = strapColorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(strapColorDTO);
    }

    /**
     * {@code DELETE  /strap-colors/:id} : delete the "id" strapColor.
     *
     * @param id the id of the strapColorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStrapColor(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete StrapColor : {}", id);
        strapColorService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
