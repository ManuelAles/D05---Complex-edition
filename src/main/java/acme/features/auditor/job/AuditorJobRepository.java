
package acme.features.auditor.job;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.jobs.Job;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface AuditorJobRepository extends AbstractRepository {

	@Query("select j from Job j where j.id = ?1")
	Job findJobById(int id);

	@Query("select a.job from AuditRecord a where a.id = ?1")
	Job findJobByAuditRecordId(int id);

	@Query("select j from Job j where j.id in (select ar.job.id from AuditRecord ar where ar.auditor.id = ?1)")
	Collection<Job> findJobsMadeByAuditor(int auditorId);

	@Query("select j from Job j where j.id not in (select ar.job.id from AuditRecord ar where ar.auditor.id = ?1)")
	Collection<Job> findJobsMadeByOthersAuditors(int auditorId);
}
