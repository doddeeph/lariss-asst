package id.lariss.web.rest;

import static id.lariss.domain.ProcessorAsserts.*;
import static id.lariss.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.lariss.IntegrationTest;
import id.lariss.domain.Processor;
import id.lariss.repository.ProcessorRepository;
import id.lariss.service.dto.ProcessorDTO;
import id.lariss.service.mapper.ProcessorMapper;
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
 * Integration tests for the {@link ProcessorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProcessorResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/processors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProcessorRepository processorRepository;

    @Autowired
    private ProcessorMapper processorMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProcessorMockMvc;

    private Processor processor;

    private Processor insertedProcessor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Processor createEntity() {
        return new Processor().name(DEFAULT_NAME).value(DEFAULT_VALUE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Processor createUpdatedEntity() {
        return new Processor().name(UPDATED_NAME).value(UPDATED_VALUE);
    }

    @BeforeEach
    public void initTest() {
        processor = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedProcessor != null) {
            processorRepository.delete(insertedProcessor);
            insertedProcessor = null;
        }
    }

    @Test
    @Transactional
    void createProcessor() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Processor
        ProcessorDTO processorDTO = processorMapper.toDto(processor);
        var returnedProcessorDTO = om.readValue(
            restProcessorMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(processorDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProcessorDTO.class
        );

        // Validate the Processor in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProcessor = processorMapper.toEntity(returnedProcessorDTO);
        assertProcessorUpdatableFieldsEquals(returnedProcessor, getPersistedProcessor(returnedProcessor));

        insertedProcessor = returnedProcessor;
    }

    @Test
    @Transactional
    void createProcessorWithExistingId() throws Exception {
        // Create the Processor with an existing ID
        processor.setId(1L);
        ProcessorDTO processorDTO = processorMapper.toDto(processor);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProcessorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(processorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Processor in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        processor.setName(null);

        // Create the Processor, which fails.
        ProcessorDTO processorDTO = processorMapper.toDto(processor);

        restProcessorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(processorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProcessors() throws Exception {
        // Initialize the database
        insertedProcessor = processorRepository.saveAndFlush(processor);

        // Get all the processorList
        restProcessorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(processor.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    @Test
    @Transactional
    void getProcessor() throws Exception {
        // Initialize the database
        insertedProcessor = processorRepository.saveAndFlush(processor);

        // Get the processor
        restProcessorMockMvc
            .perform(get(ENTITY_API_URL_ID, processor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(processor.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }

    @Test
    @Transactional
    void getNonExistingProcessor() throws Exception {
        // Get the processor
        restProcessorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProcessor() throws Exception {
        // Initialize the database
        insertedProcessor = processorRepository.saveAndFlush(processor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the processor
        Processor updatedProcessor = processorRepository.findById(processor.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProcessor are not directly saved in db
        em.detach(updatedProcessor);
        updatedProcessor.name(UPDATED_NAME).value(UPDATED_VALUE);
        ProcessorDTO processorDTO = processorMapper.toDto(updatedProcessor);

        restProcessorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, processorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(processorDTO))
            )
            .andExpect(status().isOk());

        // Validate the Processor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProcessorToMatchAllProperties(updatedProcessor);
    }

    @Test
    @Transactional
    void putNonExistingProcessor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        processor.setId(longCount.incrementAndGet());

        // Create the Processor
        ProcessorDTO processorDTO = processorMapper.toDto(processor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProcessorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, processorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(processorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Processor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProcessor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        processor.setId(longCount.incrementAndGet());

        // Create the Processor
        ProcessorDTO processorDTO = processorMapper.toDto(processor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(processorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Processor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProcessor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        processor.setId(longCount.incrementAndGet());

        // Create the Processor
        ProcessorDTO processorDTO = processorMapper.toDto(processor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(processorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Processor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProcessorWithPatch() throws Exception {
        // Initialize the database
        insertedProcessor = processorRepository.saveAndFlush(processor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the processor using partial update
        Processor partialUpdatedProcessor = new Processor();
        partialUpdatedProcessor.setId(processor.getId());

        partialUpdatedProcessor.value(UPDATED_VALUE);

        restProcessorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProcessor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProcessor))
            )
            .andExpect(status().isOk());

        // Validate the Processor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProcessorUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProcessor, processor),
            getPersistedProcessor(processor)
        );
    }

    @Test
    @Transactional
    void fullUpdateProcessorWithPatch() throws Exception {
        // Initialize the database
        insertedProcessor = processorRepository.saveAndFlush(processor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the processor using partial update
        Processor partialUpdatedProcessor = new Processor();
        partialUpdatedProcessor.setId(processor.getId());

        partialUpdatedProcessor.name(UPDATED_NAME).value(UPDATED_VALUE);

        restProcessorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProcessor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProcessor))
            )
            .andExpect(status().isOk());

        // Validate the Processor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProcessorUpdatableFieldsEquals(partialUpdatedProcessor, getPersistedProcessor(partialUpdatedProcessor));
    }

    @Test
    @Transactional
    void patchNonExistingProcessor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        processor.setId(longCount.incrementAndGet());

        // Create the Processor
        ProcessorDTO processorDTO = processorMapper.toDto(processor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProcessorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, processorDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(processorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Processor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProcessor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        processor.setId(longCount.incrementAndGet());

        // Create the Processor
        ProcessorDTO processorDTO = processorMapper.toDto(processor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(processorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Processor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProcessor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        processor.setId(longCount.incrementAndGet());

        // Create the Processor
        ProcessorDTO processorDTO = processorMapper.toDto(processor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessorMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(processorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Processor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProcessor() throws Exception {
        // Initialize the database
        insertedProcessor = processorRepository.saveAndFlush(processor);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the processor
        restProcessorMockMvc
            .perform(delete(ENTITY_API_URL_ID, processor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return processorRepository.count();
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

    protected Processor getPersistedProcessor(Processor processor) {
        return processorRepository.findById(processor.getId()).orElseThrow();
    }

    protected void assertPersistedProcessorToMatchAllProperties(Processor expectedProcessor) {
        assertProcessorAllPropertiesEquals(expectedProcessor, getPersistedProcessor(expectedProcessor));
    }

    protected void assertPersistedProcessorToMatchUpdatableProperties(Processor expectedProcessor) {
        assertProcessorAllUpdatablePropertiesEquals(expectedProcessor, getPersistedProcessor(expectedProcessor));
    }
}
