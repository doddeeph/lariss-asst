package id.lariss.service.impl;

import id.lariss.domain.OrderProduct;
import id.lariss.repository.OrderProductRepository;
import id.lariss.service.OrderProductService;
import id.lariss.service.dto.OrderProductDTO;
import id.lariss.service.mapper.OrderProductMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link id.lariss.domain.OrderProduct}.
 */
@Service
@Transactional
public class OrderProductServiceImpl implements OrderProductService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderProductServiceImpl.class);

    private final OrderProductRepository orderProductRepository;

    private final OrderProductMapper orderProductMapper;

    public OrderProductServiceImpl(OrderProductRepository orderProductRepository, OrderProductMapper orderProductMapper) {
        this.orderProductRepository = orderProductRepository;
        this.orderProductMapper = orderProductMapper;
    }

    @Override
    public OrderProductDTO save(OrderProductDTO orderProductDTO) {
        LOG.debug("Request to save OrderProduct : {}", orderProductDTO);
        OrderProduct orderProduct = orderProductMapper.toEntity(orderProductDTO);
        orderProduct = orderProductRepository.save(orderProduct);
        return orderProductMapper.toDto(orderProduct);
    }

    @Override
    public OrderProductDTO update(OrderProductDTO orderProductDTO) {
        LOG.debug("Request to update OrderProduct : {}", orderProductDTO);
        OrderProduct orderProduct = orderProductMapper.toEntity(orderProductDTO);
        orderProduct = orderProductRepository.save(orderProduct);
        return orderProductMapper.toDto(orderProduct);
    }

    @Override
    public Optional<OrderProductDTO> partialUpdate(OrderProductDTO orderProductDTO) {
        LOG.debug("Request to partially update OrderProduct : {}", orderProductDTO);

        return orderProductRepository
            .findById(orderProductDTO.getId())
            .map(existingOrderProduct -> {
                orderProductMapper.partialUpdate(existingOrderProduct, orderProductDTO);

                return existingOrderProduct;
            })
            .map(orderProductRepository::save)
            .map(orderProductMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderProductDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all OrderProducts");
        return orderProductRepository.findAll(pageable).map(orderProductMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrderProductDTO> findOne(Long id) {
        LOG.debug("Request to get OrderProduct : {}", id);
        return orderProductRepository.findById(id).map(orderProductMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete OrderProduct : {}", id);
        orderProductRepository.deleteById(id);
    }
}
