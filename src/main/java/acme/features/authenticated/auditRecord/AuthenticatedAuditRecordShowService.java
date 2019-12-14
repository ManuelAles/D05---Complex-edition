
package acme.features.authenticated.auditRecord;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.auditRecords.AuditRecord;
import acme.entities.jobs.Job;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Authenticated;
import acme.framework.services.AbstractShowService;

@Service
public class AuthenticatedAuditRecordShowService implements AbstractShowService<Authenticated, AuditRecord> {

	//		Internal states ------------------

	@Autowired
	private AuthenticatedAuditRecordRepository repository;


	// AbstractShowService<Authenticated, AuditRecord> interface -----

	@Override
	public boolean authorise(final Request<AuditRecord> request) {
		assert request != null;

		boolean result;

		Job job;
		AuditRecord record;
		int id;

		id = request.getModel().getInteger("id");
		record = this.repository.findAuditRecordById(id);
		job = record.getJob();

		Date moment;
		moment = new Date();
		result = job.getDeadline().after(moment) && job.isFinalMode() && record.getFinalMode();

		return result;
	}

	@Override
	public void unbind(final Request<AuditRecord> request, final AuditRecord entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "title", "moment", "body");

	}

	@Override
	public AuditRecord findOne(final Request<AuditRecord> request) {
		assert request != null;

		AuditRecord result;
		int id;

		id = request.getModel().getInteger("id");
		result = this.repository.findAuditRecordById(id);

		return result;
	}
}
