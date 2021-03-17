package net.openobject.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import net.openobject.domain.*; // for static metamodels
import net.openobject.domain.Covid;
import net.openobject.repository.CovidRepository;
import net.openobject.service.dto.CovidCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Covid} entities in the database.
 * The main input is a {@link CovidCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Covid} or a {@link Page} of {@link Covid} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CovidQueryService extends QueryService<Covid> {

    private final Logger log = LoggerFactory.getLogger(CovidQueryService.class);

    private final CovidRepository covidRepository;

    public CovidQueryService(CovidRepository covidRepository) {
        this.covidRepository = covidRepository;
    }

    /**
     * Return a {@link List} of {@link Covid} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Covid> findByCriteria(CovidCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Covid> specification = createSpecification(criteria);
        return covidRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Covid} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Covid> findByCriteria(CovidCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Covid> specification = createSpecification(criteria);
        return covidRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CovidCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Covid> specification = createSpecification(criteria);
        return covidRepository.count(specification);
    }

    /**
     * Function to convert {@link CovidCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Covid> createSpecification(CovidCriteria criteria) {
        Specification<Covid> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Covid_.id));
            }
            if (criteria.getCid() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCid(), Covid_.cid));
            }
            if (criteria.getCtype() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCtype(), Covid_.ctype));
            }
            if (criteria.getCentername() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCentername(), Covid_.centername));
            }
            if (criteria.getCoi() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCoi(), Covid_.coi));
            }
            if (criteria.getFacilityname() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFacilityname(), Covid_.facilityname));
            }
            if (criteria.getZipcode() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getZipcode(), Covid_.zipcode));
            }
            if (criteria.getAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress(), Covid_.address));
            }
        }
        return specification;
    }
}
