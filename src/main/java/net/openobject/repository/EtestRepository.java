package net.openobject.repository;

import net.openobject.domain.Etest;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Etest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EtestRepository extends JpaRepository<Etest, Long>, JpaSpecificationExecutor<Etest> {}
