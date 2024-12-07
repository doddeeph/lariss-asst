package id.lariss.web.rest;

import static id.lariss.domain.CaseSizeAsserts.*;
import static id.lariss.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.lariss.IntegrationTest;
import id.lariss.domain.CaseSize;
import id.lariss.repository.CaseSizeRepository;
import id.lariss.service.dto.CaseSizeDTO;
import id.lariss.service.mapper.CaseSizeMapper;
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
 * Integration tests for the {@link CaseSizeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CaseSizeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/case-sizes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CaseSizeRepository caseSizeRepository;

    @Autowired
    private CaseSizeMapper caseSizeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCaseSizeMockMvc;

    private CaseSize caseSize;

    private CaseSize insertedCaseSize;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CaseSize createEntity() {
        return new CaseSize().name(DEFAULT_NAME).value(DEFAULT_VALUE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CaseSize createUpdatedEntity() {
        return new CaseSize().name(UPDATED_NAME).value(UPDATED_VALUE);
    }

    @BeforeEach
    public void initTest() {
        caseSize = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCaseSize != null) {
            caseSizeRepository.delete(insertedCaseSize);
            insertedCaseSize = null;
        }
    }

    @Test
    @Transactional
    void createCaseSize() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CaseSize
        CaseSizeDTO caseSizeDTO = caseSizeMapper.toDto(caseSize);
        var returnedCaseSizeDTO = om.readValue(
            restCaseSizeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(caseSizeDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CaseSizeDTO.class
        );

        // Validate the CaseSize in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCaseSize = caseSizeMapper.toEntity(returnedCaseSizeDTO);
        assertCaseSizeUpdatableFieldsEquals(returnedCaseSize, getPersistedCaseSize(returnedCaseSize));

        insertedCaseSize = returnedCaseSize;
    }

    @Test
    @Transactional
    void createCaseSizeWithExistingId() throws Exception {
        // Create the CaseSize with an existing ID
        caseSize.setId(1L);
        CaseSizeDTO caseSizeDTO = caseSizeMapper.toDto(caseSize);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCaseSizeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(caseSizeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CaseSize in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        caseSize.setName(null);

        // Create the CaseSize, which fails.
        CaseSizeDTO caseSizeDTO = caseSizeMapper.toDto(caseSize);

        restCaseSizeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(caseSizeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCaseSizes() throws Exception {
        // Initialize the database
        insertedCaseSize = caseSizeRepository.saveAndFlush(caseSize);

        // Get all the caseSizeList
        restCaseSizeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(caseSize.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    @Test
    @Transactional
    void getCaseSize() throws Exception {
        // Initialize the database
        insertedCaseSize = caseSizeRepository.saveAndFlush(caseSize);

        // Get the caseSize
        restCaseSizeMockMvc
            .perform(get(ENTITY_API_URL_ID, caseSize.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(caseSize.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }

    @Test
    @Transactional
    void getNonExistingCaseSize() throws Exception {
        // Get the caseSize
        restCaseSizeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCaseSize() throws Exception {
        // Initialize the database
        insertedCaseSize = caseSizeRepository.saveAndFlush(caseSize);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the caseSize
        CaseSize updatedCaseSize = caseSizeRepository.findById(caseSize.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCaseSize are not directly saved in db
        em.detach(updatedCaseSize);
        updatedCaseSize.name(UPDATED_NAME).value(UPDATED_VALUE);
        CaseSizeDTO caseSizeDTO = caseSizeMapper.toDto(updatedCaseSize);

        restCaseSizeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, caseSizeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(caseSizeDTO))
            )
            .andExpect(status().isOk());

        // Validate the CaseSize in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCaseSizeToMatchAllProperties(updatedCaseSize);
    }

    @Test
    @Transactional
    void putNonExistingCaseSize() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        caseSize.setId(longCount.incrementAndGet());

        // Create the CaseSize
        CaseSizeDTO caseSizeDTO = caseSizeMapper.toDto(caseSize);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCaseSizeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, caseSizeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(caseSizeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CaseSize in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCaseSize() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        caseSize.setId(longCount.incrementAndGet());

        // Create the CaseSize
        CaseSizeDTO caseSizeDTO = caseSizeMapper.toDto(caseSize);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCaseSizeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(caseSizeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CaseSize in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCaseSize() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        caseSize.setId(longCount.incrementAndGet());

        // Create the CaseSize
        CaseSizeDTO caseSizeDTO = caseSizeMapper.toDto(caseSize);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCaseSizeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(caseSizeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CaseSize in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCaseSizeWithPatch() throws Exception {
        // Initialize the database
        insertedCaseSize = caseSizeRepository.saveAndFlush(caseSize);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the caseSize using partial update
        CaseSize partialUpdatedCaseSize = new CaseSize();
        partialUpdatedCaseSize.setId(caseSize.getId());

        partialUpdatedCaseSize.name(UPDATED_NAME);

        restCaseSizeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCaseSize.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCaseSize))
            )
            .andExpect(status().isOk());

        // Validate the CaseSize in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCaseSizeUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCaseSize, caseSize), getPersistedCaseSize(caseSize));
    }

    @Test
    @Transactional
    void fullUpdateCaseSizeWithPatch() throws Exception {
        // Initialize the database
        insertedCaseSize = caseSizeRepository.saveAndFlush(caseSize);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the caseSize using partial update
        CaseSize partialUpdatedCaseSize = new CaseSize();
        partialUpdatedCaseSize.setId(caseSize.getId());

        partialUpdatedCaseSize.name(UPDATED_NAME).value(UPDATED_VALUE);

        restCaseSizeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCaseSize.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCaseSize))
            )
            .andExpect(status().isOk());

        // Validate the CaseSize in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCaseSizeUpdatableFieldsEquals(partialUpdatedCaseSize, getPersistedCaseSize(partialUpdatedCaseSize));
    }

    @Test
    @Transactional
    void patchNonExistingCaseSize() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        caseSize.setId(longCount.incrementAndGet());

        // Create the CaseSize
        CaseSizeDTO caseSizeDTO = caseSizeMapper.toDto(caseSize);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCaseSizeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, caseSizeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(caseSizeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CaseSize in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCaseSize() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        caseSize.setId(longCount.incrementAndGet());

        // Create the CaseSize
        CaseSizeDTO caseSizeDTO = caseSizeMapper.toDto(caseSize);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCaseSizeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(caseSizeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CaseSize in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCaseSize() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        caseSize.setId(longCount.incrementAndGet());

        // Create the CaseSize
        CaseSizeDTO caseSizeDTO = caseSizeMapper.toDto(caseSize);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCaseSizeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(caseSizeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CaseSize in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCaseSize() throws Exception {
        // Initialize the database
        insertedCaseSize = caseSizeRepository.saveAndFlush(caseSize);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the caseSize
        restCaseSizeMockMvc
            .perform(delete(ENTITY_API_URL_ID, caseSize.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return caseSizeRepository.count();
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

    protected CaseSize getPersistedCaseSize(CaseSize caseSize) {
        return caseSizeRepository.findById(caseSize.getId()).orElseThrow();
    }

    protected void assertPersistedCaseSizeToMatchAllProperties(CaseSize expectedCaseSize) {
        assertCaseSizeAllPropertiesEquals(expectedCaseSize, getPersistedCaseSize(expectedCaseSize));
    }

    protected void assertPersistedCaseSizeToMatchUpdatableProperties(CaseSize expectedCaseSize) {
        assertCaseSizeAllUpdatablePropertiesEquals(expectedCaseSize, getPersistedCaseSize(expectedCaseSize));
    }
}
