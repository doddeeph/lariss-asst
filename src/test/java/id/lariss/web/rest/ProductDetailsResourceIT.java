package id.lariss.web.rest;

import static id.lariss.domain.ProductDetailsAsserts.*;
import static id.lariss.web.rest.TestUtil.createUpdateProxyForBean;
import static id.lariss.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.lariss.IntegrationTest;
import id.lariss.domain.ProductDetails;
import id.lariss.repository.ProductDetailsRepository;
import id.lariss.service.ProductDetailsService;
import id.lariss.service.dto.ProductDetailsDTO;
import id.lariss.service.mapper.ProductDetailsMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ProductDetailsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProductDetailsResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);

    private static final String DEFAULT_THUMBNAIL = "AAAAAAAAAA";
    private static final String UPDATED_THUMBNAIL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/product-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductDetailsRepository productDetailsRepository;

    @Mock
    private ProductDetailsRepository productDetailsRepositoryMock;

    @Autowired
    private ProductDetailsMapper productDetailsMapper;

    @Mock
    private ProductDetailsService productDetailsServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductDetailsMockMvc;

    private ProductDetails productDetails;

    private ProductDetails insertedProductDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductDetails createEntity() {
        return new ProductDetails().name(DEFAULT_NAME).price(DEFAULT_PRICE).thumbnail(DEFAULT_THUMBNAIL);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductDetails createUpdatedEntity() {
        return new ProductDetails().name(UPDATED_NAME).price(UPDATED_PRICE).thumbnail(UPDATED_THUMBNAIL);
    }

    @BeforeEach
    public void initTest() {
        productDetails = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedProductDetails != null) {
            productDetailsRepository.delete(insertedProductDetails);
            insertedProductDetails = null;
        }
    }

    @Test
    @Transactional
    void createProductDetails() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProductDetails
        ProductDetailsDTO productDetailsDTO = productDetailsMapper.toDto(productDetails);
        var returnedProductDetailsDTO = om.readValue(
            restProductDetailsMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productDetailsDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductDetailsDTO.class
        );

        // Validate the ProductDetails in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProductDetails = productDetailsMapper.toEntity(returnedProductDetailsDTO);
        assertProductDetailsUpdatableFieldsEquals(returnedProductDetails, getPersistedProductDetails(returnedProductDetails));

        insertedProductDetails = returnedProductDetails;
    }

    @Test
    @Transactional
    void createProductDetailsWithExistingId() throws Exception {
        // Create the ProductDetails with an existing ID
        productDetails.setId(1L);
        ProductDetailsDTO productDetailsDTO = productDetailsMapper.toDto(productDetails);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productDetailsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProductDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productDetails.setName(null);

        // Create the ProductDetails, which fails.
        ProductDetailsDTO productDetailsDTO = productDetailsMapper.toDto(productDetails);

        restProductDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productDetailsDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productDetails.setPrice(null);

        // Create the ProductDetails, which fails.
        ProductDetailsDTO productDetailsDTO = productDetailsMapper.toDto(productDetails);

        restProductDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productDetailsDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkThumbnailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productDetails.setThumbnail(null);

        // Create the ProductDetails, which fails.
        ProductDetailsDTO productDetailsDTO = productDetailsMapper.toDto(productDetails);

        restProductDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productDetailsDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProductDetails() throws Exception {
        // Initialize the database
        insertedProductDetails = productDetailsRepository.saveAndFlush(productDetails);

        // Get all the productDetailsList
        restProductDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].thumbnail").value(hasItem(DEFAULT_THUMBNAIL)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductDetailsWithEagerRelationshipsIsEnabled() throws Exception {
        when(productDetailsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductDetailsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(productDetailsServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductDetailsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(productDetailsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductDetailsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(productDetailsRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getProductDetails() throws Exception {
        // Initialize the database
        insertedProductDetails = productDetailsRepository.saveAndFlush(productDetails);

        // Get the productDetails
        restProductDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, productDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productDetails.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.thumbnail").value(DEFAULT_THUMBNAIL));
    }

    @Test
    @Transactional
    void getNonExistingProductDetails() throws Exception {
        // Get the productDetails
        restProductDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductDetails() throws Exception {
        // Initialize the database
        insertedProductDetails = productDetailsRepository.saveAndFlush(productDetails);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productDetails
        ProductDetails updatedProductDetails = productDetailsRepository.findById(productDetails.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProductDetails are not directly saved in db
        em.detach(updatedProductDetails);
        updatedProductDetails.name(UPDATED_NAME).price(UPDATED_PRICE).thumbnail(UPDATED_THUMBNAIL);
        ProductDetailsDTO productDetailsDTO = productDetailsMapper.toDto(updatedProductDetails);

        restProductDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productDetailsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productDetailsDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProductDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductDetailsToMatchAllProperties(updatedProductDetails);
    }

    @Test
    @Transactional
    void putNonExistingProductDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productDetails.setId(longCount.incrementAndGet());

        // Create the ProductDetails
        ProductDetailsDTO productDetailsDTO = productDetailsMapper.toDto(productDetails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productDetailsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productDetails.setId(longCount.incrementAndGet());

        // Create the ProductDetails
        ProductDetailsDTO productDetailsDTO = productDetailsMapper.toDto(productDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productDetails.setId(longCount.incrementAndGet());

        // Create the ProductDetails
        ProductDetailsDTO productDetailsDTO = productDetailsMapper.toDto(productDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductDetailsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productDetailsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductDetailsWithPatch() throws Exception {
        // Initialize the database
        insertedProductDetails = productDetailsRepository.saveAndFlush(productDetails);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productDetails using partial update
        ProductDetails partialUpdatedProductDetails = new ProductDetails();
        partialUpdatedProductDetails.setId(productDetails.getId());

        partialUpdatedProductDetails.price(UPDATED_PRICE).thumbnail(UPDATED_THUMBNAIL);

        restProductDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductDetails))
            )
            .andExpect(status().isOk());

        // Validate the ProductDetails in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductDetailsUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductDetails, productDetails),
            getPersistedProductDetails(productDetails)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductDetailsWithPatch() throws Exception {
        // Initialize the database
        insertedProductDetails = productDetailsRepository.saveAndFlush(productDetails);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productDetails using partial update
        ProductDetails partialUpdatedProductDetails = new ProductDetails();
        partialUpdatedProductDetails.setId(productDetails.getId());

        partialUpdatedProductDetails.name(UPDATED_NAME).price(UPDATED_PRICE).thumbnail(UPDATED_THUMBNAIL);

        restProductDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductDetails))
            )
            .andExpect(status().isOk());

        // Validate the ProductDetails in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductDetailsUpdatableFieldsEquals(partialUpdatedProductDetails, getPersistedProductDetails(partialUpdatedProductDetails));
    }

    @Test
    @Transactional
    void patchNonExistingProductDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productDetails.setId(longCount.incrementAndGet());

        // Create the ProductDetails
        ProductDetailsDTO productDetailsDTO = productDetailsMapper.toDto(productDetails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productDetailsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productDetails.setId(longCount.incrementAndGet());

        // Create the ProductDetails
        ProductDetailsDTO productDetailsDTO = productDetailsMapper.toDto(productDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productDetails.setId(longCount.incrementAndGet());

        // Create the ProductDetails
        ProductDetailsDTO productDetailsDTO = productDetailsMapper.toDto(productDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductDetailsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productDetailsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductDetails() throws Exception {
        // Initialize the database
        insertedProductDetails = productDetailsRepository.saveAndFlush(productDetails);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the productDetails
        restProductDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, productDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productDetailsRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected ProductDetails getPersistedProductDetails(ProductDetails productDetails) {
        return productDetailsRepository.findById(productDetails.getId()).orElseThrow();
    }

    protected void assertPersistedProductDetailsToMatchAllProperties(ProductDetails expectedProductDetails) {
        assertProductDetailsAllPropertiesEquals(expectedProductDetails, getPersistedProductDetails(expectedProductDetails));
    }

    protected void assertPersistedProductDetailsToMatchUpdatableProperties(ProductDetails expectedProductDetails) {
        assertProductDetailsAllUpdatablePropertiesEquals(expectedProductDetails, getPersistedProductDetails(expectedProductDetails));
    }
}
