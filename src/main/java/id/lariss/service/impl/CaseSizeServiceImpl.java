package id.lariss.service.impl;

import id.lariss.domain.CaseSize;
import id.lariss.repository.CaseSizeRepository;
import id.lariss.service.CaseSizeService;
import id.lariss.service.dto.CaseSizeDTO;
import id.lariss.service.mapper.CaseSizeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link id.lariss.domain.CaseSize}.
 */
@Service
@Transactional
public class CaseSizeServiceImpl implements CaseSizeService {

    private static final Logger LOG = LoggerFactory.getLogger(CaseSizeServiceImpl.class);

    private final CaseSizeRepository caseSizeRepository;

    private final CaseSizeMapper caseSizeMapper;

    public CaseSizeServiceImpl(CaseSizeRepository caseSizeRepository, CaseSizeMapper caseSizeMapper) {
        this.caseSizeRepository = caseSizeRepository;
        this.caseSizeMapper = caseSizeMapper;
    }

    @Override
    public CaseSizeDTO save(CaseSizeDTO caseSizeDTO) {
        LOG.debug("Request to save CaseSize : {}", caseSizeDTO);
        CaseSize caseSize = caseSizeMapper.toEntity(caseSizeDTO);
        caseSize = caseSizeRepository.save(caseSize);
        return caseSizeMapper.toDto(caseSize);
    }

    @Override
    public CaseSizeDTO update(CaseSizeDTO caseSizeDTO) {
        LOG.debug("Request to update CaseSize : {}", caseSizeDTO);
        CaseSize caseSize = caseSizeMapper.toEntity(caseSizeDTO);
        caseSize = caseSizeRepository.save(caseSize);
        return caseSizeMapper.toDto(caseSize);
    }

    @Override
    public Optional<CaseSizeDTO> partialUpdate(CaseSizeDTO caseSizeDTO) {
        LOG.debug("Request to partially update CaseSize : {}", caseSizeDTO);

        return caseSizeRepository
            .findById(caseSizeDTO.getId())
            .map(existingCaseSize -> {
                caseSizeMapper.partialUpdate(existingCaseSize, caseSizeDTO);

                return existingCaseSize;
            })
            .map(caseSizeRepository::save)
            .map(caseSizeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CaseSizeDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all CaseSizes");
        return caseSizeRepository.findAll(pageable).map(caseSizeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CaseSizeDTO> findOne(Long id) {
        LOG.debug("Request to get CaseSize : {}", id);
        return caseSizeRepository.findById(id).map(caseSizeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete CaseSize : {}", id);
        caseSizeRepository.deleteById(id);
    }
}
