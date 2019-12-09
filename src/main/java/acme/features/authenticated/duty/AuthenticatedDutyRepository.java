
package acme.features.authenticated.duty;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.duties.Duty;
import acme.entities.jobs.Job;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface AuthenticatedDutyRepository extends AbstractRepository {

	@Query("select j from Job j where j.id = ?1")
	Job findJobById(int id);

	@Query("select d from Duty d where d.id = ?1")
	Duty findDutyById(int id);

	@Query("select d from Duty d where d.descriptor.id = ?1")
	Collection<Duty> findDutiesByDescriptor(int id);

}
