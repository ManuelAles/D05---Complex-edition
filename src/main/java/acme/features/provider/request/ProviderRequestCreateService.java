
package acme.features.provider.request;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.requests.Request;
import acme.entities.roles.Provider;
import acme.framework.components.Errors;
import acme.framework.components.HttpMethod;
import acme.framework.components.Model;
import acme.framework.services.AbstractCreateService;

@Service
public class ProviderRequestCreateService implements AbstractCreateService<Provider, Request> {

	// Internal state ---------------------------------------------------------

	@Autowired
	ProviderRequestRepository repository;


	// AbstractCreateService<Provider, Request> interface ---------------

	@Override
	public boolean authorise(final acme.framework.components.Request<Request> request) {
		assert request != null;

		return true;
	}

	@Override
	public void bind(final acme.framework.components.Request<Request> request, final Request entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		request.bind(entity, errors, "moment");

	}

	@Override
	public void unbind(final acme.framework.components.Request<Request> request, final Request entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "title", "deadline", "text", "reward", "ticker");

		if (request.isMethod(HttpMethod.GET)) {
			model.setAttribute("accept", "false");
		} else {
			request.transfer(model, "accept");
		}
	}

	@Override
	public Request instantiate(final acme.framework.components.Request<Request> request) {
		Request result;

		result = new Request();

		return result;
	}

	@Override
	public void validate(final acme.framework.components.Request<Request> request, final Request entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		Boolean isAccepted;

		isAccepted = request.getModel().getBoolean("accept");
		errors.state(request, isAccepted, "accept", "provider.request.error.must-accept");

		Calendar calendar;
		Date minimumDeadline;

		//Comprueba que la fecha limite es en el futuro
		if (!errors.hasErrors("deadline")) {
			calendar = new GregorianCalendar();
			minimumDeadline = calendar.getTime();
			Boolean isAfter = entity.getDeadline().after(minimumDeadline);
			errors.state(request, isAfter, "deadline", "provider.request.error.before");
		}

		//Comprueba que el ticker es único
		Boolean notUnique = null;
		notUnique = this.repository.findByTicker(entity.getTicker()) != null;
		errors.state(request, !notUnique, "ticker", "provider.request.error.ticker");

		//Comprueba que maxMoney es mayor que minMoney y que esta en euros
		if (!errors.hasErrors("reward")) {
			Boolean currency = entity.getReward().getCurrency().equals("€") || entity.getReward().getCurrency().equals("EUR");
			errors.state(request, currency, "reward", "provider.request.error.currency");
		}

	}

	@Override
	public void create(final acme.framework.components.Request<Request> request, final Request entity) {
		Date moment;

		moment = new Date(System.currentTimeMillis() - 1);
		entity.setMoment(moment);
		this.repository.save(entity);

	}

}
