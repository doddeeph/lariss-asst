package id.lariss.service.impl;

import id.lariss.domain.Storage;
import id.lariss.repository.StorageRepository;
import id.lariss.service.StorageService;
import id.lariss.service.dto.StorageDTO;
import id.lariss.service.mapper.StorageMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link id.lariss.domain.Storage}.
 */
@Service
@Transactional
public class StorageServiceImpl implements StorageService {

    private static final Logger LOG = LoggerFactory.getLogger(StorageServiceImpl.class);

    private final StorageRepository storageRepository;

    private final StorageMapper storageMapper;

    public StorageServiceImpl(StorageRepository storageRepository, StorageMapper storageMapper) {
        this.storageRepository = storageRepository;
        this.storageMapper = storageMapper;
    }

    @Override
    public StorageDTO save(StorageDTO storageDTO) {
        LOG.debug("Request to save Storage : {}", storageDTO);
        Storage storage = storageMapper.toEntity(storageDTO);
        storage = storageRepository.save(storage);
        return storageMapper.toDto(storage);
    }

    @Override
    public StorageDTO update(StorageDTO storageDTO) {
        LOG.debug("Request to update Storage : {}", storageDTO);
        Storage storage = storageMapper.toEntity(storageDTO);
        storage = storageRepository.save(storage);
        return storageMapper.toDto(storage);
    }

    @Override
    public Optional<StorageDTO> partialUpdate(StorageDTO storageDTO) {
        LOG.debug("Request to partially update Storage : {}", storageDTO);

        return storageRepository
            .findById(storageDTO.getId())
            .map(existingStorage -> {
                storageMapper.partialUpdate(existingStorage, storageDTO);

                return existingStorage;
            })
            .map(storageRepository::save)
            .map(storageMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StorageDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Storages");
        return storageRepository.findAll(pageable).map(storageMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StorageDTO> findOne(Long id) {
        LOG.debug("Request to get Storage : {}", id);
        return storageRepository.findById(id).map(storageMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Storage : {}", id);
        storageRepository.deleteById(id);
    }
}
