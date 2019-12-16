
package acme.features.authenticated.duty;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.descriptors.Descriptor;
import acme.entities.duties.Duty;
import acme.entities.jobs.Job;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Authenticated;
import acme.framework.services.AbstractShowService;

@Service
public class AuthenticatedDutyShowService implements AbstractShowService<Authenticated, Duty> {

	//		Internal states ------------------

	@Autowired
	private AuthenticatedDutyRepository repository;


	// AbstractShowService<Authenticated, Duty> interface -----

	@Override
	public boolean authorise(final Request<Duty> request) {
		assert request != null;

		boolean result;

		Job job;
		Descriptor descriptor;
		int id;

		id = request.getModel().getInteger("id");
		descriptor = this.repository.findDutyById(id).getDescriptor();
		job = this.repository.findJobByDescriptor(descriptor.getId());

		Date moment;
		moment = new Date();
		result = job.getDeadline().after(moment) && job.isFinalMode();

		return result;
	}

	@Override
	public void unbind(final Request<Duty> request, final Duty entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "description", "title", "percentage");

	}

	@Override
	public Duty findOne(final Request<Duty> request) {
		assert request != null;

		Duty result;
		int id;

		id = request.getModel().getInteger("id");
		result = this.repository.findDutyById(id);

		return result;
	}
}
