package id.lariss.web.rest;

import static id.lariss.domain.ScreenAsserts.*;
import static id.lariss.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.lariss.IntegrationTest;
import id.lariss.domain.Screen;
import id.lariss.repository.ScreenRepository;
import id.lariss.service.dto.ScreenDTO;
import id.lariss.service.mapper.ScreenMapper;
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
 * Integration tests for the {@link ScreenResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ScreenResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/screens";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ScreenRepository screenRepository;

    @Autowired
    private ScreenMapper screenMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restScreenMockMvc;

    private Screen screen;

    private Screen insertedScreen;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Screen createEntity() {
        return new Screen().name(DEFAULT_NAME).value(DEFAULT_VALUE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Screen createUpdatedEntity() {
        return new Screen().name(UPDATED_NAME).value(UPDATED_VALUE);
    }

    @BeforeEach
    public void initTest() {
        screen = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedScreen != null) {
            screenRepository.delete(insertedScreen);
            insertedScreen = null;
        }
    }

    @Test
    @Transactional
    void createScreen() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Screen
        ScreenDTO screenDTO = screenMapper.toDto(screen);
        var returnedScreenDTO = om.readValue(
            restScreenMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(screenDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ScreenDTO.class
        );

        // Validate the Screen in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedScreen = screenMapper.toEntity(returnedScreenDTO);
        assertScreenUpdatableFieldsEquals(returnedScreen, getPersistedScreen(returnedScreen));

        insertedScreen = returnedScreen;
    }

    @Test
    @Transactional
    void createScreenWithExistingId() throws Exception {
        // Create the Screen with an existing ID
        screen.setId(1L);
        ScreenDTO screenDTO = screenMapper.toDto(screen);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restScreenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(screenDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Screen in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        screen.setName(null);

        // Create the Screen, which fails.
        ScreenDTO screenDTO = screenMapper.toDto(screen);

        restScreenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(screenDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllScreens() throws Exception {
        // Initialize the database
        insertedScreen = screenRepository.saveAndFlush(screen);

        // Get all the screenList
        restScreenMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(screen.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    @Test
    @Transactional
    void getScreen() throws Exception {
        // Initialize the database
        insertedScreen = screenRepository.saveAndFlush(screen);

        // Get the screen
        restScreenMockMvc
            .perform(get(ENTITY_API_URL_ID, screen.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(screen.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }

    @Test
    @Transactional
    void getNonExistingScreen() throws Exception {
        // Get the screen
        restScreenMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingScreen() throws Exception {
        // Initialize the database
        insertedScreen = screenRepository.saveAndFlush(screen);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the screen
        Screen updatedScreen = screenRepository.findById(screen.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedScreen are not directly saved in db
        em.detach(updatedScreen);
        updatedScreen.name(UPDATED_NAME).value(UPDATED_VALUE);
        ScreenDTO screenDTO = screenMapper.toDto(updatedScreen);

        restScreenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, screenDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(screenDTO))
            )
            .andExpect(status().isOk());

        // Validate the Screen in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedScreenToMatchAllProperties(updatedScreen);
    }

    @Test
    @Transactional
    void putNonExistingScreen() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        screen.setId(longCount.incrementAndGet());

        // Create the Screen
        ScreenDTO screenDTO = screenMapper.toDto(screen);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScreenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, screenDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(screenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Screen in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchScreen() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        screen.setId(longCount.incrementAndGet());

        // Create the Screen
        ScreenDTO screenDTO = screenMapper.toDto(screen);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScreenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(screenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Screen in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamScreen() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        screen.setId(longCount.incrementAndGet());

        // Create the Screen
        ScreenDTO screenDTO = screenMapper.toDto(screen);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScreenMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(screenDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Screen in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateScreenWithPatch() throws Exception {
        // Initialize the database
        insertedScreen = screenRepository.saveAndFlush(screen);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the screen using partial update
        Screen partialUpdatedScreen = new Screen();
        partialUpdatedScreen.setId(screen.getId());

        partialUpdatedScreen.name(UPDATED_NAME).value(UPDATED_VALUE);

        restScreenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedScreen.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedScreen))
            )
            .andExpect(status().isOk());

        // Validate the Screen in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertScreenUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedScreen, screen), getPersistedScreen(screen));
    }

    @Test
    @Transactional
    void fullUpdateScreenWithPatch() throws Exception {
        // Initialize the database
        insertedScreen = screenRepository.saveAndFlush(screen);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the screen using partial update
        Screen partialUpdatedScreen = new Screen();
        partialUpdatedScreen.setId(screen.getId());

        partialUpdatedScreen.name(UPDATED_NAME).value(UPDATED_VALUE);

        restScreenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedScreen.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedScreen))
            )
            .andExpect(status().isOk());

        // Validate the Screen in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertScreenUpdatableFieldsEquals(partialUpdatedScreen, getPersistedScreen(partialUpdatedScreen));
    }

    @Test
    @Transactional
    void patchNonExistingScreen() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        screen.setId(longCount.incrementAndGet());

        // Create the Screen
        ScreenDTO screenDTO = screenMapper.toDto(screen);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScreenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, screenDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(screenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Screen in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchScreen() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        screen.setId(longCount.incrementAndGet());

        // Create the Screen
        ScreenDTO screenDTO = screenMapper.toDto(screen);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScreenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(screenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Screen in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamScreen() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        screen.setId(longCount.incrementAndGet());

        // Create the Screen
        ScreenDTO screenDTO = screenMapper.toDto(screen);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScreenMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(screenDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Screen in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteScreen() throws Exception {
        // Initialize the database
        insertedScreen = screenRepository.saveAndFlush(screen);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the screen
        restScreenMockMvc
            .perform(delete(ENTITY_API_URL_ID, screen.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return screenRepository.count();
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

    protected Screen getPersistedScreen(Screen screen) {
        return screenRepository.findById(screen.getId()).orElseThrow();
    }

    protected void assertPersistedScreenToMatchAllProperties(Screen expectedScreen) {
        assertScreenAllPropertiesEquals(expectedScreen, getPersistedScreen(expectedScreen));
    }

    protected void assertPersistedScreenToMatchUpdatableProperties(Screen expectedScreen) {
        assertScreenAllUpdatablePropertiesEquals(expectedScreen, getPersistedScreen(expectedScreen));
    }
}
