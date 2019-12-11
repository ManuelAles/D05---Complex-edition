
package acme.features.worker.application;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.applications.Application;
import acme.entities.jobs.Job;
import acme.entities.roles.Worker;
import acme.framework.components.Errors;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Principal;
import acme.framework.services.AbstractCreateService;

@Service
public class WorkerApplicationCreateService implements AbstractCreateService<Worker, Application> {

	// Internal state ---------------------------------------------------------

	@Autowired
	WorkerApplicationRepository repository;


	// AbstractCreateService<Worker, Application> interface ---------------

	@Override
	public boolean authorise(final Request<Application> request) {
		assert request != null;

		return true;
	}

	@Override
	public void bind(final Request<Application> request, final Application entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		request.bind(entity, errors, "moment", "status", "worker", "job");

	}

	@Override
	public void unbind(final Request<Application> request, final Application entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "reference", "statement", "skills", "qualifications");
	}

	@Override
	public Application instantiate(final Request<Application> Application) {
		Application result;

		result = new Application();

		return result;
	}

	@Override
	public void validate(final Request<Application> request, final Application entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		//Comprueba que el reference es único
		Boolean notUnique = null;
		notUnique = this.repository.findByRefence(entity.getReference()) != null;
		errors.state(request, !notUnique, "reference", "worker.application.error.ticker");

	}

	@Override
	public void create(final Request<Application> request, final Application entity) {
		Date moment;
		String status;
		Worker worker;
		Job job;

		moment = new Date(System.currentTimeMillis() - 1);
		entity.setMoment(moment);

		status = "PENDING";
		entity.setStatus(status);

		int workerId;
		Principal principal;
		principal = request.getPrincipal();
		workerId = principal.getAccountId();
		worker = this.repository.findWorkerByUserAccountId(workerId);
		entity.setWorker(worker);

		int jobId;
		jobId = request.getModel().getInteger("id");
		job = this.repository.findJobById(jobId);
		entity.setJob(job);

		this.repository.save(entity);

	}

}
