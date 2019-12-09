
package acme.features.consumer.offer;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.offers.Offer;
import acme.entities.roles.Consumer;
import acme.framework.components.Errors;
import acme.framework.components.HttpMethod;
import acme.framework.components.Model;
import acme.framework.services.AbstractCreateService;

@Service
public class ConsumerOfferCreateService implements AbstractCreateService<Consumer, Offer> {

	// Internal state ---------------------------------------------------------

	@Autowired
	ConsumerOfferRepository repository;


	// AbstractCreateService<Consumer, Offer> interface ---------------

	@Override
	public boolean authorise(final acme.framework.components.Request<Offer> request) {
		assert request != null;

		return true;
	}

	@Override
	public void bind(final acme.framework.components.Request<Offer> request, final Offer entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		request.bind(entity, errors, "moment");

	}

	@Override
	public void unbind(final acme.framework.components.Request<Offer> request, final Offer entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "title", "deadline", "text", "minMoney", "maxMoney", "ticker");

		if (request.isMethod(HttpMethod.GET)) {
			model.setAttribute("accept", "false");
		} else {
			request.transfer(model, "accept");
		}
	}

	@Override
	public Offer instantiate(final acme.framework.components.Request<Offer> request) {
		Offer result;

		result = new Offer();

		return result;
	}

	@Override
	public void validate(final acme.framework.components.Request<Offer> request, final Offer entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		boolean isAccepted;

		isAccepted = request.getModel().getBoolean("accept");
		errors.state(request, isAccepted, "accept", "consumer.offer.error.must-accept");

		Calendar calendar;
		Date minimumDeadline;

		//Comprueba que la fecha limite es en el futuro
		if (!errors.hasErrors("deadline")) {
			calendar = new GregorianCalendar();
			minimumDeadline = calendar.getTime();
			Boolean isAfter = entity.getDeadline().after(minimumDeadline);
			errors.state(request, isAfter, "deadline", "consumer.offer.error.before");
		}

		//Comprueba que el ticker es único
		Boolean notUnique = null;
		notUnique = this.repository.findByTicker(entity.getTicker()) != null;
		errors.state(request, !notUnique, "ticker", "consumer.offer.error.ticker");

		//Comprueba que maxMoney es mayor que minMoney y que esta en euros
		if (!errors.hasErrors("maxMoney")) {
			Boolean currency = entity.getMaxMoney().getCurrency().equals("€") || entity.getMaxMoney().getCurrency().equals("EUR");
			errors.state(request, currency, "maxMoney", "consumer.offer.error.currency");
		}

		if (!errors.hasErrors("maxMoney") && entity.getMinMoney() != null) {
			Boolean higherReward = entity.getMaxMoney().getAmount() > entity.getMinMoney().getAmount();
			errors.state(request, higherReward, "maxMoney", "consumer.offer.error.maxMoney");

		}

		//Comprueba que minMoney es mayor que minMoney y que esta en euros
		if (!errors.hasErrors("minMoney")) {
			Boolean currency = entity.getMinMoney().getCurrency().equals("€") || entity.getMinMoney().getCurrency().equals("EUR");
			errors.state(request, currency, "minMoney", "consumer.offer.error.currency");
		}

		if (!errors.hasErrors("minMoney") && entity.getMaxMoney() != null) {
			Boolean lowerReward = !(entity.getMaxMoney().getAmount() <= entity.getMinMoney().getAmount());
			errors.state(request, lowerReward, "minMoney", "consumer.offer.error.minMoney");
		}

	}

	@Override
	public void create(final acme.framework.components.Request<Offer> request, final Offer entity) {
		Date moment;

		moment = new Date(System.currentTimeMillis() - 1);
		entity.setMoment(moment);
		this.repository.save(entity);

	}

}
