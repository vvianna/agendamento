package br.com.jvmsistemas.web.rest;

import br.com.jvmsistemas.AgendamentoApp;

import br.com.jvmsistemas.domain.Especialidade;
import br.com.jvmsistemas.repository.EspecialidadeRepository;
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
 * Test class for the EspecialidadeResource REST controller.
 *
 * @see EspecialidadeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgendamentoApp.class)
public class EspecialidadeResourceIntTest {

    private static final String DEFAULT_NOME_ESPECIALIDADE = "AAAAAAAAAA";
    private static final String UPDATED_NOME_ESPECIALIDADE = "BBBBBBBBBB";

    @Autowired
    private EspecialidadeRepository especialidadeRepository;
    @Mock
    private EspecialidadeRepository especialidadeRepositoryMock;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEspecialidadeMockMvc;

    private Especialidade especialidade;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EspecialidadeResource especialidadeResource = new EspecialidadeResource(especialidadeRepository);
        this.restEspecialidadeMockMvc = MockMvcBuilders.standaloneSetup(especialidadeResource)
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
    public static Especialidade createEntity(EntityManager em) {
        Especialidade especialidade = new Especialidade()
            .nomeEspecialidade(DEFAULT_NOME_ESPECIALIDADE);
        return especialidade;
    }

    @Before
    public void initTest() {
        especialidade = createEntity(em);
    }

    @Test
    @Transactional
    public void createEspecialidade() throws Exception {
        int databaseSizeBeforeCreate = especialidadeRepository.findAll().size();

        // Create the Especialidade
        restEspecialidadeMockMvc.perform(post("/api/especialidades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(especialidade)))
            .andExpect(status().isCreated());

        // Validate the Especialidade in the database
        List<Especialidade> especialidadeList = especialidadeRepository.findAll();
        assertThat(especialidadeList).hasSize(databaseSizeBeforeCreate + 1);
        Especialidade testEspecialidade = especialidadeList.get(especialidadeList.size() - 1);
        assertThat(testEspecialidade.getNomeEspecialidade()).isEqualTo(DEFAULT_NOME_ESPECIALIDADE);
    }

    @Test
    @Transactional
    public void createEspecialidadeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = especialidadeRepository.findAll().size();

        // Create the Especialidade with an existing ID
        especialidade.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEspecialidadeMockMvc.perform(post("/api/especialidades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(especialidade)))
            .andExpect(status().isBadRequest());

        // Validate the Especialidade in the database
        List<Especialidade> especialidadeList = especialidadeRepository.findAll();
        assertThat(especialidadeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllEspecialidades() throws Exception {
        // Initialize the database
        especialidadeRepository.saveAndFlush(especialidade);

        // Get all the especialidadeList
        restEspecialidadeMockMvc.perform(get("/api/especialidades?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(especialidade.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomeEspecialidade").value(hasItem(DEFAULT_NOME_ESPECIALIDADE.toString())));
    }
    
    public void getAllEspecialidadesWithEagerRelationshipsIsEnabled() throws Exception {
        EspecialidadeResource especialidadeResource = new EspecialidadeResource(especialidadeRepositoryMock);
        when(especialidadeRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restEspecialidadeMockMvc = MockMvcBuilders.standaloneSetup(especialidadeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restEspecialidadeMockMvc.perform(get("/api/especialidades?eagerload=true"))
        .andExpect(status().isOk());

        verify(especialidadeRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    public void getAllEspecialidadesWithEagerRelationshipsIsNotEnabled() throws Exception {
        EspecialidadeResource especialidadeResource = new EspecialidadeResource(especialidadeRepositoryMock);
            when(especialidadeRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restEspecialidadeMockMvc = MockMvcBuilders.standaloneSetup(especialidadeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restEspecialidadeMockMvc.perform(get("/api/especialidades?eagerload=true"))
        .andExpect(status().isOk());

            verify(especialidadeRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getEspecialidade() throws Exception {
        // Initialize the database
        especialidadeRepository.saveAndFlush(especialidade);

        // Get the especialidade
        restEspecialidadeMockMvc.perform(get("/api/especialidades/{id}", especialidade.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(especialidade.getId().intValue()))
            .andExpect(jsonPath("$.nomeEspecialidade").value(DEFAULT_NOME_ESPECIALIDADE.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingEspecialidade() throws Exception {
        // Get the especialidade
        restEspecialidadeMockMvc.perform(get("/api/especialidades/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEspecialidade() throws Exception {
        // Initialize the database
        especialidadeRepository.saveAndFlush(especialidade);

        int databaseSizeBeforeUpdate = especialidadeRepository.findAll().size();

        // Update the especialidade
        Especialidade updatedEspecialidade = especialidadeRepository.findById(especialidade.getId()).get();
        // Disconnect from session so that the updates on updatedEspecialidade are not directly saved in db
        em.detach(updatedEspecialidade);
        updatedEspecialidade
            .nomeEspecialidade(UPDATED_NOME_ESPECIALIDADE);

        restEspecialidadeMockMvc.perform(put("/api/especialidades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEspecialidade)))
            .andExpect(status().isOk());

        // Validate the Especialidade in the database
        List<Especialidade> especialidadeList = especialidadeRepository.findAll();
        assertThat(especialidadeList).hasSize(databaseSizeBeforeUpdate);
        Especialidade testEspecialidade = especialidadeList.get(especialidadeList.size() - 1);
        assertThat(testEspecialidade.getNomeEspecialidade()).isEqualTo(UPDATED_NOME_ESPECIALIDADE);
    }

    @Test
    @Transactional
    public void updateNonExistingEspecialidade() throws Exception {
        int databaseSizeBeforeUpdate = especialidadeRepository.findAll().size();

        // Create the Especialidade

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEspecialidadeMockMvc.perform(put("/api/especialidades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(especialidade)))
            .andExpect(status().isBadRequest());

        // Validate the Especialidade in the database
        List<Especialidade> especialidadeList = especialidadeRepository.findAll();
        assertThat(especialidadeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEspecialidade() throws Exception {
        // Initialize the database
        especialidadeRepository.saveAndFlush(especialidade);

        int databaseSizeBeforeDelete = especialidadeRepository.findAll().size();

        // Get the especialidade
        restEspecialidadeMockMvc.perform(delete("/api/especialidades/{id}", especialidade.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Especialidade> especialidadeList = especialidadeRepository.findAll();
        assertThat(especialidadeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Especialidade.class);
        Especialidade especialidade1 = new Especialidade();
        especialidade1.setId(1L);
        Especialidade especialidade2 = new Especialidade();
        especialidade2.setId(especialidade1.getId());
        assertThat(especialidade1).isEqualTo(especialidade2);
        especialidade2.setId(2L);
        assertThat(especialidade1).isNotEqualTo(especialidade2);
        especialidade1.setId(null);
        assertThat(especialidade1).isNotEqualTo(especialidade2);
    }
}
