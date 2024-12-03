package id.lariss.web.rest;

import static id.lariss.domain.StorageAsserts.*;
import static id.lariss.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.lariss.IntegrationTest;
import id.lariss.domain.Storage;
import id.lariss.repository.StorageRepository;
import id.lariss.service.dto.StorageDTO;
import id.lariss.service.mapper.StorageMapper;
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
 * Integration tests for the {@link StorageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StorageResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/storages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private StorageRepository storageRepository;

    @Autowired
    private StorageMapper storageMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStorageMockMvc;

    private Storage storage;

    private Storage insertedStorage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Storage createEntity() {
        return new Storage().name(DEFAULT_NAME).value(DEFAULT_VALUE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Storage createUpdatedEntity() {
        return new Storage().name(UPDATED_NAME).value(UPDATED_VALUE);
    }

    @BeforeEach
    public void initTest() {
        storage = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedStorage != null) {
            storageRepository.delete(insertedStorage);
            insertedStorage = null;
        }
    }

    @Test
    @Transactional
    void createStorage() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Storage
        StorageDTO storageDTO = storageMapper.toDto(storage);
        var returnedStorageDTO = om.readValue(
            restStorageMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(storageDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            StorageDTO.class
        );

        // Validate the Storage in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedStorage = storageMapper.toEntity(returnedStorageDTO);
        assertStorageUpdatableFieldsEquals(returnedStorage, getPersistedStorage(returnedStorage));

        insertedStorage = returnedStorage;
    }

    @Test
    @Transactional
    void createStorageWithExistingId() throws Exception {
        // Create the Storage with an existing ID
        storage.setId(1L);
        StorageDTO storageDTO = storageMapper.toDto(storage);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStorageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(storageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Storage in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        storage.setName(null);

        // Create the Storage, which fails.
        StorageDTO storageDTO = storageMapper.toDto(storage);

        restStorageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(storageDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllStorages() throws Exception {
        // Initialize the database
        insertedStorage = storageRepository.saveAndFlush(storage);

        // Get all the storageList
        restStorageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(storage.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    @Test
    @Transactional
    void getStorage() throws Exception {
        // Initialize the database
        insertedStorage = storageRepository.saveAndFlush(storage);

        // Get the storage
        restStorageMockMvc
            .perform(get(ENTITY_API_URL_ID, storage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(storage.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }

    @Test
    @Transactional
    void getNonExistingStorage() throws Exception {
        // Get the storage
        restStorageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStorage() throws Exception {
        // Initialize the database
        insertedStorage = storageRepository.saveAndFlush(storage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the storage
        Storage updatedStorage = storageRepository.findById(storage.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedStorage are not directly saved in db
        em.detach(updatedStorage);
        updatedStorage.name(UPDATED_NAME).value(UPDATED_VALUE);
        StorageDTO storageDTO = storageMapper.toDto(updatedStorage);

        restStorageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, storageDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(storageDTO))
            )
            .andExpect(status().isOk());

        // Validate the Storage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedStorageToMatchAllProperties(updatedStorage);
    }

    @Test
    @Transactional
    void putNonExistingStorage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        storage.setId(longCount.incrementAndGet());

        // Create the Storage
        StorageDTO storageDTO = storageMapper.toDto(storage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStorageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, storageDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(storageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Storage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStorage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        storage.setId(longCount.incrementAndGet());

        // Create the Storage
        StorageDTO storageDTO = storageMapper.toDto(storage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStorageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(storageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Storage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStorage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        storage.setId(longCount.incrementAndGet());

        // Create the Storage
        StorageDTO storageDTO = storageMapper.toDto(storage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStorageMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(storageDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Storage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStorageWithPatch() throws Exception {
        // Initialize the database
        insertedStorage = storageRepository.saveAndFlush(storage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the storage using partial update
        Storage partialUpdatedStorage = new Storage();
        partialUpdatedStorage.setId(storage.getId());

        restStorageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStorage.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStorage))
            )
            .andExpect(status().isOk());

        // Validate the Storage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStorageUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedStorage, storage), getPersistedStorage(storage));
    }

    @Test
    @Transactional
    void fullUpdateStorageWithPatch() throws Exception {
        // Initialize the database
        insertedStorage = storageRepository.saveAndFlush(storage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the storage using partial update
        Storage partialUpdatedStorage = new Storage();
        partialUpdatedStorage.setId(storage.getId());

        partialUpdatedStorage.name(UPDATED_NAME).value(UPDATED_VALUE);

        restStorageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStorage.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStorage))
            )
            .andExpect(status().isOk());

        // Validate the Storage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStorageUpdatableFieldsEquals(partialUpdatedStorage, getPersistedStorage(partialUpdatedStorage));
    }

    @Test
    @Transactional
    void patchNonExistingStorage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        storage.setId(longCount.incrementAndGet());

        // Create the Storage
        StorageDTO storageDTO = storageMapper.toDto(storage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStorageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, storageDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(storageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Storage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStorage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        storage.setId(longCount.incrementAndGet());

        // Create the Storage
        StorageDTO storageDTO = storageMapper.toDto(storage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStorageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(storageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Storage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStorage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        storage.setId(longCount.incrementAndGet());

        // Create the Storage
        StorageDTO storageDTO = storageMapper.toDto(storage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStorageMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(storageDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Storage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStorage() throws Exception {
        // Initialize the database
        insertedStorage = storageRepository.saveAndFlush(storage);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the storage
        restStorageMockMvc
            .perform(delete(ENTITY_API_URL_ID, storage.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return storageRepository.count();
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

    protected Storage getPersistedStorage(Storage storage) {
        return storageRepository.findById(storage.getId()).orElseThrow();
    }

    protected void assertPersistedStorageToMatchAllProperties(Storage expectedStorage) {
        assertStorageAllPropertiesEquals(expectedStorage, getPersistedStorage(expectedStorage));
    }

    protected void assertPersistedStorageToMatchUpdatableProperties(Storage expectedStorage) {
        assertStorageAllUpdatablePropertiesEquals(expectedStorage, getPersistedStorage(expectedStorage));
    }
}
