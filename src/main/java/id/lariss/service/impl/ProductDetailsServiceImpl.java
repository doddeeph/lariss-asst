package id.lariss.service.impl;

import id.lariss.domain.ProductDetails;
import id.lariss.repository.ProductDetailsRepository;
import id.lariss.service.ProductDetailsService;
import id.lariss.service.dto.ProductDetailsDTO;
import id.lariss.service.mapper.ProductDetailsMapper;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link id.lariss.domain.ProductDetails}.
 */
@Service
@Transactional
public class ProductDetailsServiceImpl implements ProductDetailsService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductDetailsServiceImpl.class);

    private final ProductDetailsRepository productDetailsRepository;

    private final ProductDetailsMapper productDetailsMapper;

    public ProductDetailsServiceImpl(ProductDetailsRepository productDetailsRepository, ProductDetailsMapper productDetailsMapper) {
        this.productDetailsRepository = productDetailsRepository;
        this.productDetailsMapper = productDetailsMapper;
    }

    @Override
    public ProductDetailsDTO save(ProductDetailsDTO productDetailsDTO) {
        LOG.debug("Request to save ProductDetails : {}", productDetailsDTO);
        ProductDetails productDetails = productDetailsMapper.toEntity(productDetailsDTO);
        productDetails = productDetailsRepository.save(productDetails);
        return productDetailsMapper.toDto(productDetails);
    }

    @Override
    public ProductDetailsDTO update(ProductDetailsDTO productDetailsDTO) {
        LOG.debug("Request to update ProductDetails : {}", productDetailsDTO);
        ProductDetails productDetails = productDetailsMapper.toEntity(productDetailsDTO);
        productDetails = productDetailsRepository.save(productDetails);
        return productDetailsMapper.toDto(productDetails);
    }

    @Override
    public Optional<ProductDetailsDTO> partialUpdate(ProductDetailsDTO productDetailsDTO) {
        LOG.debug("Request to partially update ProductDetails : {}", productDetailsDTO);

        return productDetailsRepository
            .findById(productDetailsDTO.getId())
            .map(existingProductDetails -> {
                productDetailsMapper.partialUpdate(existingProductDetails, productDetailsDTO);

                return existingProductDetails;
            })
            .map(productDetailsRepository::save)
            .map(productDetailsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDetailsDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ProductDetails");
        return productDetailsRepository.findAll(pageable).map(productDetailsMapper::toDto);
    }

    public Page<ProductDetailsDTO> findAllWithEagerRelationships(Pageable pageable) {
        return productDetailsRepository.findAllWithEagerRelationships(pageable).map(productDetailsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductDetailsDTO> findOne(Long id) {
        LOG.debug("Request to get ProductDetails : {}", id);
        return productDetailsRepository.findOneWithEagerRelationships(id).map(productDetailsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ProductDetails : {}", id);
        productDetailsRepository.deleteById(id);
    }

    @Override
    public List<ProductDetailsDTO> findAllByProductName(String name) {
        LOG.debug("Request to get all ProductDetails by product: {}", name);
        return productDetailsRepository.findAllByProductName(name.toLowerCase()).stream().map(productDetailsMapper::toDto).toList();
    }

    @Override
    public List<ProductDetailsDTO> findAllByProductNameIn(List<String> names) {
        LOG.debug("Request to get all ProductDetails by products: {}", names);
        return productDetailsRepository.findAllByProductNameIn(names).stream().map(productDetailsMapper::toDto).toList();
    }

    @Override
    public List<ProductDetailsDTO> findLowestPriceByProductNameIn(List<String> names) {
        LOG.debug("Request to get lowest price ProductDetails by products: {}", names);
        return productDetailsRepository.findLowestPriceByProductNameIn(names).stream().map(productDetailsMapper::toDto).toList();
    }
}
