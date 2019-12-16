
package acme.features.employer.job;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.descriptors.Descriptor;
import acme.entities.jobs.Job;
import acme.entities.roles.Employer;
import acme.framework.components.Errors;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Principal;
import acme.framework.services.AbstractCreateService;

@Service
public class EmployerJobCreateService implements AbstractCreateService<Employer, Job> {

	//	Internal states ------------------

	@Autowired
	private EmployerJobRepository repository;


	// AbstractCreateService<Employer, Job> interface -----

	@Override
	public boolean authorise(final Request<Job> request) {
		assert request != null;

		return true;
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

		request.unbind(entity, model, "reference", "title", "deadline", "salary", "moreInfo", "descriptor", "finalMode");

	}

	@Override
	public Job instantiate(final Request<Job> request) {

		Job result;
		Principal principal;
		int userAccountId;
		Employer employer;

		principal = request.getPrincipal();
		userAccountId = principal.getActiveRoleId();
		employer = this.repository.findEmployerById(userAccountId);

		result = new Job();
		result.setEmployer(employer);

		result.setFinalMode(false);

		return result;
	}

	@Override
	public void validate(final Request<Job> request, final Job entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		Calendar calendar;
		Date minimumDeadline;

		// validar deadline
		if (!errors.hasErrors("deadline")) {
			calendar = new GregorianCalendar();
			minimumDeadline = calendar.getTime();
			Boolean isAfter = entity.getDeadline().after(minimumDeadline);
			errors.state(request, isAfter, "deadline", "employer.job.deadline.before");
		}
		// Validar moneda del salario
		if (!errors.hasErrors("salary")) {
			Boolean isEUR = entity.getSalary().getCurrency().equals("€") || entity.getSalary().getCurrency().equals("EUR");
			errors.state(request, isEUR, "salary", "employer.job.salary.eur");
		}
		// Validar salario que negativo, ni 0
		if (!errors.hasErrors("salary")) {
			Boolean higher = entity.getSalary().getAmount() > 0.00;
			errors.state(request, higher, "salary", "employer.job.salary.higher");
		}

		//Comprueba que el reference es único
		Boolean notUnique = null;
		notUnique = this.repository.findByRefence(entity.getReference()) != null;
		errors.state(request, !notUnique, "reference", "employer.job.error.reference");

	}

	@Override
	public void create(final Request<Job> request, final Job entity) {
		assert request != null;
		assert entity != null;

		Descriptor descriptor;
		descriptor = new Descriptor();
		String description;

		description = request.getModel().getString("descriptor.description");
		descriptor.setDescription(description);
		entity.setDescriptor(descriptor);

		this.repository.save(descriptor);
		this.repository.save(entity);

	}

}
