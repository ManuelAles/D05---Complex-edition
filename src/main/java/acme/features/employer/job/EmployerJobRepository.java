
package acme.features.employer.job;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.descriptors.Descriptor;
import acme.entities.duties.Duty;
import acme.entities.jobs.Job;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface EmployerJobRepository extends AbstractRepository {

	@Query("select j from Job j where j.id = ?1")
	Job findJobById(int id);

	@Query("select d from Job d where d.id = ?1")
	Duty findDutyById(int id);

	@Query("select j from Job j where j.employer.id = ?1")
	Collection<Job> findManyByEmployerId(int emplopyerId);

	@Query("select j from Job j where j.descriptor = ?1")
	Job findJobByDuty(Descriptor descriptor);

}
