package id.lariss.web.rest;

import id.lariss.repository.OrderProductRepository;
import id.lariss.service.OrderProductService;
import id.lariss.service.dto.OrderProductDTO;
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
 * REST controller for managing {@link id.lariss.domain.OrderProduct}.
 */
@RestController
@RequestMapping("/api/order-products")
public class OrderProductResource {

    private static final Logger LOG = LoggerFactory.getLogger(OrderProductResource.class);

    private static final String ENTITY_NAME = "orderProduct";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrderProductService orderProductService;

    private final OrderProductRepository orderProductRepository;

    public OrderProductResource(OrderProductService orderProductService, OrderProductRepository orderProductRepository) {
        this.orderProductService = orderProductService;
        this.orderProductRepository = orderProductRepository;
    }

    /**
     * {@code POST  /order-products} : Create a new orderProduct.
     *
     * @param orderProductDTO the orderProductDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderProductDTO, or with status {@code 400 (Bad Request)} if the orderProduct has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OrderProductDTO> createOrderProduct(@Valid @RequestBody OrderProductDTO orderProductDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save OrderProduct : {}", orderProductDTO);
        if (orderProductDTO.getId() != null) {
            throw new BadRequestAlertException("A new orderProduct cannot already have an ID", ENTITY_NAME, "idexists");
        }
        orderProductDTO = orderProductService.save(orderProductDTO);
        return ResponseEntity.created(new URI("/api/order-products/" + orderProductDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, orderProductDTO.getId().toString()))
            .body(orderProductDTO);
    }

    /**
     * {@code PUT  /order-products/:id} : Updates an existing orderProduct.
     *
     * @param id the id of the orderProductDTO to save.
     * @param orderProductDTO the orderProductDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderProductDTO,
     * or with status {@code 400 (Bad Request)} if the orderProductDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderProductDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrderProductDTO> updateOrderProduct(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OrderProductDTO orderProductDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update OrderProduct : {}, {}", id, orderProductDTO);
        if (orderProductDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderProductDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderProductRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        orderProductDTO = orderProductService.update(orderProductDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderProductDTO.getId().toString()))
            .body(orderProductDTO);
    }

    /**
     * {@code PATCH  /order-products/:id} : Partial updates given fields of an existing orderProduct, field will ignore if it is null
     *
     * @param id the id of the orderProductDTO to save.
     * @param orderProductDTO the orderProductDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderProductDTO,
     * or with status {@code 400 (Bad Request)} if the orderProductDTO is not valid,
     * or with status {@code 404 (Not Found)} if the orderProductDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the orderProductDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrderProductDTO> partialUpdateOrderProduct(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OrderProductDTO orderProductDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update OrderProduct partially : {}, {}", id, orderProductDTO);
        if (orderProductDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderProductDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderProductRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrderProductDTO> result = orderProductService.partialUpdate(orderProductDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderProductDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /order-products} : get all the orderProducts.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orderProducts in body.
     */
    @GetMapping("")
    public ResponseEntity<List<OrderProductDTO>> getAllOrderProducts(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of OrderProducts");
        Page<OrderProductDTO> page = orderProductService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /order-products/:id} : get the "id" orderProduct.
     *
     * @param id the id of the orderProductDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderProductDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderProductDTO> getOrderProduct(@PathVariable("id") Long id) {
        LOG.debug("REST request to get OrderProduct : {}", id);
        Optional<OrderProductDTO> orderProductDTO = orderProductService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderProductDTO);
    }

    /**
     * {@code DELETE  /order-products/:id} : delete the "id" orderProduct.
     *
     * @param id the id of the orderProductDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderProduct(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete OrderProduct : {}", id);
        orderProductService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
