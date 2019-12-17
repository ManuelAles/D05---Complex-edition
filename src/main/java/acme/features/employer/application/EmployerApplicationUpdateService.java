/*
 * AuthenticatedWorkerUpdateService.java
 *
 * Copyright (c) 2019 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.employer.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.applications.Application;
import acme.entities.roles.Employer;
import acme.framework.components.Errors;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Principal;
import acme.framework.services.AbstractUpdateService;

@Service
public class EmployerApplicationUpdateService implements AbstractUpdateService<Employer, Application> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private EmployerApplicationRepository repository;


	// AbstractUpdateService<Authenticated, Worker> interface ---------------

	@Override
	public boolean authorise(final Request<Application> request) {
		assert request != null;

		boolean result;

		Application application;
		Integer applicationId;
		applicationId = request.getModel().getInteger("id");
		application = this.repository.findApplicationById(applicationId);

		Employer employer;
		Principal principal;
		employer = application.getJob().getEmployer();
		principal = request.getPrincipal();

		result = employer.getUserAccount().getId() == principal.getAccountId() && application.getStatus().equals("PENDING");

		return result;
	}

	@Override
	public void bind(final Request<Application> request, final Application entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		request.bind(entity, errors);
	}

	@Override
	public void unbind(final Request<Application> request, final Application entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "status", "rejectedDecision");

	}

	@Override
	public Application findOne(final Request<Application> request) {
		assert request != null;

		Application result;
		Integer id;

		id = request.getModel().getInteger("id");
		result = this.repository.findApplicationById(id);

		return result;
	}

	@Override
	public void validate(final Request<Application> request, final Application entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		Boolean isCorrect = entity.getStatus().equals("ACCEPTED") || entity.getStatus().equals("REJECTED") || entity.getStatus().equals("PENDING");
		errors.state(request, isCorrect, "status", "employer.application.status");

		Boolean decision;
		if (!errors.hasErrors("finalMode") && entity.getStatus().equals("REJECTED")) {
			decision = entity.getRejectedDecision() == null || entity.getRejectedDecision().isEmpty();
			errors.state(request, !decision, "rejectedDecision", "employer.application.decision");
		}
	}

	@Override
	public void update(final Request<Application> request, final Application entity) {
		assert request != null;
		assert entity != null;

		this.repository.save(entity);
	}

}
