package id.lariss.web.rest;

import static id.lariss.domain.MemoryAsserts.*;
import static id.lariss.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.lariss.IntegrationTest;
import id.lariss.domain.Memory;
import id.lariss.repository.MemoryRepository;
import id.lariss.service.dto.MemoryDTO;
import id.lariss.service.mapper.MemoryMapper;
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
 * Integration tests for the {@link MemoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MemoryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/memories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MemoryRepository memoryRepository;

    @Autowired
    private MemoryMapper memoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMemoryMockMvc;

    private Memory memory;

    private Memory insertedMemory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Memory createEntity() {
        return new Memory().name(DEFAULT_NAME).value(DEFAULT_VALUE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Memory createUpdatedEntity() {
        return new Memory().name(UPDATED_NAME).value(UPDATED_VALUE);
    }

    @BeforeEach
    public void initTest() {
        memory = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedMemory != null) {
            memoryRepository.delete(insertedMemory);
            insertedMemory = null;
        }
    }

    @Test
    @Transactional
    void createMemory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Memory
        MemoryDTO memoryDTO = memoryMapper.toDto(memory);
        var returnedMemoryDTO = om.readValue(
            restMemoryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(memoryDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MemoryDTO.class
        );

        // Validate the Memory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMemory = memoryMapper.toEntity(returnedMemoryDTO);
        assertMemoryUpdatableFieldsEquals(returnedMemory, getPersistedMemory(returnedMemory));

        insertedMemory = returnedMemory;
    }

    @Test
    @Transactional
    void createMemoryWithExistingId() throws Exception {
        // Create the Memory with an existing ID
        memory.setId(1L);
        MemoryDTO memoryDTO = memoryMapper.toDto(memory);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMemoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(memoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Memory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        memory.setName(null);

        // Create the Memory, which fails.
        MemoryDTO memoryDTO = memoryMapper.toDto(memory);

        restMemoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(memoryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMemories() throws Exception {
        // Initialize the database
        insertedMemory = memoryRepository.saveAndFlush(memory);

        // Get all the memoryList
        restMemoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(memory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    @Test
    @Transactional
    void getMemory() throws Exception {
        // Initialize the database
        insertedMemory = memoryRepository.saveAndFlush(memory);

        // Get the memory
        restMemoryMockMvc
            .perform(get(ENTITY_API_URL_ID, memory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(memory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }

    @Test
    @Transactional
    void getNonExistingMemory() throws Exception {
        // Get the memory
        restMemoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMemory() throws Exception {
        // Initialize the database
        insertedMemory = memoryRepository.saveAndFlush(memory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the memory
        Memory updatedMemory = memoryRepository.findById(memory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMemory are not directly saved in db
        em.detach(updatedMemory);
        updatedMemory.name(UPDATED_NAME).value(UPDATED_VALUE);
        MemoryDTO memoryDTO = memoryMapper.toDto(updatedMemory);

        restMemoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, memoryDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(memoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the Memory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMemoryToMatchAllProperties(updatedMemory);
    }

    @Test
    @Transactional
    void putNonExistingMemory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        memory.setId(longCount.incrementAndGet());

        // Create the Memory
        MemoryDTO memoryDTO = memoryMapper.toDto(memory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMemoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, memoryDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(memoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Memory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMemory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        memory.setId(longCount.incrementAndGet());

        // Create the Memory
        MemoryDTO memoryDTO = memoryMapper.toDto(memory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMemoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(memoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Memory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMemory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        memory.setId(longCount.incrementAndGet());

        // Create the Memory
        MemoryDTO memoryDTO = memoryMapper.toDto(memory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMemoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(memoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Memory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMemoryWithPatch() throws Exception {
        // Initialize the database
        insertedMemory = memoryRepository.saveAndFlush(memory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the memory using partial update
        Memory partialUpdatedMemory = new Memory();
        partialUpdatedMemory.setId(memory.getId());

        partialUpdatedMemory.name(UPDATED_NAME);

        restMemoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMemory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMemory))
            )
            .andExpect(status().isOk());

        // Validate the Memory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMemoryUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedMemory, memory), getPersistedMemory(memory));
    }

    @Test
    @Transactional
    void fullUpdateMemoryWithPatch() throws Exception {
        // Initialize the database
        insertedMemory = memoryRepository.saveAndFlush(memory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the memory using partial update
        Memory partialUpdatedMemory = new Memory();
        partialUpdatedMemory.setId(memory.getId());

        partialUpdatedMemory.name(UPDATED_NAME).value(UPDATED_VALUE);

        restMemoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMemory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMemory))
            )
            .andExpect(status().isOk());

        // Validate the Memory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMemoryUpdatableFieldsEquals(partialUpdatedMemory, getPersistedMemory(partialUpdatedMemory));
    }

    @Test
    @Transactional
    void patchNonExistingMemory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        memory.setId(longCount.incrementAndGet());

        // Create the Memory
        MemoryDTO memoryDTO = memoryMapper.toDto(memory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMemoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, memoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(memoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Memory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMemory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        memory.setId(longCount.incrementAndGet());

        // Create the Memory
        MemoryDTO memoryDTO = memoryMapper.toDto(memory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMemoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(memoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Memory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMemory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        memory.setId(longCount.incrementAndGet());

        // Create the Memory
        MemoryDTO memoryDTO = memoryMapper.toDto(memory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMemoryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(memoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Memory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMemory() throws Exception {
        // Initialize the database
        insertedMemory = memoryRepository.saveAndFlush(memory);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the memory
        restMemoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, memory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return memoryRepository.count();
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

    protected Memory getPersistedMemory(Memory memory) {
        return memoryRepository.findById(memory.getId()).orElseThrow();
    }

    protected void assertPersistedMemoryToMatchAllProperties(Memory expectedMemory) {
        assertMemoryAllPropertiesEquals(expectedMemory, getPersistedMemory(expectedMemory));
    }

    protected void assertPersistedMemoryToMatchUpdatableProperties(Memory expectedMemory) {
        assertMemoryAllUpdatablePropertiesEquals(expectedMemory, getPersistedMemory(expectedMemory));
    }
}
