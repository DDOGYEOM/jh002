package net.openobject.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import net.openobject.domain.Covid;
import net.openobject.service.CovidQueryService;
import net.openobject.service.CovidService;
import net.openobject.service.dto.CovidCriteria;
import net.openobject.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link net.openobject.domain.Covid}.
 */
@RestController
@RequestMapping("/api")
public class CovidResource {

    private final Logger log = LoggerFactory.getLogger(CovidResource.class);

    private static final String ENTITY_NAME = "covid";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CovidService covidService;

    private final CovidQueryService covidQueryService;

    public CovidResource(CovidService covidService, CovidQueryService covidQueryService) {
        this.covidService = covidService;
        this.covidQueryService = covidQueryService;
    }

    /**
     * {@code POST  /covids} : Create a new covid.
     *
     * @param covid the covid to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new covid, or with status {@code 400 (Bad Request)} if the covid has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/covids")
    public ResponseEntity<Covid> createCovid(@RequestBody Covid covid) throws URISyntaxException {
        log.debug("REST request to save Covid : {}", covid);
        if (covid.getId() != null) {
            throw new BadRequestAlertException("A new covid cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Covid result = covidService.save(covid);
        return ResponseEntity
            .created(new URI("/api/covids/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /covids} : Updates an existing covid.
     *
     * @param covid the covid to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated covid,
     * or with status {@code 400 (Bad Request)} if the covid is not valid,
     * or with status {@code 500 (Internal Server Error)} if the covid couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/covids")
    public ResponseEntity<Covid> updateCovid(@RequestBody Covid covid) throws URISyntaxException {
        log.debug("REST request to update Covid : {}", covid);
        if (covid.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Covid result = covidService.save(covid);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, covid.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /covids} : Updates given fields of an existing covid.
     *
     * @param covid the covid to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated covid,
     * or with status {@code 400 (Bad Request)} if the covid is not valid,
     * or with status {@code 404 (Not Found)} if the covid is not found,
     * or with status {@code 500 (Internal Server Error)} if the covid couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/covids", consumes = "application/merge-patch+json")
    public ResponseEntity<Covid> partialUpdateCovid(@RequestBody Covid covid) throws URISyntaxException {
        log.debug("REST request to update Covid partially : {}", covid);
        if (covid.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        Optional<Covid> result = covidService.partialUpdate(covid);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, covid.getId().toString())
        );
    }

    /**
     * {@code GET  /covids} : get all the covids.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of covids in body.
     */
    @GetMapping("/covids")
    public ResponseEntity<List<Covid>> getAllCovids(CovidCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Covids by criteria: {}", criteria);
        Page<Covid> page = covidQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /covids/count} : count all the covids.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/covids/count")
    public ResponseEntity<Long> countCovids(CovidCriteria criteria) {
        log.debug("REST request to count Covids by criteria: {}", criteria);
        return ResponseEntity.ok().body(covidQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /covids/:id} : get the "id" covid.
     *
     * @param id the id of the covid to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the covid, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/covids/{id}")
    public ResponseEntity<Covid> getCovid(@PathVariable Long id) {
        log.debug("REST request to get Covid : {}", id);
        Optional<Covid> covid = covidService.findOne(id);
        return ResponseUtil.wrapOrNotFound(covid);
    }

    /**
     * {@code DELETE  /covids/:id} : delete the "id" covid.
     *
     * @param id the id of the covid to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/covids/{id}")
    public ResponseEntity<Void> deleteCovid(@PathVariable Long id) {
        log.debug("REST request to delete Covid : {}", id);
        covidService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
