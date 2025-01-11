package id.lariss.web.rest;

import static id.lariss.domain.BillingAsserts.*;
import static id.lariss.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.lariss.IntegrationTest;
import id.lariss.domain.Billing;
import id.lariss.repository.BillingRepository;
import id.lariss.service.dto.BillingDTO;
import id.lariss.service.mapper.BillingMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link BillingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BillingResourceIT {

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

    private static final String ENTITY_API_URL = "/api/billings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BillingRepository billingRepository;

    @Autowired
    private BillingMapper billingMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBillingMockMvc;

    private Billing billing;

    private Billing insertedBilling;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Billing createEntity() {
        return new Billing()
            .streetAddress(DEFAULT_STREET_ADDRESS)
            .city(DEFAULT_CITY)
            .state(DEFAULT_STATE)
            .postalCode(DEFAULT_POSTAL_CODE)
            .country(DEFAULT_COUNTRY);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Billing createUpdatedEntity() {
        return new Billing()
            .streetAddress(UPDATED_STREET_ADDRESS)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .postalCode(UPDATED_POSTAL_CODE)
            .country(UPDATED_COUNTRY);
    }

    @BeforeEach
    public void initTest() {
        billing = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedBilling != null) {
            billingRepository.delete(insertedBilling);
            insertedBilling = null;
        }
    }

    @Test
    @Transactional
    void createBilling() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Billing
        BillingDTO billingDTO = billingMapper.toDto(billing);
        var returnedBillingDTO = om.readValue(
            restBillingMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(billingDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BillingDTO.class
        );

        // Validate the Billing in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBilling = billingMapper.toEntity(returnedBillingDTO);
        assertBillingUpdatableFieldsEquals(returnedBilling, getPersistedBilling(returnedBilling));

        insertedBilling = returnedBilling;
    }

    @Test
    @Transactional
    void createBillingWithExistingId() throws Exception {
        // Create the Billing with an existing ID
        billing.setId(1L);
        BillingDTO billingDTO = billingMapper.toDto(billing);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBillingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(billingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Billing in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBillings() throws Exception {
        // Initialize the database
        insertedBilling = billingRepository.saveAndFlush(billing);

        // Get all the billingList
        restBillingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(billing.getId().intValue())))
            .andExpect(jsonPath("$.[*].streetAddress").value(hasItem(DEFAULT_STREET_ADDRESS)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)));
    }

    @Test
    @Transactional
    void getBilling() throws Exception {
        // Initialize the database
        insertedBilling = billingRepository.saveAndFlush(billing);

        // Get the billing
        restBillingMockMvc
            .perform(get(ENTITY_API_URL_ID, billing.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(billing.getId().intValue()))
            .andExpect(jsonPath("$.streetAddress").value(DEFAULT_STREET_ADDRESS))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE))
            .andExpect(jsonPath("$.postalCode").value(DEFAULT_POSTAL_CODE))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY));
    }

    @Test
    @Transactional
    void getNonExistingBilling() throws Exception {
        // Get the billing
        restBillingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBilling() throws Exception {
        // Initialize the database
        insertedBilling = billingRepository.saveAndFlush(billing);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the billing
        Billing updatedBilling = billingRepository.findById(billing.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBilling are not directly saved in db
        em.detach(updatedBilling);
        updatedBilling
            .streetAddress(UPDATED_STREET_ADDRESS)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .postalCode(UPDATED_POSTAL_CODE)
            .country(UPDATED_COUNTRY);
        BillingDTO billingDTO = billingMapper.toDto(updatedBilling);

        restBillingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, billingDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(billingDTO))
            )
            .andExpect(status().isOk());

        // Validate the Billing in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBillingToMatchAllProperties(updatedBilling);
    }

    @Test
    @Transactional
    void putNonExistingBilling() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        billing.setId(longCount.incrementAndGet());

        // Create the Billing
        BillingDTO billingDTO = billingMapper.toDto(billing);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBillingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, billingDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(billingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Billing in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBilling() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        billing.setId(longCount.incrementAndGet());

        // Create the Billing
        BillingDTO billingDTO = billingMapper.toDto(billing);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(billingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Billing in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBilling() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        billing.setId(longCount.incrementAndGet());

        // Create the Billing
        BillingDTO billingDTO = billingMapper.toDto(billing);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillingMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(billingDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Billing in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBillingWithPatch() throws Exception {
        // Initialize the database
        insertedBilling = billingRepository.saveAndFlush(billing);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the billing using partial update
        Billing partialUpdatedBilling = new Billing();
        partialUpdatedBilling.setId(billing.getId());

        partialUpdatedBilling
            .streetAddress(UPDATED_STREET_ADDRESS)
            .city(UPDATED_CITY)
            .postalCode(UPDATED_POSTAL_CODE)
            .country(UPDATED_COUNTRY);

        restBillingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBilling.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBilling))
            )
            .andExpect(status().isOk());

        // Validate the Billing in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBillingUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedBilling, billing), getPersistedBilling(billing));
    }

    @Test
    @Transactional
    void fullUpdateBillingWithPatch() throws Exception {
        // Initialize the database
        insertedBilling = billingRepository.saveAndFlush(billing);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the billing using partial update
        Billing partialUpdatedBilling = new Billing();
        partialUpdatedBilling.setId(billing.getId());

        partialUpdatedBilling
            .streetAddress(UPDATED_STREET_ADDRESS)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .postalCode(UPDATED_POSTAL_CODE)
            .country(UPDATED_COUNTRY);

        restBillingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBilling.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBilling))
            )
            .andExpect(status().isOk());

        // Validate the Billing in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBillingUpdatableFieldsEquals(partialUpdatedBilling, getPersistedBilling(partialUpdatedBilling));
    }

    @Test
    @Transactional
    void patchNonExistingBilling() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        billing.setId(longCount.incrementAndGet());

        // Create the Billing
        BillingDTO billingDTO = billingMapper.toDto(billing);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBillingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, billingDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(billingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Billing in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBilling() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        billing.setId(longCount.incrementAndGet());

        // Create the Billing
        BillingDTO billingDTO = billingMapper.toDto(billing);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(billingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Billing in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBilling() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        billing.setId(longCount.incrementAndGet());

        // Create the Billing
        BillingDTO billingDTO = billingMapper.toDto(billing);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillingMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(billingDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Billing in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBilling() throws Exception {
        // Initialize the database
        insertedBilling = billingRepository.saveAndFlush(billing);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the billing
        restBillingMockMvc
            .perform(delete(ENTITY_API_URL_ID, billing.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return billingRepository.count();
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

    protected Billing getPersistedBilling(Billing billing) {
        return billingRepository.findById(billing.getId()).orElseThrow();
    }

    protected void assertPersistedBillingToMatchAllProperties(Billing expectedBilling) {
        assertBillingAllPropertiesEquals(expectedBilling, getPersistedBilling(expectedBilling));
    }

    protected void assertPersistedBillingToMatchUpdatableProperties(Billing expectedBilling) {
        assertBillingAllUpdatablePropertiesEquals(expectedBilling, getPersistedBilling(expectedBilling));
    }
}
