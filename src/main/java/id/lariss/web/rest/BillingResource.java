package id.lariss.web.rest;

import id.lariss.repository.BillingRepository;
import id.lariss.service.BillingService;
import id.lariss.service.dto.BillingDTO;
import id.lariss.web.rest.errors.BadRequestAlertException;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link id.lariss.domain.Billing}.
 */
@RestController
@RequestMapping("/api/billings")
public class BillingResource {

    private static final Logger LOG = LoggerFactory.getLogger(BillingResource.class);

    private static final String ENTITY_NAME = "billing";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BillingService billingService;

    private final BillingRepository billingRepository;

    public BillingResource(BillingService billingService, BillingRepository billingRepository) {
        this.billingService = billingService;
        this.billingRepository = billingRepository;
    }

    /**
     * {@code POST  /billings} : Create a new billing.
     *
     * @param billingDTO the billingDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new billingDTO, or with status {@code 400 (Bad Request)} if the billing has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BillingDTO> createBilling(@RequestBody BillingDTO billingDTO) throws URISyntaxException {
        LOG.debug("REST request to save Billing : {}", billingDTO);
        if (billingDTO.getId() != null) {
            throw new BadRequestAlertException("A new billing cannot already have an ID", ENTITY_NAME, "idexists");
        }
        billingDTO = billingService.save(billingDTO);
        return ResponseEntity.created(new URI("/api/billings/" + billingDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, billingDTO.getId().toString()))
            .body(billingDTO);
    }

    /**
     * {@code PUT  /billings/:id} : Updates an existing billing.
     *
     * @param id the id of the billingDTO to save.
     * @param billingDTO the billingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated billingDTO,
     * or with status {@code 400 (Bad Request)} if the billingDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the billingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BillingDTO> updateBilling(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BillingDTO billingDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Billing : {}, {}", id, billingDTO);
        if (billingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, billingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!billingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        billingDTO = billingService.update(billingDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, billingDTO.getId().toString()))
            .body(billingDTO);
    }

    /**
     * {@code PATCH  /billings/:id} : Partial updates given fields of an existing billing, field will ignore if it is null
     *
     * @param id the id of the billingDTO to save.
     * @param billingDTO the billingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated billingDTO,
     * or with status {@code 400 (Bad Request)} if the billingDTO is not valid,
     * or with status {@code 404 (Not Found)} if the billingDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the billingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BillingDTO> partialUpdateBilling(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BillingDTO billingDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Billing partially : {}, {}", id, billingDTO);
        if (billingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, billingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!billingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BillingDTO> result = billingService.partialUpdate(billingDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, billingDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /billings} : get all the billings.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of billings in body.
     */
    @GetMapping("")
    public ResponseEntity<List<BillingDTO>> getAllBillings(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "filter", required = false) String filter
    ) {
        if ("order-is-null".equals(filter)) {
            LOG.debug("REST request to get all Billings where order is null");
            return new ResponseEntity<>(billingService.findAllWhereOrderIsNull(), HttpStatus.OK);
        }
        LOG.debug("REST request to get a page of Billings");
        Page<BillingDTO> page = billingService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /billings/:id} : get the "id" billing.
     *
     * @param id the id of the billingDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the billingDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BillingDTO> getBilling(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Billing : {}", id);
        Optional<BillingDTO> billingDTO = billingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(billingDTO);
    }

    /**
     * {@code DELETE  /billings/:id} : delete the "id" billing.
     *
     * @param id the id of the billingDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBilling(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Billing : {}", id);
        billingService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
