package id.lariss.service.impl;

import id.lariss.domain.StrapSize;
import id.lariss.repository.StrapSizeRepository;
import id.lariss.service.StrapSizeService;
import id.lariss.service.dto.StrapSizeDTO;
import id.lariss.service.mapper.StrapSizeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link id.lariss.domain.StrapSize}.
 */
@Service
@Transactional
public class StrapSizeServiceImpl implements StrapSizeService {

    private static final Logger LOG = LoggerFactory.getLogger(StrapSizeServiceImpl.class);

    private final StrapSizeRepository strapSizeRepository;

    private final StrapSizeMapper strapSizeMapper;

    public StrapSizeServiceImpl(StrapSizeRepository strapSizeRepository, StrapSizeMapper strapSizeMapper) {
        this.strapSizeRepository = strapSizeRepository;
        this.strapSizeMapper = strapSizeMapper;
    }

    @Override
    public StrapSizeDTO save(StrapSizeDTO strapSizeDTO) {
        LOG.debug("Request to save StrapSize : {}", strapSizeDTO);
        StrapSize strapSize = strapSizeMapper.toEntity(strapSizeDTO);
        strapSize = strapSizeRepository.save(strapSize);
        return strapSizeMapper.toDto(strapSize);
    }

    @Override
    public StrapSizeDTO update(StrapSizeDTO strapSizeDTO) {
        LOG.debug("Request to update StrapSize : {}", strapSizeDTO);
        StrapSize strapSize = strapSizeMapper.toEntity(strapSizeDTO);
        strapSize = strapSizeRepository.save(strapSize);
        return strapSizeMapper.toDto(strapSize);
    }

    @Override
    public Optional<StrapSizeDTO> partialUpdate(StrapSizeDTO strapSizeDTO) {
        LOG.debug("Request to partially update StrapSize : {}", strapSizeDTO);

        return strapSizeRepository
            .findById(strapSizeDTO.getId())
            .map(existingStrapSize -> {
                strapSizeMapper.partialUpdate(existingStrapSize, strapSizeDTO);

                return existingStrapSize;
            })
            .map(strapSizeRepository::save)
            .map(strapSizeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StrapSizeDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all StrapSizes");
        return strapSizeRepository.findAll(pageable).map(strapSizeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StrapSizeDTO> findOne(Long id) {
        LOG.debug("Request to get StrapSize : {}", id);
        return strapSizeRepository.findById(id).map(strapSizeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete StrapSize : {}", id);
        strapSizeRepository.deleteById(id);
    }
}
