
package acme.features.auditor.duty;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.duties.Duty;
import acme.entities.jobs.Job;
import acme.entities.roles.Auditor;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.services.AbstractListService;

@Service
public class AuditorDutyListByJobService implements AbstractListService<Auditor, Duty> {

	// Internal interface --------------------

	@Autowired
	AuditorDutyRepository repository;


	// AbstractListService<Auditor, Duty> interface ------

	@Override
	public boolean authorise(final Request<Duty> request) {
		assert request != null;

		return true;
	}

	@Override
	public void unbind(final Request<Duty> request, final Duty entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "description", "title", "percentage");
	}

	@Override
	public Collection<Duty> findMany(final Request<Duty> request) {
		assert request != null;

		Collection<Duty> result;
		Job job;

		job = this.repository.findJobById(request.getModel().getInteger("id"));
		result = this.repository.findDutiesByDescriptor(job.getDescriptor().getId());

		return result;
	}

}
