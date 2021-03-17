package net.openobject.service;

import java.util.Optional;
import net.openobject.domain.Covid;
import net.openobject.repository.CovidRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Covid}.
 */
@Service
@Transactional
public class CovidService {

    private final Logger log = LoggerFactory.getLogger(CovidService.class);

    private final CovidRepository covidRepository;

    public CovidService(CovidRepository covidRepository) {
        this.covidRepository = covidRepository;
    }

    /**
     * Save a covid.
     *
     * @param covid the entity to save.
     * @return the persisted entity.
     */
    public Covid save(Covid covid) {
        log.debug("Request to save Covid : {}", covid);
        return covidRepository.save(covid);
    }

    /**
     * Partially update a covid.
     *
     * @param covid the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Covid> partialUpdate(Covid covid) {
        log.debug("Request to partially update Covid : {}", covid);

        return covidRepository
            .findById(covid.getId())
            .map(
                existingCovid -> {
                    if (covid.getCid() != null) {
                        existingCovid.setCid(covid.getCid());
                    }

                    if (covid.getCtype() != null) {
                        existingCovid.setCtype(covid.getCtype());
                    }

                    if (covid.getCentername() != null) {
                        existingCovid.setCentername(covid.getCentername());
                    }

                    if (covid.getCoi() != null) {
                        existingCovid.setCoi(covid.getCoi());
                    }

                    if (covid.getFacilityname() != null) {
                        existingCovid.setFacilityname(covid.getFacilityname());
                    }

                    if (covid.getZipcode() != null) {
                        existingCovid.setZipcode(covid.getZipcode());
                    }

                    if (covid.getAddress() != null) {
                        existingCovid.setAddress(covid.getAddress());
                    }

                    return existingCovid;
                }
            )
            .map(covidRepository::save);
    }

    /**
     * Get all the covids.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Covid> findAll(Pageable pageable) {
        log.debug("Request to get all Covids");
        return covidRepository.findAll(pageable);
    }

    /**
     * Get one covid by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Covid> findOne(Long id) {
        log.debug("Request to get Covid : {}", id);
        return covidRepository.findById(id);
    }

    /**
     * Delete the covid by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Covid : {}", id);
        covidRepository.deleteById(id);
    }
}
