package id.lariss.service.impl;

import id.lariss.domain.Memory;
import id.lariss.repository.MemoryRepository;
import id.lariss.service.MemoryService;
import id.lariss.service.dto.MemoryDTO;
import id.lariss.service.mapper.MemoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link id.lariss.domain.Memory}.
 */
@Service
@Transactional
public class MemoryServiceImpl implements MemoryService {

    private static final Logger LOG = LoggerFactory.getLogger(MemoryServiceImpl.class);

    private final MemoryRepository memoryRepository;

    private final MemoryMapper memoryMapper;

    public MemoryServiceImpl(MemoryRepository memoryRepository, MemoryMapper memoryMapper) {
        this.memoryRepository = memoryRepository;
        this.memoryMapper = memoryMapper;
    }

    @Override
    public MemoryDTO save(MemoryDTO memoryDTO) {
        LOG.debug("Request to save Memory : {}", memoryDTO);
        Memory memory = memoryMapper.toEntity(memoryDTO);
        memory = memoryRepository.save(memory);
        return memoryMapper.toDto(memory);
    }

    @Override
    public MemoryDTO update(MemoryDTO memoryDTO) {
        LOG.debug("Request to update Memory : {}", memoryDTO);
        Memory memory = memoryMapper.toEntity(memoryDTO);
        memory = memoryRepository.save(memory);
        return memoryMapper.toDto(memory);
    }

    @Override
    public Optional<MemoryDTO> partialUpdate(MemoryDTO memoryDTO) {
        LOG.debug("Request to partially update Memory : {}", memoryDTO);

        return memoryRepository
            .findById(memoryDTO.getId())
            .map(existingMemory -> {
                memoryMapper.partialUpdate(existingMemory, memoryDTO);

                return existingMemory;
            })
            .map(memoryRepository::save)
            .map(memoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MemoryDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Memories");
        return memoryRepository.findAll(pageable).map(memoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MemoryDTO> findOne(Long id) {
        LOG.debug("Request to get Memory : {}", id);
        return memoryRepository.findById(id).map(memoryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Memory : {}", id);
        memoryRepository.deleteById(id);
    }
}
