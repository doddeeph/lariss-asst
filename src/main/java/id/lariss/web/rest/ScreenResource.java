package id.lariss.web.rest;

import id.lariss.repository.ScreenRepository;
import id.lariss.service.ScreenService;
import id.lariss.service.dto.ScreenDTO;
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
 * REST controller for managing {@link id.lariss.domain.Screen}.
 */
@RestController
@RequestMapping("/api/screens")
public class ScreenResource {

    private static final Logger LOG = LoggerFactory.getLogger(ScreenResource.class);

    private static final String ENTITY_NAME = "screen";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ScreenService screenService;

    private final ScreenRepository screenRepository;

    public ScreenResource(ScreenService screenService, ScreenRepository screenRepository) {
        this.screenService = screenService;
        this.screenRepository = screenRepository;
    }

    /**
     * {@code POST  /screens} : Create a new screen.
     *
     * @param screenDTO the screenDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new screenDTO, or with status {@code 400 (Bad Request)} if the screen has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ScreenDTO> createScreen(@Valid @RequestBody ScreenDTO screenDTO) throws URISyntaxException {
        LOG.debug("REST request to save Screen : {}", screenDTO);
        if (screenDTO.getId() != null) {
            throw new BadRequestAlertException("A new screen cannot already have an ID", ENTITY_NAME, "idexists");
        }
        screenDTO = screenService.save(screenDTO);
        return ResponseEntity.created(new URI("/api/screens/" + screenDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, screenDTO.getId().toString()))
            .body(screenDTO);
    }

    /**
     * {@code PUT  /screens/:id} : Updates an existing screen.
     *
     * @param id the id of the screenDTO to save.
     * @param screenDTO the screenDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated screenDTO,
     * or with status {@code 400 (Bad Request)} if the screenDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the screenDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ScreenDTO> updateScreen(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ScreenDTO screenDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Screen : {}, {}", id, screenDTO);
        if (screenDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, screenDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!screenRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        screenDTO = screenService.update(screenDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, screenDTO.getId().toString()))
            .body(screenDTO);
    }

    /**
     * {@code PATCH  /screens/:id} : Partial updates given fields of an existing screen, field will ignore if it is null
     *
     * @param id the id of the screenDTO to save.
     * @param screenDTO the screenDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated screenDTO,
     * or with status {@code 400 (Bad Request)} if the screenDTO is not valid,
     * or with status {@code 404 (Not Found)} if the screenDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the screenDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ScreenDTO> partialUpdateScreen(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ScreenDTO screenDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Screen partially : {}, {}", id, screenDTO);
        if (screenDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, screenDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!screenRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ScreenDTO> result = screenService.partialUpdate(screenDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, screenDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /screens} : get all the screens.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of screens in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ScreenDTO>> getAllScreens(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Screens");
        Page<ScreenDTO> page = screenService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /screens/:id} : get the "id" screen.
     *
     * @param id the id of the screenDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the screenDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ScreenDTO> getScreen(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Screen : {}", id);
        Optional<ScreenDTO> screenDTO = screenService.findOne(id);
        return ResponseUtil.wrapOrNotFound(screenDTO);
    }

    /**
     * {@code DELETE  /screens/:id} : delete the "id" screen.
     *
     * @param id the id of the screenDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScreen(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Screen : {}", id);
        screenService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
