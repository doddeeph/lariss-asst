package id.lariss.repository;

import id.lariss.domain.Billing;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Billing entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BillingRepository extends JpaRepository<Billing, Long> {}
