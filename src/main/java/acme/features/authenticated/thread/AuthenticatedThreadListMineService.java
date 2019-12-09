
package acme.features.authenticated.thread;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Authenticated;
import acme.framework.services.AbstractListService;

@Service
public class AuthenticatedThreadListMineService implements AbstractListService<Authenticated, acme.entities.threads.Thread> {

	// Internal interface --------------------

	@Autowired
	AuthenticatedThreadRepository repository;


	// AbstractListService<Authenticated, acme.entities.threads.Thread> interface ------

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

		request.unbind(entity, model, "title");
	}

	@Override
	public Collection<acme.entities.threads.Thread> findMany(final Request<acme.entities.threads.Thread> request) {
		assert request != null;

		Collection<acme.entities.threads.Thread> result;
		int principalId;

		principalId = request.getPrincipal().getActiveRoleId();
		result = this.repository.findThreadsByUserId(principalId);

		return result;
	}

}
