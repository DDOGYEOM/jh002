package net.openobject.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import net.openobject.domain.*; // for static metamodels
import net.openobject.domain.Comment;
import net.openobject.repository.CommentRepository;
import net.openobject.service.dto.CommentCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Comment} entities in the database.
 * The main input is a {@link CommentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Comment} or a {@link Page} of {@link Comment} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CommentQueryService extends QueryService<Comment> {

    private final Logger log = LoggerFactory.getLogger(CommentQueryService.class);

    private final CommentRepository commentRepository;

    public CommentQueryService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    /**
     * Return a {@link List} of {@link Comment} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Comment> findByCriteria(CommentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Comment> specification = createSpecification(criteria);
        return commentRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Comment} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Comment> findByCriteria(CommentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Comment> specification = createSpecification(criteria);
        return commentRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CommentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Comment> specification = createSpecification(criteria);
        return commentRepository.count(specification);
    }

    /**
     * Function to convert {@link CommentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Comment> createSpecification(CommentCriteria criteria) {
        Specification<Comment> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Comment_.id));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), Comment_.date));
            }
            if (criteria.getText() != null) {
                specification = specification.and(buildStringSpecification(criteria.getText(), Comment_.text));
            }
            if (criteria.getParentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getParentId(), root -> root.join(Comment_.parents, JoinType.LEFT).get(Comment_.id))
                    );
            }
            if (criteria.getLoginId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getLoginId(), root -> root.join(Comment_.login, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getChildId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getChildId(), root -> root.join(Comment_.child, JoinType.LEFT).get(Comment_.id))
                    );
            }
        }
        return specification;
    }
}
