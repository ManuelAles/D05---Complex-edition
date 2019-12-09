
package acme.features.authenticated.duty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.duties.Duty;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Authenticated;
import acme.framework.services.AbstractShowService;

@Service
public class AuthenticatedDutyShowService implements AbstractShowService<Authenticated, Duty> {

	//		Internal states ------------------

	@Autowired
	private AuthenticatedDutyRepository repository;


	// AbstractShowService<Authenticated, Duty> interface -----

	@Override
	public boolean authorise(final Request<Duty> request) {
		assert request != null;

		return true;
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
