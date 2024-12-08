package id.lariss.repository;

import id.lariss.domain.CaseSize;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CaseSize entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CaseSizeRepository extends JpaRepository<CaseSize, Long> {}
