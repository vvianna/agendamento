package br.com.jvmsistemas.web.rest;

import br.com.jvmsistemas.AgendamentoApp;

import br.com.jvmsistemas.domain.PlanoDeSaude;
import br.com.jvmsistemas.repository.PlanoDeSaudeRepository;
import br.com.jvmsistemas.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;


import static br.com.jvmsistemas.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PlanoDeSaudeResource REST controller.
 *
 * @see PlanoDeSaudeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgendamentoApp.class)
public class PlanoDeSaudeResourceIntTest {

    private static final String DEFAULT_NOME_PLANO = "AAAAAAAAAA";
    private static final String UPDATED_NOME_PLANO = "BBBBBBBBBB";

    private static final Long DEFAULT_CNPJ = 1L;
    private static final Long UPDATED_CNPJ = 2L;

    @Autowired
    private PlanoDeSaudeRepository planoDeSaudeRepository;
    @Mock
    private PlanoDeSaudeRepository planoDeSaudeRepositoryMock;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPlanoDeSaudeMockMvc;

    private PlanoDeSaude planoDeSaude;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PlanoDeSaudeResource planoDeSaudeResource = new PlanoDeSaudeResource(planoDeSaudeRepository);
        this.restPlanoDeSaudeMockMvc = MockMvcBuilders.standaloneSetup(planoDeSaudeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlanoDeSaude createEntity(EntityManager em) {
        PlanoDeSaude planoDeSaude = new PlanoDeSaude()
            .nomePlano(DEFAULT_NOME_PLANO)
            .cnpj(DEFAULT_CNPJ);
        return planoDeSaude;
    }

    @Before
    public void initTest() {
        planoDeSaude = createEntity(em);
    }

    @Test
    @Transactional
    public void createPlanoDeSaude() throws Exception {
        int databaseSizeBeforeCreate = planoDeSaudeRepository.findAll().size();

        // Create the PlanoDeSaude
        restPlanoDeSaudeMockMvc.perform(post("/api/plano-de-saudes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planoDeSaude)))
            .andExpect(status().isCreated());

        // Validate the PlanoDeSaude in the database
        List<PlanoDeSaude> planoDeSaudeList = planoDeSaudeRepository.findAll();
        assertThat(planoDeSaudeList).hasSize(databaseSizeBeforeCreate + 1);
        PlanoDeSaude testPlanoDeSaude = planoDeSaudeList.get(planoDeSaudeList.size() - 1);
        assertThat(testPlanoDeSaude.getNomePlano()).isEqualTo(DEFAULT_NOME_PLANO);
        assertThat(testPlanoDeSaude.getCnpj()).isEqualTo(DEFAULT_CNPJ);
    }

    @Test
    @Transactional
    public void createPlanoDeSaudeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = planoDeSaudeRepository.findAll().size();

        // Create the PlanoDeSaude with an existing ID
        planoDeSaude.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlanoDeSaudeMockMvc.perform(post("/api/plano-de-saudes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planoDeSaude)))
            .andExpect(status().isBadRequest());

        // Validate the PlanoDeSaude in the database
        List<PlanoDeSaude> planoDeSaudeList = planoDeSaudeRepository.findAll();
        assertThat(planoDeSaudeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPlanoDeSaudes() throws Exception {
        // Initialize the database
        planoDeSaudeRepository.saveAndFlush(planoDeSaude);

        // Get all the planoDeSaudeList
        restPlanoDeSaudeMockMvc.perform(get("/api/plano-de-saudes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(planoDeSaude.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomePlano").value(hasItem(DEFAULT_NOME_PLANO.toString())))
            .andExpect(jsonPath("$.[*].cnpj").value(hasItem(DEFAULT_CNPJ.intValue())));
    }
    
    public void getAllPlanoDeSaudesWithEagerRelationshipsIsEnabled() throws Exception {
        PlanoDeSaudeResource planoDeSaudeResource = new PlanoDeSaudeResource(planoDeSaudeRepositoryMock);
        when(planoDeSaudeRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restPlanoDeSaudeMockMvc = MockMvcBuilders.standaloneSetup(planoDeSaudeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restPlanoDeSaudeMockMvc.perform(get("/api/plano-de-saudes?eagerload=true"))
        .andExpect(status().isOk());

        verify(planoDeSaudeRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    public void getAllPlanoDeSaudesWithEagerRelationshipsIsNotEnabled() throws Exception {
        PlanoDeSaudeResource planoDeSaudeResource = new PlanoDeSaudeResource(planoDeSaudeRepositoryMock);
            when(planoDeSaudeRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restPlanoDeSaudeMockMvc = MockMvcBuilders.standaloneSetup(planoDeSaudeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restPlanoDeSaudeMockMvc.perform(get("/api/plano-de-saudes?eagerload=true"))
        .andExpect(status().isOk());

            verify(planoDeSaudeRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getPlanoDeSaude() throws Exception {
        // Initialize the database
        planoDeSaudeRepository.saveAndFlush(planoDeSaude);

        // Get the planoDeSaude
        restPlanoDeSaudeMockMvc.perform(get("/api/plano-de-saudes/{id}", planoDeSaude.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(planoDeSaude.getId().intValue()))
            .andExpect(jsonPath("$.nomePlano").value(DEFAULT_NOME_PLANO.toString()))
            .andExpect(jsonPath("$.cnpj").value(DEFAULT_CNPJ.intValue()));
    }
    @Test
    @Transactional
    public void getNonExistingPlanoDeSaude() throws Exception {
        // Get the planoDeSaude
        restPlanoDeSaudeMockMvc.perform(get("/api/plano-de-saudes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlanoDeSaude() throws Exception {
        // Initialize the database
        planoDeSaudeRepository.saveAndFlush(planoDeSaude);

        int databaseSizeBeforeUpdate = planoDeSaudeRepository.findAll().size();

        // Update the planoDeSaude
        PlanoDeSaude updatedPlanoDeSaude = planoDeSaudeRepository.findById(planoDeSaude.getId()).get();
        // Disconnect from session so that the updates on updatedPlanoDeSaude are not directly saved in db
        em.detach(updatedPlanoDeSaude);
        updatedPlanoDeSaude
            .nomePlano(UPDATED_NOME_PLANO)
            .cnpj(UPDATED_CNPJ);

        restPlanoDeSaudeMockMvc.perform(put("/api/plano-de-saudes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPlanoDeSaude)))
            .andExpect(status().isOk());

        // Validate the PlanoDeSaude in the database
        List<PlanoDeSaude> planoDeSaudeList = planoDeSaudeRepository.findAll();
        assertThat(planoDeSaudeList).hasSize(databaseSizeBeforeUpdate);
        PlanoDeSaude testPlanoDeSaude = planoDeSaudeList.get(planoDeSaudeList.size() - 1);
        assertThat(testPlanoDeSaude.getNomePlano()).isEqualTo(UPDATED_NOME_PLANO);
        assertThat(testPlanoDeSaude.getCnpj()).isEqualTo(UPDATED_CNPJ);
    }

    @Test
    @Transactional
    public void updateNonExistingPlanoDeSaude() throws Exception {
        int databaseSizeBeforeUpdate = planoDeSaudeRepository.findAll().size();

        // Create the PlanoDeSaude

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPlanoDeSaudeMockMvc.perform(put("/api/plano-de-saudes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planoDeSaude)))
            .andExpect(status().isBadRequest());

        // Validate the PlanoDeSaude in the database
        List<PlanoDeSaude> planoDeSaudeList = planoDeSaudeRepository.findAll();
        assertThat(planoDeSaudeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePlanoDeSaude() throws Exception {
        // Initialize the database
        planoDeSaudeRepository.saveAndFlush(planoDeSaude);

        int databaseSizeBeforeDelete = planoDeSaudeRepository.findAll().size();

        // Get the planoDeSaude
        restPlanoDeSaudeMockMvc.perform(delete("/api/plano-de-saudes/{id}", planoDeSaude.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PlanoDeSaude> planoDeSaudeList = planoDeSaudeRepository.findAll();
        assertThat(planoDeSaudeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlanoDeSaude.class);
        PlanoDeSaude planoDeSaude1 = new PlanoDeSaude();
        planoDeSaude1.setId(1L);
        PlanoDeSaude planoDeSaude2 = new PlanoDeSaude();
        planoDeSaude2.setId(planoDeSaude1.getId());
        assertThat(planoDeSaude1).isEqualTo(planoDeSaude2);
        planoDeSaude2.setId(2L);
        assertThat(planoDeSaude1).isNotEqualTo(planoDeSaude2);
        planoDeSaude1.setId(null);
        assertThat(planoDeSaude1).isNotEqualTo(planoDeSaude2);
    }
}
