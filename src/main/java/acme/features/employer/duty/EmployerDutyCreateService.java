
package acme.features.employer.duty;

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
import acme.framework.services.AbstractCreateService;

@Service
public class EmployerDutyCreateService implements AbstractCreateService<Employer, Duty> {

	//	INTERNAL STATES ----------------------------------------

	@Autowired
	EmployerDutyRepository repository;


	//	AbstractUpdateService<Employer, Duty> interface -----

	@Override
	public boolean authorise(final Request<Duty> request) {
		assert request != null;

		boolean result;
		int jobId;
		Employer employer;
		Principal principal;
		Job job;
		Double workPercentage = 0.0;

		jobId = request.getModel().getInteger("jobId");
		job = this.repository.findJobById(jobId);
		employer = job.getEmployer();
		principal = request.getPrincipal();

		Collection<Duty> collection = this.repository.findDutiesByDescriptor(job.getDescriptor().getId());

		for (Duty d : collection) {
			workPercentage += d.getPercentage();
		}

		result = employer.getUserAccount().getId() == principal.getAccountId() && !job.isFinalMode() && workPercentage < 100.0;

		return result;
	}

	@Override
	public void bind(final Request<Duty> request, final Duty entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		request.bind(entity, errors, "descriptor");
	}

	@Override
	public void unbind(final Request<Duty> request, final Duty entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "title", "percentage", "description");
	}

	@Override
	public Duty instantiate(final Request<Duty> request) {
		Duty result;

		result = new Duty();

		Job job;
		int jobId;
		jobId = request.getModel().getInteger("jobId");
		job = this.repository.findJobById(jobId);

		result.setDescriptor(job.getDescriptor());

		return result;
	}

	@Override
	public void validate(final Request<Duty> request, final Duty entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		Boolean isNegative = !(request.getModel().getDouble("percentage") < 0.00);
		Boolean isHigher = !(request.getModel().getDouble("percentage") > 100.00);
		errors.state(request, isNegative, "percentage", "employer.duty.error.negative");
		errors.state(request, isHigher, "percentage", "employer.duty.error.higher");

	}

	@Override
	public void create(final Request<Duty> request, final Duty entity) {

		this.repository.save(entity);
	}

}
