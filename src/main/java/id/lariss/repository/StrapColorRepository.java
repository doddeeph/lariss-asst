package id.lariss.repository;

import id.lariss.domain.StrapColor;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the StrapColor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StrapColorRepository extends JpaRepository<StrapColor, Long> {}
