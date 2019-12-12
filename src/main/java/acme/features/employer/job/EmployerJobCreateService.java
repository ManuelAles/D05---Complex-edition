
package acme.features.employer.job;

import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.jobs.Job;
import acme.entities.roles.Employer;
import acme.framework.components.Errors;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Principal;
import acme.framework.services.AbstractCreateService;

public class EmployerJobCreateService implements AbstractCreateService<Employer, Job> {

	//	Internal states ------------------

	@Autowired
	private EmployerJobRepository repository;


	// AbstractShowService<Employer, Job> interface -----

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

		request.unbind(entity, model, "reference", "title", "deadline", "salary", "moreInfo", "descriptor.description", "finalMode");

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
		//poner finalMode?

		return result;
	}

	@Override
	public void validate(final Request<Job> request, final Job entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		/*
		 * A job cannot be saved in final mode unless it has a descriptor,
		 * the duties sum up to 100% the weekly workload, and it’s not considered spam.
		 */

	}

	@Override
	public void create(final Request<Job> request, final Job entity) {
		assert request != null;
		assert entity != null;

		this.repository.save(entity);

	}

	/*
	 * @Override
	 * public void onSuccess(final Request<Job> request, final Response<Job> response) {
	 * assert request != null;
	 * assert response != null;
	 *
	 * if (request.isMethod(HttpMethod.POST)) {
	 * PrincipalHelper.handleUpdate();
	 * }
	 * }
	 */
}
