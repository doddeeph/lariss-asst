package id.lariss.repository;

import id.lariss.domain.StrapSize;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the StrapSize entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StrapSizeRepository extends JpaRepository<StrapSize, Long> {}
