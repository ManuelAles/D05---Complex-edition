
package acme.features.employer.job;

import org.springframework.stereotype.Service;

import acme.entities.jobs.Job;
import acme.entities.roles.Employer;
import acme.framework.components.Errors;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.services.AbstractUpdateService;

@Service
public class EmployerJobUpdateService implements AbstractUpdateService<Employer, Job> {

	@Override
	public boolean authorise(final Request<Job> request) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void bind(final Request<Job> request, final Job entity, final Errors errors) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unbind(final Request<Job> request, final Job entity, final Model model) {
		// TODO Auto-generated method stub

	}

	@Override
	public Job findOne(final Request<Job> request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validate(final Request<Job> request, final Job entity, final Errors errors) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(final Request<Job> request, final Job entity) {
		// TODO Auto-generated method stub

	}

}
