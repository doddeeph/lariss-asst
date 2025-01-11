package id.lariss.web.rest;

import static id.lariss.domain.ShippingAsserts.*;
import static id.lariss.web.rest.TestUtil.createUpdateProxyForBean;
import static id.lariss.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.lariss.IntegrationTest;
import id.lariss.domain.Shipping;
import id.lariss.domain.enumeration.ShippingMethod;
import id.lariss.repository.ShippingRepository;
import id.lariss.service.dto.ShippingDTO;
import id.lariss.service.mapper.ShippingMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ShippingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ShippingResourceIT {

    private static final String DEFAULT_STREET_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_STREET_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_STATE = "BBBBBBBBBB";

    private static final String DEFAULT_POSTAL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_POSTAL_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final ShippingMethod DEFAULT_METHOD = ShippingMethod.STANDARD;
    private static final ShippingMethod UPDATED_METHOD = ShippingMethod.STANDARD;

    private static final BigDecimal DEFAULT_COST = new BigDecimal(1);
    private static final BigDecimal UPDATED_COST = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/shippings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ShippingRepository shippingRepository;

    @Autowired
    private ShippingMapper shippingMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restShippingMockMvc;

    private Shipping shipping;

    private Shipping insertedShipping;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Shipping createEntity() {
        return new Shipping()
            .streetAddress(DEFAULT_STREET_ADDRESS)
            .city(DEFAULT_CITY)
            .state(DEFAULT_STATE)
            .postalCode(DEFAULT_POSTAL_CODE)
            .country(DEFAULT_COUNTRY)
            .method(DEFAULT_METHOD)
            .cost(DEFAULT_COST);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Shipping createUpdatedEntity() {
        return new Shipping()
            .streetAddress(UPDATED_STREET_ADDRESS)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .postalCode(UPDATED_POSTAL_CODE)
            .country(UPDATED_COUNTRY)
            .method(UPDATED_METHOD)
            .cost(UPDATED_COST);
    }

    @BeforeEach
    public void initTest() {
        shipping = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedShipping != null) {
            shippingRepository.delete(insertedShipping);
            insertedShipping = null;
        }
    }

    @Test
    @Transactional
    void createShipping() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Shipping
        ShippingDTO shippingDTO = shippingMapper.toDto(shipping);
        var returnedShippingDTO = om.readValue(
            restShippingMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shippingDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ShippingDTO.class
        );

        // Validate the Shipping in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedShipping = shippingMapper.toEntity(returnedShippingDTO);
        assertShippingUpdatableFieldsEquals(returnedShipping, getPersistedShipping(returnedShipping));

        insertedShipping = returnedShipping;
    }

    @Test
    @Transactional
    void createShippingWithExistingId() throws Exception {
        // Create the Shipping with an existing ID
        shipping.setId(1L);
        ShippingDTO shippingDTO = shippingMapper.toDto(shipping);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restShippingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shippingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Shipping in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllShippings() throws Exception {
        // Initialize the database
        insertedShipping = shippingRepository.saveAndFlush(shipping);

        // Get all the shippingList
        restShippingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shipping.getId().intValue())))
            .andExpect(jsonPath("$.[*].streetAddress").value(hasItem(DEFAULT_STREET_ADDRESS)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].method").value(hasItem(DEFAULT_METHOD.toString())))
            .andExpect(jsonPath("$.[*].cost").value(hasItem(sameNumber(DEFAULT_COST))));
    }

    @Test
    @Transactional
    void getShipping() throws Exception {
        // Initialize the database
        insertedShipping = shippingRepository.saveAndFlush(shipping);

        // Get the shipping
        restShippingMockMvc
            .perform(get(ENTITY_API_URL_ID, shipping.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(shipping.getId().intValue()))
            .andExpect(jsonPath("$.streetAddress").value(DEFAULT_STREET_ADDRESS))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE))
            .andExpect(jsonPath("$.postalCode").value(DEFAULT_POSTAL_CODE))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.method").value(DEFAULT_METHOD.toString()))
            .andExpect(jsonPath("$.cost").value(sameNumber(DEFAULT_COST)));
    }

    @Test
    @Transactional
    void getNonExistingShipping() throws Exception {
        // Get the shipping
        restShippingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingShipping() throws Exception {
        // Initialize the database
        insertedShipping = shippingRepository.saveAndFlush(shipping);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shipping
        Shipping updatedShipping = shippingRepository.findById(shipping.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedShipping are not directly saved in db
        em.detach(updatedShipping);
        updatedShipping
            .streetAddress(UPDATED_STREET_ADDRESS)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .postalCode(UPDATED_POSTAL_CODE)
            .country(UPDATED_COUNTRY)
            .method(UPDATED_METHOD)
            .cost(UPDATED_COST);
        ShippingDTO shippingDTO = shippingMapper.toDto(updatedShipping);

        restShippingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shippingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shippingDTO))
            )
            .andExpect(status().isOk());

        // Validate the Shipping in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedShippingToMatchAllProperties(updatedShipping);
    }

    @Test
    @Transactional
    void putNonExistingShipping() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipping.setId(longCount.incrementAndGet());

        // Create the Shipping
        ShippingDTO shippingDTO = shippingMapper.toDto(shipping);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShippingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shippingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shippingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Shipping in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchShipping() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipping.setId(longCount.incrementAndGet());

        // Create the Shipping
        ShippingDTO shippingDTO = shippingMapper.toDto(shipping);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShippingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shippingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Shipping in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamShipping() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipping.setId(longCount.incrementAndGet());

        // Create the Shipping
        ShippingDTO shippingDTO = shippingMapper.toDto(shipping);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShippingMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shippingDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Shipping in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateShippingWithPatch() throws Exception {
        // Initialize the database
        insertedShipping = shippingRepository.saveAndFlush(shipping);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shipping using partial update
        Shipping partialUpdatedShipping = new Shipping();
        partialUpdatedShipping.setId(shipping.getId());

        partialUpdatedShipping.city(UPDATED_CITY).cost(UPDATED_COST);

        restShippingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShipping.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShipping))
            )
            .andExpect(status().isOk());

        // Validate the Shipping in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShippingUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedShipping, shipping), getPersistedShipping(shipping));
    }

    @Test
    @Transactional
    void fullUpdateShippingWithPatch() throws Exception {
        // Initialize the database
        insertedShipping = shippingRepository.saveAndFlush(shipping);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shipping using partial update
        Shipping partialUpdatedShipping = new Shipping();
        partialUpdatedShipping.setId(shipping.getId());

        partialUpdatedShipping
            .streetAddress(UPDATED_STREET_ADDRESS)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .postalCode(UPDATED_POSTAL_CODE)
            .country(UPDATED_COUNTRY)
            .method(UPDATED_METHOD)
            .cost(UPDATED_COST);

        restShippingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShipping.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShipping))
            )
            .andExpect(status().isOk());

        // Validate the Shipping in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShippingUpdatableFieldsEquals(partialUpdatedShipping, getPersistedShipping(partialUpdatedShipping));
    }

    @Test
    @Transactional
    void patchNonExistingShipping() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipping.setId(longCount.incrementAndGet());

        // Create the Shipping
        ShippingDTO shippingDTO = shippingMapper.toDto(shipping);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShippingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, shippingDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(shippingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Shipping in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchShipping() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipping.setId(longCount.incrementAndGet());

        // Create the Shipping
        ShippingDTO shippingDTO = shippingMapper.toDto(shipping);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShippingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(shippingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Shipping in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamShipping() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipping.setId(longCount.incrementAndGet());

        // Create the Shipping
        ShippingDTO shippingDTO = shippingMapper.toDto(shipping);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShippingMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(shippingDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Shipping in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteShipping() throws Exception {
        // Initialize the database
        insertedShipping = shippingRepository.saveAndFlush(shipping);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the shipping
        restShippingMockMvc
            .perform(delete(ENTITY_API_URL_ID, shipping.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return shippingRepository.count();
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

    protected Shipping getPersistedShipping(Shipping shipping) {
        return shippingRepository.findById(shipping.getId()).orElseThrow();
    }

    protected void assertPersistedShippingToMatchAllProperties(Shipping expectedShipping) {
        assertShippingAllPropertiesEquals(expectedShipping, getPersistedShipping(expectedShipping));
    }

    protected void assertPersistedShippingToMatchUpdatableProperties(Shipping expectedShipping) {
        assertShippingAllUpdatablePropertiesEquals(expectedShipping, getPersistedShipping(expectedShipping));
    }
}
