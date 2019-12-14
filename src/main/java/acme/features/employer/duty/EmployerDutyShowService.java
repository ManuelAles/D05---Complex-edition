
package acme.features.employer.duty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.descriptors.Descriptor;
import acme.entities.duties.Duty;
import acme.entities.jobs.Job;
import acme.entities.roles.Employer;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Principal;
import acme.framework.services.AbstractShowService;

@Service
public class EmployerDutyShowService implements AbstractShowService<Employer, Duty> {

	//		Internal states ------------------

	@Autowired
	private EmployerDutyRepository repository;


	// AbstractShowService<Employer, Duty> interface -----

	@Override
	public boolean authorise(final Request<Duty> request) {
		assert request != null;

		boolean result;
		int dutyId;
		Duty duty;
		Employer employer;
		Principal principal;
		Descriptor descriptor;
		Job job;

		dutyId = request.getModel().getInteger("id");
		duty = this.repository.findDutyById(dutyId);
		descriptor = duty.getDescriptor();
		job = this.repository.findJobByDescriptorId(descriptor.getId());
		employer = job.getEmployer();
		principal = request.getPrincipal();
		result = employer.getUserAccount().getId() == principal.getAccountId();

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
