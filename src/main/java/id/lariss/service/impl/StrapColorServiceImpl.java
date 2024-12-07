package id.lariss.service.impl;

import id.lariss.domain.StrapColor;
import id.lariss.repository.StrapColorRepository;
import id.lariss.service.StrapColorService;
import id.lariss.service.dto.StrapColorDTO;
import id.lariss.service.mapper.StrapColorMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link id.lariss.domain.StrapColor}.
 */
@Service
@Transactional
public class StrapColorServiceImpl implements StrapColorService {

    private static final Logger LOG = LoggerFactory.getLogger(StrapColorServiceImpl.class);

    private final StrapColorRepository strapColorRepository;

    private final StrapColorMapper strapColorMapper;

    public StrapColorServiceImpl(StrapColorRepository strapColorRepository, StrapColorMapper strapColorMapper) {
        this.strapColorRepository = strapColorRepository;
        this.strapColorMapper = strapColorMapper;
    }

    @Override
    public StrapColorDTO save(StrapColorDTO strapColorDTO) {
        LOG.debug("Request to save StrapColor : {}", strapColorDTO);
        StrapColor strapColor = strapColorMapper.toEntity(strapColorDTO);
        strapColor = strapColorRepository.save(strapColor);
        return strapColorMapper.toDto(strapColor);
    }

    @Override
    public StrapColorDTO update(StrapColorDTO strapColorDTO) {
        LOG.debug("Request to update StrapColor : {}", strapColorDTO);
        StrapColor strapColor = strapColorMapper.toEntity(strapColorDTO);
        strapColor = strapColorRepository.save(strapColor);
        return strapColorMapper.toDto(strapColor);
    }

    @Override
    public Optional<StrapColorDTO> partialUpdate(StrapColorDTO strapColorDTO) {
        LOG.debug("Request to partially update StrapColor : {}", strapColorDTO);

        return strapColorRepository
            .findById(strapColorDTO.getId())
            .map(existingStrapColor -> {
                strapColorMapper.partialUpdate(existingStrapColor, strapColorDTO);

                return existingStrapColor;
            })
            .map(strapColorRepository::save)
            .map(strapColorMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StrapColorDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all StrapColors");
        return strapColorRepository.findAll(pageable).map(strapColorMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StrapColorDTO> findOne(Long id) {
        LOG.debug("Request to get StrapColor : {}", id);
        return strapColorRepository.findById(id).map(strapColorMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete StrapColor : {}", id);
        strapColorRepository.deleteById(id);
    }
}
