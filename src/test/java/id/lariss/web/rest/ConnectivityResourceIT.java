package id.lariss.web.rest;

import static id.lariss.domain.ConnectivityAsserts.*;
import static id.lariss.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.lariss.IntegrationTest;
import id.lariss.domain.Connectivity;
import id.lariss.repository.ConnectivityRepository;
import id.lariss.service.dto.ConnectivityDTO;
import id.lariss.service.mapper.ConnectivityMapper;
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
 * Integration tests for the {@link ConnectivityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ConnectivityResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/connectivities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ConnectivityRepository connectivityRepository;

    @Autowired
    private ConnectivityMapper connectivityMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConnectivityMockMvc;

    private Connectivity connectivity;

    private Connectivity insertedConnectivity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Connectivity createEntity() {
        return new Connectivity().name(DEFAULT_NAME).value(DEFAULT_VALUE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Connectivity createUpdatedEntity() {
        return new Connectivity().name(UPDATED_NAME).value(UPDATED_VALUE);
    }

    @BeforeEach
    public void initTest() {
        connectivity = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedConnectivity != null) {
            connectivityRepository.delete(insertedConnectivity);
            insertedConnectivity = null;
        }
    }

    @Test
    @Transactional
    void createConnectivity() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Connectivity
        ConnectivityDTO connectivityDTO = connectivityMapper.toDto(connectivity);
        var returnedConnectivityDTO = om.readValue(
            restConnectivityMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(connectivityDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ConnectivityDTO.class
        );

        // Validate the Connectivity in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedConnectivity = connectivityMapper.toEntity(returnedConnectivityDTO);
        assertConnectivityUpdatableFieldsEquals(returnedConnectivity, getPersistedConnectivity(returnedConnectivity));

        insertedConnectivity = returnedConnectivity;
    }

    @Test
    @Transactional
    void createConnectivityWithExistingId() throws Exception {
        // Create the Connectivity with an existing ID
        connectivity.setId(1L);
        ConnectivityDTO connectivityDTO = connectivityMapper.toDto(connectivity);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restConnectivityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(connectivityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Connectivity in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        connectivity.setName(null);

        // Create the Connectivity, which fails.
        ConnectivityDTO connectivityDTO = connectivityMapper.toDto(connectivity);

        restConnectivityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(connectivityDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllConnectivities() throws Exception {
        // Initialize the database
        insertedConnectivity = connectivityRepository.saveAndFlush(connectivity);

        // Get all the connectivityList
        restConnectivityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(connectivity.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    @Test
    @Transactional
    void getConnectivity() throws Exception {
        // Initialize the database
        insertedConnectivity = connectivityRepository.saveAndFlush(connectivity);

        // Get the connectivity
        restConnectivityMockMvc
            .perform(get(ENTITY_API_URL_ID, connectivity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(connectivity.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }

    @Test
    @Transactional
    void getNonExistingConnectivity() throws Exception {
        // Get the connectivity
        restConnectivityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingConnectivity() throws Exception {
        // Initialize the database
        insertedConnectivity = connectivityRepository.saveAndFlush(connectivity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the connectivity
        Connectivity updatedConnectivity = connectivityRepository.findById(connectivity.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedConnectivity are not directly saved in db
        em.detach(updatedConnectivity);
        updatedConnectivity.name(UPDATED_NAME).value(UPDATED_VALUE);
        ConnectivityDTO connectivityDTO = connectivityMapper.toDto(updatedConnectivity);

        restConnectivityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, connectivityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(connectivityDTO))
            )
            .andExpect(status().isOk());

        // Validate the Connectivity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedConnectivityToMatchAllProperties(updatedConnectivity);
    }

    @Test
    @Transactional
    void putNonExistingConnectivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        connectivity.setId(longCount.incrementAndGet());

        // Create the Connectivity
        ConnectivityDTO connectivityDTO = connectivityMapper.toDto(connectivity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConnectivityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, connectivityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(connectivityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Connectivity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchConnectivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        connectivity.setId(longCount.incrementAndGet());

        // Create the Connectivity
        ConnectivityDTO connectivityDTO = connectivityMapper.toDto(connectivity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConnectivityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(connectivityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Connectivity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamConnectivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        connectivity.setId(longCount.incrementAndGet());

        // Create the Connectivity
        ConnectivityDTO connectivityDTO = connectivityMapper.toDto(connectivity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConnectivityMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(connectivityDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Connectivity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateConnectivityWithPatch() throws Exception {
        // Initialize the database
        insertedConnectivity = connectivityRepository.saveAndFlush(connectivity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the connectivity using partial update
        Connectivity partialUpdatedConnectivity = new Connectivity();
        partialUpdatedConnectivity.setId(connectivity.getId());

        restConnectivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConnectivity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedConnectivity))
            )
            .andExpect(status().isOk());

        // Validate the Connectivity in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertConnectivityUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedConnectivity, connectivity),
            getPersistedConnectivity(connectivity)
        );
    }

    @Test
    @Transactional
    void fullUpdateConnectivityWithPatch() throws Exception {
        // Initialize the database
        insertedConnectivity = connectivityRepository.saveAndFlush(connectivity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the connectivity using partial update
        Connectivity partialUpdatedConnectivity = new Connectivity();
        partialUpdatedConnectivity.setId(connectivity.getId());

        partialUpdatedConnectivity.name(UPDATED_NAME).value(UPDATED_VALUE);

        restConnectivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConnectivity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedConnectivity))
            )
            .andExpect(status().isOk());

        // Validate the Connectivity in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertConnectivityUpdatableFieldsEquals(partialUpdatedConnectivity, getPersistedConnectivity(partialUpdatedConnectivity));
    }

    @Test
    @Transactional
    void patchNonExistingConnectivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        connectivity.setId(longCount.incrementAndGet());

        // Create the Connectivity
        ConnectivityDTO connectivityDTO = connectivityMapper.toDto(connectivity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConnectivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, connectivityDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(connectivityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Connectivity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchConnectivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        connectivity.setId(longCount.incrementAndGet());

        // Create the Connectivity
        ConnectivityDTO connectivityDTO = connectivityMapper.toDto(connectivity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConnectivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(connectivityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Connectivity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamConnectivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        connectivity.setId(longCount.incrementAndGet());

        // Create the Connectivity
        ConnectivityDTO connectivityDTO = connectivityMapper.toDto(connectivity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConnectivityMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(connectivityDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Connectivity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteConnectivity() throws Exception {
        // Initialize the database
        insertedConnectivity = connectivityRepository.saveAndFlush(connectivity);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the connectivity
        restConnectivityMockMvc
            .perform(delete(ENTITY_API_URL_ID, connectivity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return connectivityRepository.count();
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

    protected Connectivity getPersistedConnectivity(Connectivity connectivity) {
        return connectivityRepository.findById(connectivity.getId()).orElseThrow();
    }

    protected void assertPersistedConnectivityToMatchAllProperties(Connectivity expectedConnectivity) {
        assertConnectivityAllPropertiesEquals(expectedConnectivity, getPersistedConnectivity(expectedConnectivity));
    }

    protected void assertPersistedConnectivityToMatchUpdatableProperties(Connectivity expectedConnectivity) {
        assertConnectivityAllUpdatablePropertiesEquals(expectedConnectivity, getPersistedConnectivity(expectedConnectivity));
    }
}
