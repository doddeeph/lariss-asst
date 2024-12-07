package id.lariss.web.rest;

import static id.lariss.domain.ColorAsserts.*;
import static id.lariss.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.lariss.IntegrationTest;
import id.lariss.domain.Color;
import id.lariss.repository.ColorRepository;
import id.lariss.service.dto.ColorDTO;
import id.lariss.service.mapper.ColorMapper;
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
 * Integration tests for the {@link ColorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ColorResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/colors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ColorRepository colorRepository;

    @Autowired
    private ColorMapper colorMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restColorMockMvc;

    private Color color;

    private Color insertedColor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Color createEntity() {
        return new Color().name(DEFAULT_NAME).value(DEFAULT_VALUE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Color createUpdatedEntity() {
        return new Color().name(UPDATED_NAME).value(UPDATED_VALUE);
    }

    @BeforeEach
    public void initTest() {
        color = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedColor != null) {
            colorRepository.delete(insertedColor);
            insertedColor = null;
        }
    }

    @Test
    @Transactional
    void createColor() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Color
        ColorDTO colorDTO = colorMapper.toDto(color);
        var returnedColorDTO = om.readValue(
            restColorMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(colorDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ColorDTO.class
        );

        // Validate the Color in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedColor = colorMapper.toEntity(returnedColorDTO);
        assertColorUpdatableFieldsEquals(returnedColor, getPersistedColor(returnedColor));

        insertedColor = returnedColor;
    }

    @Test
    @Transactional
    void createColorWithExistingId() throws Exception {
        // Create the Color with an existing ID
        color.setId(1L);
        ColorDTO colorDTO = colorMapper.toDto(color);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restColorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(colorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Color in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        color.setName(null);

        // Create the Color, which fails.
        ColorDTO colorDTO = colorMapper.toDto(color);

        restColorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(colorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllColors() throws Exception {
        // Initialize the database
        insertedColor = colorRepository.saveAndFlush(color);

        // Get all the colorList
        restColorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(color.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    @Test
    @Transactional
    void getColor() throws Exception {
        // Initialize the database
        insertedColor = colorRepository.saveAndFlush(color);

        // Get the color
        restColorMockMvc
            .perform(get(ENTITY_API_URL_ID, color.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(color.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }

    @Test
    @Transactional
    void getNonExistingColor() throws Exception {
        // Get the color
        restColorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingColor() throws Exception {
        // Initialize the database
        insertedColor = colorRepository.saveAndFlush(color);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the color
        Color updatedColor = colorRepository.findById(color.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedColor are not directly saved in db
        em.detach(updatedColor);
        updatedColor.name(UPDATED_NAME).value(UPDATED_VALUE);
        ColorDTO colorDTO = colorMapper.toDto(updatedColor);

        restColorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, colorDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(colorDTO))
            )
            .andExpect(status().isOk());

        // Validate the Color in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedColorToMatchAllProperties(updatedColor);
    }

    @Test
    @Transactional
    void putNonExistingColor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        color.setId(longCount.incrementAndGet());

        // Create the Color
        ColorDTO colorDTO = colorMapper.toDto(color);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restColorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, colorDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(colorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Color in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchColor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        color.setId(longCount.incrementAndGet());

        // Create the Color
        ColorDTO colorDTO = colorMapper.toDto(color);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restColorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(colorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Color in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamColor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        color.setId(longCount.incrementAndGet());

        // Create the Color
        ColorDTO colorDTO = colorMapper.toDto(color);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restColorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(colorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Color in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateColorWithPatch() throws Exception {
        // Initialize the database
        insertedColor = colorRepository.saveAndFlush(color);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the color using partial update
        Color partialUpdatedColor = new Color();
        partialUpdatedColor.setId(color.getId());

        partialUpdatedColor.value(UPDATED_VALUE);

        restColorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedColor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedColor))
            )
            .andExpect(status().isOk());

        // Validate the Color in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertColorUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedColor, color), getPersistedColor(color));
    }

    @Test
    @Transactional
    void fullUpdateColorWithPatch() throws Exception {
        // Initialize the database
        insertedColor = colorRepository.saveAndFlush(color);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the color using partial update
        Color partialUpdatedColor = new Color();
        partialUpdatedColor.setId(color.getId());

        partialUpdatedColor.name(UPDATED_NAME).value(UPDATED_VALUE);

        restColorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedColor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedColor))
            )
            .andExpect(status().isOk());

        // Validate the Color in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertColorUpdatableFieldsEquals(partialUpdatedColor, getPersistedColor(partialUpdatedColor));
    }

    @Test
    @Transactional
    void patchNonExistingColor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        color.setId(longCount.incrementAndGet());

        // Create the Color
        ColorDTO colorDTO = colorMapper.toDto(color);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restColorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, colorDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(colorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Color in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchColor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        color.setId(longCount.incrementAndGet());

        // Create the Color
        ColorDTO colorDTO = colorMapper.toDto(color);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restColorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(colorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Color in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamColor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        color.setId(longCount.incrementAndGet());

        // Create the Color
        ColorDTO colorDTO = colorMapper.toDto(color);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restColorMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(colorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Color in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteColor() throws Exception {
        // Initialize the database
        insertedColor = colorRepository.saveAndFlush(color);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the color
        restColorMockMvc
            .perform(delete(ENTITY_API_URL_ID, color.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return colorRepository.count();
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

    protected Color getPersistedColor(Color color) {
        return colorRepository.findById(color.getId()).orElseThrow();
    }

    protected void assertPersistedColorToMatchAllProperties(Color expectedColor) {
        assertColorAllPropertiesEquals(expectedColor, getPersistedColor(expectedColor));
    }

    protected void assertPersistedColorToMatchUpdatableProperties(Color expectedColor) {
        assertColorAllUpdatablePropertiesEquals(expectedColor, getPersistedColor(expectedColor));
    }
}
