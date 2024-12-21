package id.lariss.service.impl;

import id.lariss.domain.Billing;
import id.lariss.repository.BillingRepository;
import id.lariss.service.BillingService;
import id.lariss.service.dto.BillingDTO;
import id.lariss.service.mapper.BillingMapper;
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
 * Service Implementation for managing {@link id.lariss.domain.Billing}.
 */
@Service
@Transactional
public class BillingServiceImpl implements BillingService {

    private static final Logger LOG = LoggerFactory.getLogger(BillingServiceImpl.class);

    private final BillingRepository billingRepository;

    private final BillingMapper billingMapper;

    public BillingServiceImpl(BillingRepository billingRepository, BillingMapper billingMapper) {
        this.billingRepository = billingRepository;
        this.billingMapper = billingMapper;
    }

    @Override
    public BillingDTO save(BillingDTO billingDTO) {
        LOG.debug("Request to save Billing : {}", billingDTO);
        Billing billing = billingMapper.toEntity(billingDTO);
        billing = billingRepository.save(billing);
        return billingMapper.toDto(billing);
    }

    @Override
    public BillingDTO update(BillingDTO billingDTO) {
        LOG.debug("Request to update Billing : {}", billingDTO);
        Billing billing = billingMapper.toEntity(billingDTO);
        billing = billingRepository.save(billing);
        return billingMapper.toDto(billing);
    }

    @Override
    public Optional<BillingDTO> partialUpdate(BillingDTO billingDTO) {
        LOG.debug("Request to partially update Billing : {}", billingDTO);

        return billingRepository
            .findById(billingDTO.getId())
            .map(existingBilling -> {
                billingMapper.partialUpdate(existingBilling, billingDTO);

                return existingBilling;
            })
            .map(billingRepository::save)
            .map(billingMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BillingDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Billings");
        return billingRepository.findAll(pageable).map(billingMapper::toDto);
    }

    /**
     *  Get all the billings where Order is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<BillingDTO> findAllWhereOrderIsNull() {
        LOG.debug("Request to get all billings where Order is null");
        return StreamSupport.stream(billingRepository.findAll().spliterator(), false)
            .filter(billing -> billing.getOrder() == null)
            .map(billingMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BillingDTO> findOne(Long id) {
        LOG.debug("Request to get Billing : {}", id);
        return billingRepository.findById(id).map(billingMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Billing : {}", id);
        billingRepository.deleteById(id);
    }
}
