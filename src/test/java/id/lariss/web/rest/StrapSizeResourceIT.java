package id.lariss.web.rest;

import static id.lariss.domain.StrapSizeAsserts.*;
import static id.lariss.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.lariss.IntegrationTest;
import id.lariss.domain.StrapSize;
import id.lariss.repository.StrapSizeRepository;
import id.lariss.service.dto.StrapSizeDTO;
import id.lariss.service.mapper.StrapSizeMapper;
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
 * Integration tests for the {@link StrapSizeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StrapSizeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/strap-sizes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private StrapSizeRepository strapSizeRepository;

    @Autowired
    private StrapSizeMapper strapSizeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStrapSizeMockMvc;

    private StrapSize strapSize;

    private StrapSize insertedStrapSize;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StrapSize createEntity() {
        return new StrapSize().name(DEFAULT_NAME).value(DEFAULT_VALUE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StrapSize createUpdatedEntity() {
        return new StrapSize().name(UPDATED_NAME).value(UPDATED_VALUE);
    }

    @BeforeEach
    public void initTest() {
        strapSize = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedStrapSize != null) {
            strapSizeRepository.delete(insertedStrapSize);
            insertedStrapSize = null;
        }
    }

    @Test
    @Transactional
    void createStrapSize() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the StrapSize
        StrapSizeDTO strapSizeDTO = strapSizeMapper.toDto(strapSize);
        var returnedStrapSizeDTO = om.readValue(
            restStrapSizeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(strapSizeDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            StrapSizeDTO.class
        );

        // Validate the StrapSize in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedStrapSize = strapSizeMapper.toEntity(returnedStrapSizeDTO);
        assertStrapSizeUpdatableFieldsEquals(returnedStrapSize, getPersistedStrapSize(returnedStrapSize));

        insertedStrapSize = returnedStrapSize;
    }

    @Test
    @Transactional
    void createStrapSizeWithExistingId() throws Exception {
        // Create the StrapSize with an existing ID
        strapSize.setId(1L);
        StrapSizeDTO strapSizeDTO = strapSizeMapper.toDto(strapSize);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStrapSizeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(strapSizeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the StrapSize in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        strapSize.setName(null);

        // Create the StrapSize, which fails.
        StrapSizeDTO strapSizeDTO = strapSizeMapper.toDto(strapSize);

        restStrapSizeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(strapSizeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllStrapSizes() throws Exception {
        // Initialize the database
        insertedStrapSize = strapSizeRepository.saveAndFlush(strapSize);

        // Get all the strapSizeList
        restStrapSizeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(strapSize.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    @Test
    @Transactional
    void getStrapSize() throws Exception {
        // Initialize the database
        insertedStrapSize = strapSizeRepository.saveAndFlush(strapSize);

        // Get the strapSize
        restStrapSizeMockMvc
            .perform(get(ENTITY_API_URL_ID, strapSize.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(strapSize.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }

    @Test
    @Transactional
    void getNonExistingStrapSize() throws Exception {
        // Get the strapSize
        restStrapSizeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStrapSize() throws Exception {
        // Initialize the database
        insertedStrapSize = strapSizeRepository.saveAndFlush(strapSize);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the strapSize
        StrapSize updatedStrapSize = strapSizeRepository.findById(strapSize.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedStrapSize are not directly saved in db
        em.detach(updatedStrapSize);
        updatedStrapSize.name(UPDATED_NAME).value(UPDATED_VALUE);
        StrapSizeDTO strapSizeDTO = strapSizeMapper.toDto(updatedStrapSize);

        restStrapSizeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, strapSizeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(strapSizeDTO))
            )
            .andExpect(status().isOk());

        // Validate the StrapSize in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedStrapSizeToMatchAllProperties(updatedStrapSize);
    }

    @Test
    @Transactional
    void putNonExistingStrapSize() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        strapSize.setId(longCount.incrementAndGet());

        // Create the StrapSize
        StrapSizeDTO strapSizeDTO = strapSizeMapper.toDto(strapSize);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStrapSizeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, strapSizeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(strapSizeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StrapSize in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStrapSize() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        strapSize.setId(longCount.incrementAndGet());

        // Create the StrapSize
        StrapSizeDTO strapSizeDTO = strapSizeMapper.toDto(strapSize);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStrapSizeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(strapSizeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StrapSize in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStrapSize() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        strapSize.setId(longCount.incrementAndGet());

        // Create the StrapSize
        StrapSizeDTO strapSizeDTO = strapSizeMapper.toDto(strapSize);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStrapSizeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(strapSizeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StrapSize in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStrapSizeWithPatch() throws Exception {
        // Initialize the database
        insertedStrapSize = strapSizeRepository.saveAndFlush(strapSize);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the strapSize using partial update
        StrapSize partialUpdatedStrapSize = new StrapSize();
        partialUpdatedStrapSize.setId(strapSize.getId());

        restStrapSizeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStrapSize.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStrapSize))
            )
            .andExpect(status().isOk());

        // Validate the StrapSize in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStrapSizeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedStrapSize, strapSize),
            getPersistedStrapSize(strapSize)
        );
    }

    @Test
    @Transactional
    void fullUpdateStrapSizeWithPatch() throws Exception {
        // Initialize the database
        insertedStrapSize = strapSizeRepository.saveAndFlush(strapSize);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the strapSize using partial update
        StrapSize partialUpdatedStrapSize = new StrapSize();
        partialUpdatedStrapSize.setId(strapSize.getId());

        partialUpdatedStrapSize.name(UPDATED_NAME).value(UPDATED_VALUE);

        restStrapSizeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStrapSize.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStrapSize))
            )
            .andExpect(status().isOk());

        // Validate the StrapSize in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStrapSizeUpdatableFieldsEquals(partialUpdatedStrapSize, getPersistedStrapSize(partialUpdatedStrapSize));
    }

    @Test
    @Transactional
    void patchNonExistingStrapSize() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        strapSize.setId(longCount.incrementAndGet());

        // Create the StrapSize
        StrapSizeDTO strapSizeDTO = strapSizeMapper.toDto(strapSize);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStrapSizeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, strapSizeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(strapSizeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StrapSize in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStrapSize() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        strapSize.setId(longCount.incrementAndGet());

        // Create the StrapSize
        StrapSizeDTO strapSizeDTO = strapSizeMapper.toDto(strapSize);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStrapSizeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(strapSizeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StrapSize in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStrapSize() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        strapSize.setId(longCount.incrementAndGet());

        // Create the StrapSize
        StrapSizeDTO strapSizeDTO = strapSizeMapper.toDto(strapSize);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStrapSizeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(strapSizeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StrapSize in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStrapSize() throws Exception {
        // Initialize the database
        insertedStrapSize = strapSizeRepository.saveAndFlush(strapSize);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the strapSize
        restStrapSizeMockMvc
            .perform(delete(ENTITY_API_URL_ID, strapSize.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return strapSizeRepository.count();
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

    protected StrapSize getPersistedStrapSize(StrapSize strapSize) {
        return strapSizeRepository.findById(strapSize.getId()).orElseThrow();
    }

    protected void assertPersistedStrapSizeToMatchAllProperties(StrapSize expectedStrapSize) {
        assertStrapSizeAllPropertiesEquals(expectedStrapSize, getPersistedStrapSize(expectedStrapSize));
    }

    protected void assertPersistedStrapSizeToMatchUpdatableProperties(StrapSize expectedStrapSize) {
        assertStrapSizeAllUpdatablePropertiesEquals(expectedStrapSize, getPersistedStrapSize(expectedStrapSize));
    }
}
