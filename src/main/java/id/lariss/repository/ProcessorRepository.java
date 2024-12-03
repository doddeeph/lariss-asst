package id.lariss.repository;

import id.lariss.domain.Processor;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Processor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProcessorRepository extends JpaRepository<Processor, Long> {}
