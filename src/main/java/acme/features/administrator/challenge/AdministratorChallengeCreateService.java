
package acme.features.administrator.challenge;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.challenges.Challenge;
import acme.framework.components.Errors;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Administrator;
import acme.framework.services.AbstractCreateService;

@Service
public class AdministratorChallengeCreateService implements AbstractCreateService<Administrator, Challenge> {

	//	INTERNAL STATES ----------------------------------------

	@Autowired
	AdministratorChallengeRepository repository;


	//	AbstractUpdateService<Administrator, Announcement> interface -----

	@Override
	public boolean authorise(final Request<Challenge> request) {
		assert request != null;

		return true;
	}

	@Override
	public void bind(final Request<Challenge> request, final Challenge entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		request.bind(entity, errors);
	}

	@Override
	public void unbind(final Request<Challenge> request, final Challenge entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "title", "deadline", "description", "goldReward", "silverReward", "bronzeReward", "goldGoal", "silverGoal", "bronzeGoal");
	}

	@Override
	public Challenge instantiate(final Request<Challenge> request) {
		Challenge result;

		result = new Challenge();

		return result;
	}

	//	TODO: El atributo deadline debe ser futuro, no puede ser un día anterior al actual!!

	@Override
	public void validate(final Request<Challenge> request, final Challenge entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		//		Validación del deadline--------------

		Calendar calendar;
		Date minimumDeadline;

		if (!errors.hasErrors("deadline")) {
			calendar = new GregorianCalendar();
			minimumDeadline = calendar.getTime();
			Boolean isAfter = entity.getDeadline().after(minimumDeadline);
			errors.state(request, isAfter, "deadline", "administrator.challenge.deadline.before");
		}

		//		Validación de la unidad monetaria

		if (!errors.hasErrors("goldReward")) {
			Boolean isEUR = entity.getGoldReward().getCurrency().equals("€") || entity.getGoldReward().getCurrency().equals("EUR");
			errors.state(request, isEUR, "goldReward", "administrator.challenge.goldReward.eur");
		}

		if (!errors.hasErrors("silverReward")) {
			Boolean isEUR = entity.getSilverReward().getCurrency().equals("€") || entity.getGoldReward().getCurrency().equals("EUR");
			errors.state(request, isEUR, "silverReward", "administrator.challenge.silverReward.eur");
		}

		if (!errors.hasErrors("bronzeReward")) {
			Boolean isEUR = entity.getBronzeReward().getCurrency().equals("€") || entity.getGoldReward().getCurrency().equals("EUR");
			errors.state(request, isEUR, "bronzeReward", "administrator.challenge.bronzeReward.eur");
		}

		//		validación de recompensas (oro > plata > bronce)

		if (!errors.hasErrors("goldReward") && entity.getBronzeReward() != null && entity.getSilverReward() != null && entity.getGoldReward() != null) {
			Boolean balance = entity.getGoldReward().getAmount() > entity.getSilverReward().getAmount() && entity.getGoldReward().getAmount() > entity.getBronzeReward().getAmount();
			errors.state(request, balance, "goldReward", "administrator.challenge.rewards");
		}

		if (!errors.hasErrors("silverReward") && entity.getBronzeReward() != null && entity.getSilverReward() != null && entity.getGoldReward() != null) {
			Boolean balance = entity.getSilverReward().getAmount() < entity.getGoldReward().getAmount() && entity.getSilverReward().getAmount() > entity.getBronzeReward().getAmount();
			errors.state(request, balance, "silverReward", "administrator.challenge.rewards");
		}

		if (!errors.hasErrors("bronzeReward") && entity.getBronzeReward() != null && entity.getSilverReward() != null && entity.getGoldReward() != null) {
			Boolean balance = entity.getBronzeReward().getAmount() < entity.getGoldReward().getAmount() && entity.getBronzeReward().getAmount() < entity.getSilverReward().getAmount();
			errors.state(request, balance, "bronzeReward", "administrator.challenge.rewards");
		}
	}

	@Override
	public void create(final Request<Challenge> request, final Challenge entity) {

		this.repository.save(entity);
	}

}
