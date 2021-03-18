package net.openobject.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import net.openobject.domain.*; // for static metamodels
import net.openobject.domain.Etest;
import net.openobject.repository.EtestRepository;
import net.openobject.service.dto.EtestCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Etest} entities in the database.
 * The main input is a {@link EtestCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Etest} or a {@link Page} of {@link Etest} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EtestQueryService extends QueryService<Etest> {

    private final Logger log = LoggerFactory.getLogger(EtestQueryService.class);

    private final EtestRepository etestRepository;

    public EtestQueryService(EtestRepository etestRepository) {
        this.etestRepository = etestRepository;
    }

    /**
     * Return a {@link List} of {@link Etest} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Etest> findByCriteria(EtestCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Etest> specification = createSpecification(criteria);
        return etestRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Etest} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Etest> findByCriteria(EtestCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Etest> specification = createSpecification(criteria);
        return etestRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EtestCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Etest> specification = createSpecification(criteria);
        return etestRepository.count(specification);
    }

    /**
     * Function to convert {@link EtestCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Etest> createSpecification(EtestCriteria criteria) {
        Specification<Etest> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Etest_.id));
            }
            if (criteria.getTestname() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTestname(), Etest_.testname));
            }
            if (criteria.getTestnum() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTestnum(), Etest_.testnum));
            }
            if (criteria.getTestaddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTestaddress(), Etest_.testaddress));
            }
            if (criteria.getTestphone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTestphone(), Etest_.testphone));
            }
        }
        return specification;
    }
}
