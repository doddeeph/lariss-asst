package id.lariss.repository;

import id.lariss.domain.Screen;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Screen entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ScreenRepository extends JpaRepository<Screen, Long> {}
