package id.lariss.service.impl;

import id.lariss.domain.Description;
import id.lariss.repository.DescriptionRepository;
import id.lariss.service.DescriptionService;
import id.lariss.service.dto.DescriptionDTO;
import id.lariss.service.mapper.DescriptionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link id.lariss.domain.Description}.
 */
@Service
@Transactional
public class DescriptionServiceImpl implements DescriptionService {

    private static final Logger LOG = LoggerFactory.getLogger(DescriptionServiceImpl.class);

    private final DescriptionRepository descriptionRepository;

    private final DescriptionMapper descriptionMapper;

    public DescriptionServiceImpl(DescriptionRepository descriptionRepository, DescriptionMapper descriptionMapper) {
        this.descriptionRepository = descriptionRepository;
        this.descriptionMapper = descriptionMapper;
    }

    @Override
    public DescriptionDTO save(DescriptionDTO descriptionDTO) {
        LOG.debug("Request to save Description : {}", descriptionDTO);
        Description description = descriptionMapper.toEntity(descriptionDTO);
        description = descriptionRepository.save(description);
        return descriptionMapper.toDto(description);
    }

    @Override
    public DescriptionDTO update(DescriptionDTO descriptionDTO) {
        LOG.debug("Request to update Description : {}", descriptionDTO);
        Description description = descriptionMapper.toEntity(descriptionDTO);
        description = descriptionRepository.save(description);
        return descriptionMapper.toDto(description);
    }

    @Override
    public Optional<DescriptionDTO> partialUpdate(DescriptionDTO descriptionDTO) {
        LOG.debug("Request to partially update Description : {}", descriptionDTO);

        return descriptionRepository
            .findById(descriptionDTO.getId())
            .map(existingDescription -> {
                descriptionMapper.partialUpdate(existingDescription, descriptionDTO);

                return existingDescription;
            })
            .map(descriptionRepository::save)
            .map(descriptionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DescriptionDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Descriptions");
        return descriptionRepository.findAll(pageable).map(descriptionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DescriptionDTO> findOne(Long id) {
        LOG.debug("Request to get Description : {}", id);
        return descriptionRepository.findById(id).map(descriptionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Description : {}", id);
        descriptionRepository.deleteById(id);
    }
}
