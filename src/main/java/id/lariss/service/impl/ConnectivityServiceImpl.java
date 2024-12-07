package id.lariss.service.impl;

import id.lariss.domain.Connectivity;
import id.lariss.repository.ConnectivityRepository;
import id.lariss.service.ConnectivityService;
import id.lariss.service.dto.ConnectivityDTO;
import id.lariss.service.mapper.ConnectivityMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link id.lariss.domain.Connectivity}.
 */
@Service
@Transactional
public class ConnectivityServiceImpl implements ConnectivityService {

    private static final Logger LOG = LoggerFactory.getLogger(ConnectivityServiceImpl.class);

    private final ConnectivityRepository connectivityRepository;

    private final ConnectivityMapper connectivityMapper;

    public ConnectivityServiceImpl(ConnectivityRepository connectivityRepository, ConnectivityMapper connectivityMapper) {
        this.connectivityRepository = connectivityRepository;
        this.connectivityMapper = connectivityMapper;
    }

    @Override
    public ConnectivityDTO save(ConnectivityDTO connectivityDTO) {
        LOG.debug("Request to save Connectivity : {}", connectivityDTO);
        Connectivity connectivity = connectivityMapper.toEntity(connectivityDTO);
        connectivity = connectivityRepository.save(connectivity);
        return connectivityMapper.toDto(connectivity);
    }

    @Override
    public ConnectivityDTO update(ConnectivityDTO connectivityDTO) {
        LOG.debug("Request to update Connectivity : {}", connectivityDTO);
        Connectivity connectivity = connectivityMapper.toEntity(connectivityDTO);
        connectivity = connectivityRepository.save(connectivity);
        return connectivityMapper.toDto(connectivity);
    }

    @Override
    public Optional<ConnectivityDTO> partialUpdate(ConnectivityDTO connectivityDTO) {
        LOG.debug("Request to partially update Connectivity : {}", connectivityDTO);

        return connectivityRepository
            .findById(connectivityDTO.getId())
            .map(existingConnectivity -> {
                connectivityMapper.partialUpdate(existingConnectivity, connectivityDTO);

                return existingConnectivity;
            })
            .map(connectivityRepository::save)
            .map(connectivityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ConnectivityDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Connectivities");
        return connectivityRepository.findAll(pageable).map(connectivityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ConnectivityDTO> findOne(Long id) {
        LOG.debug("Request to get Connectivity : {}", id);
        return connectivityRepository.findById(id).map(connectivityMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Connectivity : {}", id);
        connectivityRepository.deleteById(id);
    }
}
