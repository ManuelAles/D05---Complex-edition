
package acme.features.authenticated.job;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.jobs.Job;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Authenticated;
import acme.framework.services.AbstractShowService;

@Service
public class AuthenticatedJobShowService implements AbstractShowService<Authenticated, Job> {

	//		Internal states ------------------

	@Autowired
	private AuthenticatedJobRepository repository;


	// AbstractShowService<Authenticated, Job> interface -----

	@Override
	public boolean authorise(final Request<Job> request) {
		assert request != null;

		Boolean result;

		Job job;
		int id;
		id = request.getModel().getInteger("id");
		job = this.repository.findJobById(id);

		Date moment;
		moment = new Date();

		result = job.getDeadline().after(moment) && job.isFinalMode();

		return result;
	}

	@Override
	public void unbind(final Request<Job> request, final Job entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "reference", "title", "deadline");
		request.unbind(entity, model, "salary", "moreInfo", "descriptor.description", "finalMode");

	}

	@Override
	public Job findOne(final Request<Job> request) {
		assert request != null;

		Job result;
		int id;

		id = request.getModel().getInteger("id");
		result = this.repository.findJobById(id);

		return result;
	}
}
