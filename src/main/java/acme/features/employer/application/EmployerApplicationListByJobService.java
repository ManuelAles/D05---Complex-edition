
package acme.features.employer.application;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.applications.Application;
import acme.entities.roles.Employer;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Principal;
import acme.framework.services.AbstractListService;

@Service
public class EmployerApplicationListByJobService implements AbstractListService<Employer, Application> {

	// Internal interface --------------------

	@Autowired
	EmployerApplicationRepository repository;


	// AbstractListService<Employer, Application> interface ------

	@Override
	public boolean authorise(final Request<Application> request) {
		assert request != null;

		return true;
	}

	@Override
	public void unbind(final Request<Application> request, final Application entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "reference", "moment", "status", "statement", "skills", "qualifications");
	}

	@Override
	public Collection<Application> findMany(final Request<Application> request) {
		assert request != null;

		Collection<Application> result;
		Principal principal;

		principal = request.getPrincipal();
		result = this.repository.findApplicationsByJob(principal.getActiveRoleId());
		return result;
	}

}
