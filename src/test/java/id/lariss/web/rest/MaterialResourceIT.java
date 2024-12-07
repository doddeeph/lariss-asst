package id.lariss.web.rest;

import static id.lariss.domain.MaterialAsserts.*;
import static id.lariss.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.lariss.IntegrationTest;
import id.lariss.domain.Material;
import id.lariss.repository.MaterialRepository;
import id.lariss.service.dto.MaterialDTO;
import id.lariss.service.mapper.MaterialMapper;
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
 * Integration tests for the {@link MaterialResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MaterialResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/materials";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private MaterialMapper materialMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMaterialMockMvc;

    private Material material;

    private Material insertedMaterial;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Material createEntity() {
        return new Material().name(DEFAULT_NAME).value(DEFAULT_VALUE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Material createUpdatedEntity() {
        return new Material().name(UPDATED_NAME).value(UPDATED_VALUE);
    }

    @BeforeEach
    public void initTest() {
        material = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedMaterial != null) {
            materialRepository.delete(insertedMaterial);
            insertedMaterial = null;
        }
    }

    @Test
    @Transactional
    void createMaterial() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Material
        MaterialDTO materialDTO = materialMapper.toDto(material);
        var returnedMaterialDTO = om.readValue(
            restMaterialMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(materialDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MaterialDTO.class
        );

        // Validate the Material in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMaterial = materialMapper.toEntity(returnedMaterialDTO);
        assertMaterialUpdatableFieldsEquals(returnedMaterial, getPersistedMaterial(returnedMaterial));

        insertedMaterial = returnedMaterial;
    }

    @Test
    @Transactional
    void createMaterialWithExistingId() throws Exception {
        // Create the Material with an existing ID
        material.setId(1L);
        MaterialDTO materialDTO = materialMapper.toDto(material);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMaterialMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(materialDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Material in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        material.setName(null);

        // Create the Material, which fails.
        MaterialDTO materialDTO = materialMapper.toDto(material);

        restMaterialMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(materialDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMaterials() throws Exception {
        // Initialize the database
        insertedMaterial = materialRepository.saveAndFlush(material);

        // Get all the materialList
        restMaterialMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(material.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    @Test
    @Transactional
    void getMaterial() throws Exception {
        // Initialize the database
        insertedMaterial = materialRepository.saveAndFlush(material);

        // Get the material
        restMaterialMockMvc
            .perform(get(ENTITY_API_URL_ID, material.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(material.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }

    @Test
    @Transactional
    void getNonExistingMaterial() throws Exception {
        // Get the material
        restMaterialMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMaterial() throws Exception {
        // Initialize the database
        insertedMaterial = materialRepository.saveAndFlush(material);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the material
        Material updatedMaterial = materialRepository.findById(material.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMaterial are not directly saved in db
        em.detach(updatedMaterial);
        updatedMaterial.name(UPDATED_NAME).value(UPDATED_VALUE);
        MaterialDTO materialDTO = materialMapper.toDto(updatedMaterial);

        restMaterialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, materialDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(materialDTO))
            )
            .andExpect(status().isOk());

        // Validate the Material in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMaterialToMatchAllProperties(updatedMaterial);
    }

    @Test
    @Transactional
    void putNonExistingMaterial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        material.setId(longCount.incrementAndGet());

        // Create the Material
        MaterialDTO materialDTO = materialMapper.toDto(material);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaterialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, materialDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(materialDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Material in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMaterial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        material.setId(longCount.incrementAndGet());

        // Create the Material
        MaterialDTO materialDTO = materialMapper.toDto(material);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(materialDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Material in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMaterial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        material.setId(longCount.incrementAndGet());

        // Create the Material
        MaterialDTO materialDTO = materialMapper.toDto(material);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterialMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(materialDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Material in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMaterialWithPatch() throws Exception {
        // Initialize the database
        insertedMaterial = materialRepository.saveAndFlush(material);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the material using partial update
        Material partialUpdatedMaterial = new Material();
        partialUpdatedMaterial.setId(material.getId());

        partialUpdatedMaterial.name(UPDATED_NAME).value(UPDATED_VALUE);

        restMaterialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMaterial.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMaterial))
            )
            .andExpect(status().isOk());

        // Validate the Material in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMaterialUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedMaterial, material), getPersistedMaterial(material));
    }

    @Test
    @Transactional
    void fullUpdateMaterialWithPatch() throws Exception {
        // Initialize the database
        insertedMaterial = materialRepository.saveAndFlush(material);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the material using partial update
        Material partialUpdatedMaterial = new Material();
        partialUpdatedMaterial.setId(material.getId());

        partialUpdatedMaterial.name(UPDATED_NAME).value(UPDATED_VALUE);

        restMaterialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMaterial.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMaterial))
            )
            .andExpect(status().isOk());

        // Validate the Material in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMaterialUpdatableFieldsEquals(partialUpdatedMaterial, getPersistedMaterial(partialUpdatedMaterial));
    }

    @Test
    @Transactional
    void patchNonExistingMaterial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        material.setId(longCount.incrementAndGet());

        // Create the Material
        MaterialDTO materialDTO = materialMapper.toDto(material);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaterialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, materialDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(materialDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Material in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMaterial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        material.setId(longCount.incrementAndGet());

        // Create the Material
        MaterialDTO materialDTO = materialMapper.toDto(material);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(materialDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Material in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMaterial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        material.setId(longCount.incrementAndGet());

        // Create the Material
        MaterialDTO materialDTO = materialMapper.toDto(material);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterialMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(materialDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Material in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMaterial() throws Exception {
        // Initialize the database
        insertedMaterial = materialRepository.saveAndFlush(material);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the material
        restMaterialMockMvc
            .perform(delete(ENTITY_API_URL_ID, material.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return materialRepository.count();
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

    protected Material getPersistedMaterial(Material material) {
        return materialRepository.findById(material.getId()).orElseThrow();
    }

    protected void assertPersistedMaterialToMatchAllProperties(Material expectedMaterial) {
        assertMaterialAllPropertiesEquals(expectedMaterial, getPersistedMaterial(expectedMaterial));
    }

    protected void assertPersistedMaterialToMatchUpdatableProperties(Material expectedMaterial) {
        assertMaterialAllUpdatablePropertiesEquals(expectedMaterial, getPersistedMaterial(expectedMaterial));
    }
}
