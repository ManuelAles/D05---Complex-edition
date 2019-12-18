
package acme.features.employer.application;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.applications.Application;
import acme.entities.configurations.Configuration;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface EmployerApplicationRepository extends AbstractRepository {

	@Query("select a from Application a where a.id = ?1")
	Application findApplicationById(int id);

	@Query("select a from Application a where a.job.employer.id = ?1" + " order by a.reference, a.status, a.moment")
	Collection<Application> findApplicationsByJob(int id);

	@Query("select c from Configuration c")
	Configuration selectConfiguration();
}
