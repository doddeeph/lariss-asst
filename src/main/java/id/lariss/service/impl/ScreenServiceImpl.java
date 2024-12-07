package id.lariss.service.impl;

import id.lariss.domain.Screen;
import id.lariss.repository.ScreenRepository;
import id.lariss.service.ScreenService;
import id.lariss.service.dto.ScreenDTO;
import id.lariss.service.mapper.ScreenMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link id.lariss.domain.Screen}.
 */
@Service
@Transactional
public class ScreenServiceImpl implements ScreenService {

    private static final Logger LOG = LoggerFactory.getLogger(ScreenServiceImpl.class);

    private final ScreenRepository screenRepository;

    private final ScreenMapper screenMapper;

    public ScreenServiceImpl(ScreenRepository screenRepository, ScreenMapper screenMapper) {
        this.screenRepository = screenRepository;
        this.screenMapper = screenMapper;
    }

    @Override
    public ScreenDTO save(ScreenDTO screenDTO) {
        LOG.debug("Request to save Screen : {}", screenDTO);
        Screen screen = screenMapper.toEntity(screenDTO);
        screen = screenRepository.save(screen);
        return screenMapper.toDto(screen);
    }

    @Override
    public ScreenDTO update(ScreenDTO screenDTO) {
        LOG.debug("Request to update Screen : {}", screenDTO);
        Screen screen = screenMapper.toEntity(screenDTO);
        screen = screenRepository.save(screen);
        return screenMapper.toDto(screen);
    }

    @Override
    public Optional<ScreenDTO> partialUpdate(ScreenDTO screenDTO) {
        LOG.debug("Request to partially update Screen : {}", screenDTO);

        return screenRepository
            .findById(screenDTO.getId())
            .map(existingScreen -> {
                screenMapper.partialUpdate(existingScreen, screenDTO);

                return existingScreen;
            })
            .map(screenRepository::save)
            .map(screenMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ScreenDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Screens");
        return screenRepository.findAll(pageable).map(screenMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ScreenDTO> findOne(Long id) {
        LOG.debug("Request to get Screen : {}", id);
        return screenRepository.findById(id).map(screenMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Screen : {}", id);
        screenRepository.deleteById(id);
    }
}
