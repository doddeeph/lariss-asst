package id.lariss.web.rest;

import static id.lariss.domain.StrapColorAsserts.*;
import static id.lariss.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.lariss.IntegrationTest;
import id.lariss.domain.StrapColor;
import id.lariss.repository.StrapColorRepository;
import id.lariss.service.dto.StrapColorDTO;
import id.lariss.service.mapper.StrapColorMapper;
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
 * Integration tests for the {@link StrapColorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StrapColorResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/strap-colors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private StrapColorRepository strapColorRepository;

    @Autowired
    private StrapColorMapper strapColorMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStrapColorMockMvc;

    private StrapColor strapColor;

    private StrapColor insertedStrapColor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StrapColor createEntity() {
        return new StrapColor().name(DEFAULT_NAME).value(DEFAULT_VALUE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StrapColor createUpdatedEntity() {
        return new StrapColor().name(UPDATED_NAME).value(UPDATED_VALUE);
    }

    @BeforeEach
    public void initTest() {
        strapColor = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedStrapColor != null) {
            strapColorRepository.delete(insertedStrapColor);
            insertedStrapColor = null;
        }
    }

    @Test
    @Transactional
    void createStrapColor() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the StrapColor
        StrapColorDTO strapColorDTO = strapColorMapper.toDto(strapColor);
        var returnedStrapColorDTO = om.readValue(
            restStrapColorMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(strapColorDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            StrapColorDTO.class
        );

        // Validate the StrapColor in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedStrapColor = strapColorMapper.toEntity(returnedStrapColorDTO);
        assertStrapColorUpdatableFieldsEquals(returnedStrapColor, getPersistedStrapColor(returnedStrapColor));

        insertedStrapColor = returnedStrapColor;
    }

    @Test
    @Transactional
    void createStrapColorWithExistingId() throws Exception {
        // Create the StrapColor with an existing ID
        strapColor.setId(1L);
        StrapColorDTO strapColorDTO = strapColorMapper.toDto(strapColor);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStrapColorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(strapColorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the StrapColor in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        strapColor.setName(null);

        // Create the StrapColor, which fails.
        StrapColorDTO strapColorDTO = strapColorMapper.toDto(strapColor);

        restStrapColorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(strapColorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllStrapColors() throws Exception {
        // Initialize the database
        insertedStrapColor = strapColorRepository.saveAndFlush(strapColor);

        // Get all the strapColorList
        restStrapColorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(strapColor.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    @Test
    @Transactional
    void getStrapColor() throws Exception {
        // Initialize the database
        insertedStrapColor = strapColorRepository.saveAndFlush(strapColor);

        // Get the strapColor
        restStrapColorMockMvc
            .perform(get(ENTITY_API_URL_ID, strapColor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(strapColor.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }

    @Test
    @Transactional
    void getNonExistingStrapColor() throws Exception {
        // Get the strapColor
        restStrapColorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStrapColor() throws Exception {
        // Initialize the database
        insertedStrapColor = strapColorRepository.saveAndFlush(strapColor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the strapColor
        StrapColor updatedStrapColor = strapColorRepository.findById(strapColor.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedStrapColor are not directly saved in db
        em.detach(updatedStrapColor);
        updatedStrapColor.name(UPDATED_NAME).value(UPDATED_VALUE);
        StrapColorDTO strapColorDTO = strapColorMapper.toDto(updatedStrapColor);

        restStrapColorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, strapColorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(strapColorDTO))
            )
            .andExpect(status().isOk());

        // Validate the StrapColor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedStrapColorToMatchAllProperties(updatedStrapColor);
    }

    @Test
    @Transactional
    void putNonExistingStrapColor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        strapColor.setId(longCount.incrementAndGet());

        // Create the StrapColor
        StrapColorDTO strapColorDTO = strapColorMapper.toDto(strapColor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStrapColorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, strapColorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(strapColorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StrapColor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStrapColor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        strapColor.setId(longCount.incrementAndGet());

        // Create the StrapColor
        StrapColorDTO strapColorDTO = strapColorMapper.toDto(strapColor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStrapColorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(strapColorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StrapColor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStrapColor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        strapColor.setId(longCount.incrementAndGet());

        // Create the StrapColor
        StrapColorDTO strapColorDTO = strapColorMapper.toDto(strapColor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStrapColorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(strapColorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StrapColor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStrapColorWithPatch() throws Exception {
        // Initialize the database
        insertedStrapColor = strapColorRepository.saveAndFlush(strapColor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the strapColor using partial update
        StrapColor partialUpdatedStrapColor = new StrapColor();
        partialUpdatedStrapColor.setId(strapColor.getId());

        partialUpdatedStrapColor.value(UPDATED_VALUE);

        restStrapColorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStrapColor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStrapColor))
            )
            .andExpect(status().isOk());

        // Validate the StrapColor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStrapColorUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedStrapColor, strapColor),
            getPersistedStrapColor(strapColor)
        );
    }

    @Test
    @Transactional
    void fullUpdateStrapColorWithPatch() throws Exception {
        // Initialize the database
        insertedStrapColor = strapColorRepository.saveAndFlush(strapColor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the strapColor using partial update
        StrapColor partialUpdatedStrapColor = new StrapColor();
        partialUpdatedStrapColor.setId(strapColor.getId());

        partialUpdatedStrapColor.name(UPDATED_NAME).value(UPDATED_VALUE);

        restStrapColorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStrapColor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStrapColor))
            )
            .andExpect(status().isOk());

        // Validate the StrapColor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStrapColorUpdatableFieldsEquals(partialUpdatedStrapColor, getPersistedStrapColor(partialUpdatedStrapColor));
    }

    @Test
    @Transactional
    void patchNonExistingStrapColor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        strapColor.setId(longCount.incrementAndGet());

        // Create the StrapColor
        StrapColorDTO strapColorDTO = strapColorMapper.toDto(strapColor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStrapColorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, strapColorDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(strapColorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StrapColor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStrapColor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        strapColor.setId(longCount.incrementAndGet());

        // Create the StrapColor
        StrapColorDTO strapColorDTO = strapColorMapper.toDto(strapColor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStrapColorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(strapColorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StrapColor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStrapColor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        strapColor.setId(longCount.incrementAndGet());

        // Create the StrapColor
        StrapColorDTO strapColorDTO = strapColorMapper.toDto(strapColor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStrapColorMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(strapColorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StrapColor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStrapColor() throws Exception {
        // Initialize the database
        insertedStrapColor = strapColorRepository.saveAndFlush(strapColor);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the strapColor
        restStrapColorMockMvc
            .perform(delete(ENTITY_API_URL_ID, strapColor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return strapColorRepository.count();
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

    protected StrapColor getPersistedStrapColor(StrapColor strapColor) {
        return strapColorRepository.findById(strapColor.getId()).orElseThrow();
    }

    protected void assertPersistedStrapColorToMatchAllProperties(StrapColor expectedStrapColor) {
        assertStrapColorAllPropertiesEquals(expectedStrapColor, getPersistedStrapColor(expectedStrapColor));
    }

    protected void assertPersistedStrapColorToMatchUpdatableProperties(StrapColor expectedStrapColor) {
        assertStrapColorAllUpdatablePropertiesEquals(expectedStrapColor, getPersistedStrapColor(expectedStrapColor));
    }
}
