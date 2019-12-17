
package acme.features.employer.job;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.auditRecords.AuditRecord;
import acme.entities.configurations.Configuration;
import acme.entities.descriptors.Descriptor;
import acme.entities.duties.Duty;
import acme.entities.jobs.Job;
import acme.entities.roles.Employer;
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

	@Query("select e from Employer e where e.id = ?1")
	Employer findEmployerById(int id);

	@Query("select count(a) from Application a where a.job.id = ?1")
	Integer countApplicationsByJob(int id);

	@Query("select j from Job j where j.reference = ?1")
	Job findByRefence(String reference);

	@Query("select d from Duty d where d.descriptor.id = ?1")
	Collection<Duty> findDutiesByDescriptor(int id);

	@Query("select d from AuditRecord d where d.job.id = ?1")
	Collection<AuditRecord> findAuditRecordByJob(int id);

	@Query("select c from Configuration c")
	Configuration selectConfiguration();

}
