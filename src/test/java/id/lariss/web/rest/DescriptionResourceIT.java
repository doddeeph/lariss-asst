package id.lariss.web.rest;

import static id.lariss.domain.DescriptionAsserts.*;
import static id.lariss.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.lariss.IntegrationTest;
import id.lariss.domain.Description;
import id.lariss.repository.DescriptionRepository;
import id.lariss.service.dto.DescriptionDTO;
import id.lariss.service.mapper.DescriptionMapper;
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
 * Integration tests for the {@link DescriptionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DescriptionResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/descriptions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DescriptionRepository descriptionRepository;

    @Autowired
    private DescriptionMapper descriptionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDescriptionMockMvc;

    private Description description;

    private Description insertedDescription;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Description createEntity() {
        return new Description().name(DEFAULT_NAME).value(DEFAULT_VALUE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Description createUpdatedEntity() {
        return new Description().name(UPDATED_NAME).value(UPDATED_VALUE);
    }

    @BeforeEach
    public void initTest() {
        description = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedDescription != null) {
            descriptionRepository.delete(insertedDescription);
            insertedDescription = null;
        }
    }

    @Test
    @Transactional
    void createDescription() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Description
        DescriptionDTO descriptionDTO = descriptionMapper.toDto(description);
        var returnedDescriptionDTO = om.readValue(
            restDescriptionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(descriptionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DescriptionDTO.class
        );

        // Validate the Description in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDescription = descriptionMapper.toEntity(returnedDescriptionDTO);
        assertDescriptionUpdatableFieldsEquals(returnedDescription, getPersistedDescription(returnedDescription));

        insertedDescription = returnedDescription;
    }

    @Test
    @Transactional
    void createDescriptionWithExistingId() throws Exception {
        // Create the Description with an existing ID
        description.setId(1L);
        DescriptionDTO descriptionDTO = descriptionMapper.toDto(description);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDescriptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(descriptionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Description in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        description.setName(null);

        // Create the Description, which fails.
        DescriptionDTO descriptionDTO = descriptionMapper.toDto(description);

        restDescriptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(descriptionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDescriptions() throws Exception {
        // Initialize the database
        insertedDescription = descriptionRepository.saveAndFlush(description);

        // Get all the descriptionList
        restDescriptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(description.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    @Test
    @Transactional
    void getDescription() throws Exception {
        // Initialize the database
        insertedDescription = descriptionRepository.saveAndFlush(description);

        // Get the description
        restDescriptionMockMvc
            .perform(get(ENTITY_API_URL_ID, description.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(description.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }

    @Test
    @Transactional
    void getNonExistingDescription() throws Exception {
        // Get the description
        restDescriptionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDescription() throws Exception {
        // Initialize the database
        insertedDescription = descriptionRepository.saveAndFlush(description);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the description
        Description updatedDescription = descriptionRepository.findById(description.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDescription are not directly saved in db
        em.detach(updatedDescription);
        updatedDescription.name(UPDATED_NAME).value(UPDATED_VALUE);
        DescriptionDTO descriptionDTO = descriptionMapper.toDto(updatedDescription);

        restDescriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, descriptionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(descriptionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Description in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDescriptionToMatchAllProperties(updatedDescription);
    }

    @Test
    @Transactional
    void putNonExistingDescription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        description.setId(longCount.incrementAndGet());

        // Create the Description
        DescriptionDTO descriptionDTO = descriptionMapper.toDto(description);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDescriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, descriptionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(descriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Description in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDescription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        description.setId(longCount.incrementAndGet());

        // Create the Description
        DescriptionDTO descriptionDTO = descriptionMapper.toDto(description);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDescriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(descriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Description in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDescription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        description.setId(longCount.incrementAndGet());

        // Create the Description
        DescriptionDTO descriptionDTO = descriptionMapper.toDto(description);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDescriptionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(descriptionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Description in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDescriptionWithPatch() throws Exception {
        // Initialize the database
        insertedDescription = descriptionRepository.saveAndFlush(description);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the description using partial update
        Description partialUpdatedDescription = new Description();
        partialUpdatedDescription.setId(description.getId());

        partialUpdatedDescription.value(UPDATED_VALUE);

        restDescriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDescription.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDescription))
            )
            .andExpect(status().isOk());

        // Validate the Description in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDescriptionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDescription, description),
            getPersistedDescription(description)
        );
    }

    @Test
    @Transactional
    void fullUpdateDescriptionWithPatch() throws Exception {
        // Initialize the database
        insertedDescription = descriptionRepository.saveAndFlush(description);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the description using partial update
        Description partialUpdatedDescription = new Description();
        partialUpdatedDescription.setId(description.getId());

        partialUpdatedDescription.name(UPDATED_NAME).value(UPDATED_VALUE);

        restDescriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDescription.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDescription))
            )
            .andExpect(status().isOk());

        // Validate the Description in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDescriptionUpdatableFieldsEquals(partialUpdatedDescription, getPersistedDescription(partialUpdatedDescription));
    }

    @Test
    @Transactional
    void patchNonExistingDescription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        description.setId(longCount.incrementAndGet());

        // Create the Description
        DescriptionDTO descriptionDTO = descriptionMapper.toDto(description);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDescriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, descriptionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(descriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Description in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDescription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        description.setId(longCount.incrementAndGet());

        // Create the Description
        DescriptionDTO descriptionDTO = descriptionMapper.toDto(description);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDescriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(descriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Description in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDescription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        description.setId(longCount.incrementAndGet());

        // Create the Description
        DescriptionDTO descriptionDTO = descriptionMapper.toDto(description);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDescriptionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(descriptionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Description in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDescription() throws Exception {
        // Initialize the database
        insertedDescription = descriptionRepository.saveAndFlush(description);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the description
        restDescriptionMockMvc
            .perform(delete(ENTITY_API_URL_ID, description.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return descriptionRepository.count();
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

    protected Description getPersistedDescription(Description description) {
        return descriptionRepository.findById(description.getId()).orElseThrow();
    }

    protected void assertPersistedDescriptionToMatchAllProperties(Description expectedDescription) {
        assertDescriptionAllPropertiesEquals(expectedDescription, getPersistedDescription(expectedDescription));
    }

    protected void assertPersistedDescriptionToMatchUpdatableProperties(Description expectedDescription) {
        assertDescriptionAllUpdatablePropertiesEquals(expectedDescription, getPersistedDescription(expectedDescription));
    }
}
