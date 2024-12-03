package id.lariss.service.impl;

import id.lariss.domain.Processor;
import id.lariss.repository.ProcessorRepository;
import id.lariss.service.ProcessorService;
import id.lariss.service.dto.ProcessorDTO;
import id.lariss.service.mapper.ProcessorMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link id.lariss.domain.Processor}.
 */
@Service
@Transactional
public class ProcessorServiceImpl implements ProcessorService {

    private static final Logger LOG = LoggerFactory.getLogger(ProcessorServiceImpl.class);

    private final ProcessorRepository processorRepository;

    private final ProcessorMapper processorMapper;

    public ProcessorServiceImpl(ProcessorRepository processorRepository, ProcessorMapper processorMapper) {
        this.processorRepository = processorRepository;
        this.processorMapper = processorMapper;
    }

    @Override
    public ProcessorDTO save(ProcessorDTO processorDTO) {
        LOG.debug("Request to save Processor : {}", processorDTO);
        Processor processor = processorMapper.toEntity(processorDTO);
        processor = processorRepository.save(processor);
        return processorMapper.toDto(processor);
    }

    @Override
    public ProcessorDTO update(ProcessorDTO processorDTO) {
        LOG.debug("Request to update Processor : {}", processorDTO);
        Processor processor = processorMapper.toEntity(processorDTO);
        processor = processorRepository.save(processor);
        return processorMapper.toDto(processor);
    }

    @Override
    public Optional<ProcessorDTO> partialUpdate(ProcessorDTO processorDTO) {
        LOG.debug("Request to partially update Processor : {}", processorDTO);

        return processorRepository
            .findById(processorDTO.getId())
            .map(existingProcessor -> {
                processorMapper.partialUpdate(existingProcessor, processorDTO);

                return existingProcessor;
            })
            .map(processorRepository::save)
            .map(processorMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProcessorDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Processors");
        return processorRepository.findAll(pageable).map(processorMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProcessorDTO> findOne(Long id) {
        LOG.debug("Request to get Processor : {}", id);
        return processorRepository.findById(id).map(processorMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Processor : {}", id);
        processorRepository.deleteById(id);
    }
}
