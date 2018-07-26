package br.com.jvmsistemas.web.rest;

import br.com.jvmsistemas.AgendamentoApp;

import br.com.jvmsistemas.domain.Medico;
import br.com.jvmsistemas.repository.MedicoRepository;
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
 * Test class for the MedicoResource REST controller.
 *
 * @see MedicoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgendamentoApp.class)
public class MedicoResourceIntTest {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_CRM = "AAAAAAAAAA";
    private static final String UPDATED_CRM = "BBBBBBBBBB";

    @Autowired
    private MedicoRepository medicoRepository;
    @Mock
    private MedicoRepository medicoRepositoryMock;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMedicoMockMvc;

    private Medico medico;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MedicoResource medicoResource = new MedicoResource(medicoRepository);
        this.restMedicoMockMvc = MockMvcBuilders.standaloneSetup(medicoResource)
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
    public static Medico createEntity(EntityManager em) {
        Medico medico = new Medico()
            .nome(DEFAULT_NOME)
            .crm(DEFAULT_CRM);
        return medico;
    }

    @Before
    public void initTest() {
        medico = createEntity(em);
    }

    @Test
    @Transactional
    public void createMedico() throws Exception {
        int databaseSizeBeforeCreate = medicoRepository.findAll().size();

        // Create the Medico
        restMedicoMockMvc.perform(post("/api/medicos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(medico)))
            .andExpect(status().isCreated());

        // Validate the Medico in the database
        List<Medico> medicoList = medicoRepository.findAll();
        assertThat(medicoList).hasSize(databaseSizeBeforeCreate + 1);
        Medico testMedico = medicoList.get(medicoList.size() - 1);
        assertThat(testMedico.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testMedico.getCrm()).isEqualTo(DEFAULT_CRM);
    }

    @Test
    @Transactional
    public void createMedicoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = medicoRepository.findAll().size();

        // Create the Medico with an existing ID
        medico.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMedicoMockMvc.perform(post("/api/medicos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(medico)))
            .andExpect(status().isBadRequest());

        // Validate the Medico in the database
        List<Medico> medicoList = medicoRepository.findAll();
        assertThat(medicoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllMedicos() throws Exception {
        // Initialize the database
        medicoRepository.saveAndFlush(medico);

        // Get all the medicoList
        restMedicoMockMvc.perform(get("/api/medicos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medico.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].crm").value(hasItem(DEFAULT_CRM.toString())));
    }
    
    public void getAllMedicosWithEagerRelationshipsIsEnabled() throws Exception {
        MedicoResource medicoResource = new MedicoResource(medicoRepositoryMock);
        when(medicoRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restMedicoMockMvc = MockMvcBuilders.standaloneSetup(medicoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restMedicoMockMvc.perform(get("/api/medicos?eagerload=true"))
        .andExpect(status().isOk());

        verify(medicoRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    public void getAllMedicosWithEagerRelationshipsIsNotEnabled() throws Exception {
        MedicoResource medicoResource = new MedicoResource(medicoRepositoryMock);
            when(medicoRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restMedicoMockMvc = MockMvcBuilders.standaloneSetup(medicoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restMedicoMockMvc.perform(get("/api/medicos?eagerload=true"))
        .andExpect(status().isOk());

            verify(medicoRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getMedico() throws Exception {
        // Initialize the database
        medicoRepository.saveAndFlush(medico);

        // Get the medico
        restMedicoMockMvc.perform(get("/api/medicos/{id}", medico.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(medico.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
            .andExpect(jsonPath("$.crm").value(DEFAULT_CRM.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingMedico() throws Exception {
        // Get the medico
        restMedicoMockMvc.perform(get("/api/medicos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMedico() throws Exception {
        // Initialize the database
        medicoRepository.saveAndFlush(medico);

        int databaseSizeBeforeUpdate = medicoRepository.findAll().size();

        // Update the medico
        Medico updatedMedico = medicoRepository.findById(medico.getId()).get();
        // Disconnect from session so that the updates on updatedMedico are not directly saved in db
        em.detach(updatedMedico);
        updatedMedico
            .nome(UPDATED_NOME)
            .crm(UPDATED_CRM);

        restMedicoMockMvc.perform(put("/api/medicos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMedico)))
            .andExpect(status().isOk());

        // Validate the Medico in the database
        List<Medico> medicoList = medicoRepository.findAll();
        assertThat(medicoList).hasSize(databaseSizeBeforeUpdate);
        Medico testMedico = medicoList.get(medicoList.size() - 1);
        assertThat(testMedico.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testMedico.getCrm()).isEqualTo(UPDATED_CRM);
    }

    @Test
    @Transactional
    public void updateNonExistingMedico() throws Exception {
        int databaseSizeBeforeUpdate = medicoRepository.findAll().size();

        // Create the Medico

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMedicoMockMvc.perform(put("/api/medicos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(medico)))
            .andExpect(status().isBadRequest());

        // Validate the Medico in the database
        List<Medico> medicoList = medicoRepository.findAll();
        assertThat(medicoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMedico() throws Exception {
        // Initialize the database
        medicoRepository.saveAndFlush(medico);

        int databaseSizeBeforeDelete = medicoRepository.findAll().size();

        // Get the medico
        restMedicoMockMvc.perform(delete("/api/medicos/{id}", medico.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Medico> medicoList = medicoRepository.findAll();
        assertThat(medicoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Medico.class);
        Medico medico1 = new Medico();
        medico1.setId(1L);
        Medico medico2 = new Medico();
        medico2.setId(medico1.getId());
        assertThat(medico1).isEqualTo(medico2);
        medico2.setId(2L);
        assertThat(medico1).isNotEqualTo(medico2);
        medico1.setId(null);
        assertThat(medico1).isNotEqualTo(medico2);
    }
}
