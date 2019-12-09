
package acme.features.authenticated.thread;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Authenticated;
import acme.framework.services.AbstractShowService;

@Service
public class AuthenticatedThreadShowService implements AbstractShowService<Authenticated, acme.entities.threads.Thread> {

	//		Internal states ------------------

	@Autowired
	private AuthenticatedThreadRepository repository;


	// AbstractShowService<Authenticated, acme.entities.threads.Thread> interface -----

	@Override
	public boolean authorise(final Request<acme.entities.threads.Thread> request) {
		assert request != null;

		return true;
	}

	@Override
	public void unbind(final Request<acme.entities.threads.Thread> request, final acme.entities.threads.Thread entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "title", "moment");

	}

	@Override
	public acme.entities.threads.Thread findOne(final Request<acme.entities.threads.Thread> request) {
		assert request != null;

		acme.entities.threads.Thread result;
		int id;

		id = request.getModel().getInteger("id");
		result = this.repository.findThreadById(id);

		return result;
	}
}
