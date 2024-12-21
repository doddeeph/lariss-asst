package id.lariss.service;

import id.lariss.service.dto.OrderProductDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link id.lariss.domain.OrderProduct}.
 */
public interface OrderProductService {
    /**
     * Save a orderProduct.
     *
     * @param orderProductDTO the entity to save.
     * @return the persisted entity.
     */
    OrderProductDTO save(OrderProductDTO orderProductDTO);

    /**
     * Updates a orderProduct.
     *
     * @param orderProductDTO the entity to update.
     * @return the persisted entity.
     */
    OrderProductDTO update(OrderProductDTO orderProductDTO);

    /**
     * Partially updates a orderProduct.
     *
     * @param orderProductDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OrderProductDTO> partialUpdate(OrderProductDTO orderProductDTO);

    /**
     * Get all the orderProducts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OrderProductDTO> findAll(Pageable pageable);

    /**
     * Get the "id" orderProduct.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrderProductDTO> findOne(Long id);

    /**
     * Delete the "id" orderProduct.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
