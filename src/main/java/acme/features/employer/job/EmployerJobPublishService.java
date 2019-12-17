
package acme.features.employer.job;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.duties.Duty;
import acme.entities.jobs.Job;
import acme.entities.roles.Employer;
import acme.framework.components.Errors;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Principal;
import acme.framework.services.AbstractUpdateService;

@Service
public class EmployerJobPublishService implements AbstractUpdateService<Employer, Job> {

	//	Internal states ------------------

	@Autowired
	private EmployerJobRepository repository;


	// AbstractUpdateService<Employer, Job> interface -----

	@Override
	public boolean authorise(final Request<Job> request) {
		assert request != null;

		boolean result;
		int jobId;
		Job job;
		Employer employer;
		Principal principal;

		jobId = request.getModel().getInteger("id");
		job = this.repository.findJobById(jobId);
		employer = job.getEmployer();
		principal = request.getPrincipal();

		boolean finalMode;
		finalMode = job.isFinalMode();

		result = employer.getUserAccount().getId() == principal.getAccountId() && !finalMode;

		return result;
	}

	@Override
	public void bind(final Request<Job> request, final Job entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		request.bind(entity, errors);
	}

	@Override
	public void unbind(final Request<Job> request, final Job entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "finalMode");
	}

	@Override
	public Job findOne(final Request<Job> request) {
		assert request != null;

		Job result;
		int jobId;

		jobId = request.getModel().getInteger("id");
		result = this.repository.findJobById(jobId);

		return result;
	}

	@Override
	public void validate(final Request<Job> request, final Job entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		// Un job debe tener un descriptor y el porcentaje de trabajo sumar 100 si va a ser pasado a modo público
		if (!errors.hasErrors("finalMode")) {
			Boolean notWorkload, notDescriptor;
			Double workload = 0.0;
			Collection<Duty> duties = this.repository.findDutiesByDescriptor(entity.getDescriptor().getId());
			for (Duty d : duties) {
				workload += d.getPercentage();
			}
			notWorkload = workload.equals(100.00);
			notDescriptor = entity.getDescriptor() != null;
			errors.state(request, notDescriptor, "finalMode", "employer.job.error.descriptor");
			errors.state(request, notWorkload, "finalMode", "employer.job.error.workload");
		}

	}

	@Override
	public void update(final Request<Job> request, final Job entity) {
		assert request != null;
		assert entity != null;

		entity.setFinalMode(true);
		this.repository.save(entity);
	}

}
