package id.lariss.service.impl;

import id.lariss.domain.Shipping;
import id.lariss.repository.ShippingRepository;
import id.lariss.service.ShippingService;
import id.lariss.service.dto.ShippingDTO;
import id.lariss.service.mapper.ShippingMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link id.lariss.domain.Shipping}.
 */
@Service
@Transactional
public class ShippingServiceImpl implements ShippingService {

    private static final Logger LOG = LoggerFactory.getLogger(ShippingServiceImpl.class);

    private final ShippingRepository shippingRepository;

    private final ShippingMapper shippingMapper;

    public ShippingServiceImpl(ShippingRepository shippingRepository, ShippingMapper shippingMapper) {
        this.shippingRepository = shippingRepository;
        this.shippingMapper = shippingMapper;
    }

    @Override
    public ShippingDTO save(ShippingDTO shippingDTO) {
        LOG.debug("Request to save Shipping : {}", shippingDTO);
        Shipping shipping = shippingMapper.toEntity(shippingDTO);
        shipping = shippingRepository.save(shipping);
        return shippingMapper.toDto(shipping);
    }

    @Override
    public ShippingDTO update(ShippingDTO shippingDTO) {
        LOG.debug("Request to update Shipping : {}", shippingDTO);
        Shipping shipping = shippingMapper.toEntity(shippingDTO);
        shipping = shippingRepository.save(shipping);
        return shippingMapper.toDto(shipping);
    }

    @Override
    public Optional<ShippingDTO> partialUpdate(ShippingDTO shippingDTO) {
        LOG.debug("Request to partially update Shipping : {}", shippingDTO);

        return shippingRepository
            .findById(shippingDTO.getId())
            .map(existingShipping -> {
                shippingMapper.partialUpdate(existingShipping, shippingDTO);

                return existingShipping;
            })
            .map(shippingRepository::save)
            .map(shippingMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ShippingDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Shippings");
        return shippingRepository.findAll(pageable).map(shippingMapper::toDto);
    }

    /**
     *  Get all the shippings where Order is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ShippingDTO> findAllWhereOrderIsNull() {
        LOG.debug("Request to get all shippings where Order is null");
        return StreamSupport.stream(shippingRepository.findAll().spliterator(), false)
            .filter(shipping -> shipping.getOrder() == null)
            .map(shippingMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ShippingDTO> findOne(Long id) {
        LOG.debug("Request to get Shipping : {}", id);
        return shippingRepository.findById(id).map(shippingMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Shipping : {}", id);
        shippingRepository.deleteById(id);
    }
}
