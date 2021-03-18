package net.openobject.service;

import java.util.Optional;
import net.openobject.domain.Etest;
import net.openobject.repository.EtestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Etest}.
 */
@Service
@Transactional
public class EtestService {

    private final Logger log = LoggerFactory.getLogger(EtestService.class);

    private final EtestRepository etestRepository;

    public EtestService(EtestRepository etestRepository) {
        this.etestRepository = etestRepository;
    }

    /**
     * Save a etest.
     *
     * @param etest the entity to save.
     * @return the persisted entity.
     */
    public Etest save(Etest etest) {
        log.debug("Request to save Etest : {}", etest);
        return etestRepository.save(etest);
    }

    /**
     * Partially update a etest.
     *
     * @param etest the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Etest> partialUpdate(Etest etest) {
        log.debug("Request to partially update Etest : {}", etest);

        return etestRepository
            .findById(etest.getId())
            .map(
                existingEtest -> {
                    if (etest.getTestname() != null) {
                        existingEtest.setTestname(etest.getTestname());
                    }

                    if (etest.getTestnum() != null) {
                        existingEtest.setTestnum(etest.getTestnum());
                    }

                    if (etest.getTestaddress() != null) {
                        existingEtest.setTestaddress(etest.getTestaddress());
                    }

                    if (etest.getTestphone() != null) {
                        existingEtest.setTestphone(etest.getTestphone());
                    }

                    return existingEtest;
                }
            )
            .map(etestRepository::save);
    }

    /**
     * Get all the etests.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Etest> findAll(Pageable pageable) {
        log.debug("Request to get all Etests");
        return etestRepository.findAll(pageable);
    }

    /**
     * Get one etest by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Etest> findOne(Long id) {
        log.debug("Request to get Etest : {}", id);
        return etestRepository.findById(id);
    }

    /**
     * Delete the etest by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Etest : {}", id);
        etestRepository.deleteById(id);
    }
}
