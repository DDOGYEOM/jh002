package net.openobject.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import net.openobject.domain.Etest;
import net.openobject.service.EtestQueryService;
import net.openobject.service.EtestService;
import net.openobject.service.dto.EtestCriteria;
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
 * REST controller for managing {@link net.openobject.domain.Etest}.
 */
@RestController
@RequestMapping("/api")
public class EtestResource {

    private final Logger log = LoggerFactory.getLogger(EtestResource.class);

    private static final String ENTITY_NAME = "etest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EtestService etestService;

    private final EtestQueryService etestQueryService;

    public EtestResource(EtestService etestService, EtestQueryService etestQueryService) {
        this.etestService = etestService;
        this.etestQueryService = etestQueryService;
    }

    /**
     * {@code POST  /etests} : Create a new etest.
     *
     * @param etest the etest to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new etest, or with status {@code 400 (Bad Request)} if the etest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/etests")
    public ResponseEntity<Etest> createEtest(@RequestBody Etest etest) throws URISyntaxException {
        log.debug("REST request to save Etest : {}", etest);
        if (etest.getId() != null) {
            throw new BadRequestAlertException("A new etest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Etest result = etestService.save(etest);
        return ResponseEntity
            .created(new URI("/api/etests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /etests} : Updates an existing etest.
     *
     * @param etest the etest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated etest,
     * or with status {@code 400 (Bad Request)} if the etest is not valid,
     * or with status {@code 500 (Internal Server Error)} if the etest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/etests")
    public ResponseEntity<Etest> updateEtest(@RequestBody Etest etest) throws URISyntaxException {
        log.debug("REST request to update Etest : {}", etest);
        if (etest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Etest result = etestService.save(etest);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, etest.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /etests} : Updates given fields of an existing etest.
     *
     * @param etest the etest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated etest,
     * or with status {@code 400 (Bad Request)} if the etest is not valid,
     * or with status {@code 404 (Not Found)} if the etest is not found,
     * or with status {@code 500 (Internal Server Error)} if the etest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/etests", consumes = "application/merge-patch+json")
    public ResponseEntity<Etest> partialUpdateEtest(@RequestBody Etest etest) throws URISyntaxException {
        log.debug("REST request to update Etest partially : {}", etest);
        if (etest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        Optional<Etest> result = etestService.partialUpdate(etest);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, etest.getId().toString())
        );
    }

    /**
     * {@code GET  /etests} : get all the etests.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of etests in body.
     */
    @GetMapping("/etests")
    public ResponseEntity<List<Etest>> getAllEtests(EtestCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Etests by criteria: {}", criteria);
        Page<Etest> page = etestQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /etests/count} : count all the etests.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/etests/count")
    public ResponseEntity<Long> countEtests(EtestCriteria criteria) {
        log.debug("REST request to count Etests by criteria: {}", criteria);
        return ResponseEntity.ok().body(etestQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /etests/:id} : get the "id" etest.
     *
     * @param id the id of the etest to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the etest, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/etests/{id}")
    public ResponseEntity<Etest> getEtest(@PathVariable Long id) {
        log.debug("REST request to get Etest : {}", id);
        Optional<Etest> etest = etestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(etest);
    }

    /**
     * {@code DELETE  /etests/:id} : delete the "id" etest.
     *
     * @param id the id of the etest to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/etests/{id}")
    public ResponseEntity<Void> deleteEtest(@PathVariable Long id) {
        log.debug("REST request to delete Etest : {}", id);
        etestService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
