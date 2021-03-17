package net.openobject.repository;

import net.openobject.domain.Covid;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Covid entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CovidRepository extends JpaRepository<Covid, Long>, JpaSpecificationExecutor<Covid> {}
