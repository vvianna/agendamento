package br.com.jvmsistemas.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.jvmsistemas.domain.PlanoDeSaude;
import br.com.jvmsistemas.repository.PlanoDeSaudeRepository;
import br.com.jvmsistemas.web.rest.errors.BadRequestAlertException;
import br.com.jvmsistemas.web.rest.util.HeaderUtil;
import br.com.jvmsistemas.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing PlanoDeSaude.
 */
@RestController
@RequestMapping("/api")
public class PlanoDeSaudeResource {

    private final Logger log = LoggerFactory.getLogger(PlanoDeSaudeResource.class);

    private static final String ENTITY_NAME = "planoDeSaude";

    private final PlanoDeSaudeRepository planoDeSaudeRepository;

    public PlanoDeSaudeResource(PlanoDeSaudeRepository planoDeSaudeRepository) {
        this.planoDeSaudeRepository = planoDeSaudeRepository;
    }

    /**
     * POST  /plano-de-saudes : Create a new planoDeSaude.
     *
     * @param planoDeSaude the planoDeSaude to create
     * @return the ResponseEntity with status 201 (Created) and with body the new planoDeSaude, or with status 400 (Bad Request) if the planoDeSaude has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/plano-de-saudes")
    @Timed
    public ResponseEntity<PlanoDeSaude> createPlanoDeSaude(@RequestBody PlanoDeSaude planoDeSaude) throws URISyntaxException {
        log.debug("REST request to save PlanoDeSaude : {}", planoDeSaude);
        if (planoDeSaude.getId() != null) {
            throw new BadRequestAlertException("A new planoDeSaude cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PlanoDeSaude result = planoDeSaudeRepository.save(planoDeSaude);
        return ResponseEntity.created(new URI("/api/plano-de-saudes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /plano-de-saudes : Updates an existing planoDeSaude.
     *
     * @param planoDeSaude the planoDeSaude to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated planoDeSaude,
     * or with status 400 (Bad Request) if the planoDeSaude is not valid,
     * or with status 500 (Internal Server Error) if the planoDeSaude couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/plano-de-saudes")
    @Timed
    public ResponseEntity<PlanoDeSaude> updatePlanoDeSaude(@RequestBody PlanoDeSaude planoDeSaude) throws URISyntaxException {
        log.debug("REST request to update PlanoDeSaude : {}", planoDeSaude);
        if (planoDeSaude.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PlanoDeSaude result = planoDeSaudeRepository.save(planoDeSaude);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, planoDeSaude.getId().toString()))
            .body(result);
    }

    /**
     * GET  /plano-de-saudes : get all the planoDeSaudes.
     *
     * @param pageable the pagination information
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of planoDeSaudes in body
     */
    @GetMapping("/plano-de-saudes")
    @Timed
    public ResponseEntity<List<PlanoDeSaude>> getAllPlanoDeSaudes(Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of PlanoDeSaudes");
        Page<PlanoDeSaude> page;
        if (eagerload) {
            page = planoDeSaudeRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = planoDeSaudeRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, String.format("/api/plano-de-saudes?eagerload=%b", eagerload));
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /plano-de-saudes/:id : get the "id" planoDeSaude.
     *
     * @param id the id of the planoDeSaude to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the planoDeSaude, or with status 404 (Not Found)
     */
    @GetMapping("/plano-de-saudes/{id}")
    @Timed
    public ResponseEntity<PlanoDeSaude> getPlanoDeSaude(@PathVariable Long id) {
        log.debug("REST request to get PlanoDeSaude : {}", id);
        Optional<PlanoDeSaude> planoDeSaude = planoDeSaudeRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(planoDeSaude);
    }

    /**
     * DELETE  /plano-de-saudes/:id : delete the "id" planoDeSaude.
     *
     * @param id the id of the planoDeSaude to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/plano-de-saudes/{id}")
    @Timed
    public ResponseEntity<Void> deletePlanoDeSaude(@PathVariable Long id) {
        log.debug("REST request to delete PlanoDeSaude : {}", id);

        planoDeSaudeRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
