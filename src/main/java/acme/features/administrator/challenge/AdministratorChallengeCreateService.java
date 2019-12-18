
package acme.features.administrator.challenge;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.challenges.Challenge;
import acme.entities.configurations.Configuration;
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

		//Detectar spam

		Boolean spam1, spam2, spam3, spam4, spam5 = null;
		spam1 = this.esSpam(entity.getTitle());
		spam2 = this.esSpam(entity.getDescription());
		spam3 = this.esSpam(entity.getBronzeGoal());
		spam4 = this.esSpam(entity.getSilverGoal());
		spam5 = this.esSpam(entity.getGoldGoal());

		errors.state(request, !spam1, "title", "administrator.challenge.error.spam");
		errors.state(request, !spam2, "description", "administrator.challenge.error.spam");
		errors.state(request, !spam3, "bronzeGoal", "administrator.challenge.error.spam");
		errors.state(request, !spam4, "silverGoal", "administrator.challenge.error.spam");
		errors.state(request, !spam5, "goldGoal", "administrator.challenge.error.spam");
	}

	@Override
	public void create(final Request<Challenge> request, final Challenge entity) {

		this.repository.save(entity);
	}

	public Boolean esSpam(final String cadena) {
		//Inicializamos la variable de resultado, el contador de palabras y el contador de spam
		Boolean esSpam = false;
		Integer palabrasSpam = 0;

		//Con el repositorio llamamos a la
		Configuration c = this.repository.selectConfiguration();
		String listaSpam = c.getSpamWords();

		//Dividimos las palabras spam por coma
		String[] spam = listaSpam.split(",");

		//Recorremos la lista de spam
		for (String s : spam) {
			//Metemos en una variable Pattern cada palabra de la lista para que se compile
			Pattern p = Pattern.compile(s);
			//Pasamos la cadena de texto en una variable Matcher con el Pattern anterior
			Matcher m = p.matcher(cadena);
			//En el Matcher buscamos si se encuentra el Pattern, es decir el termino de spam actual
			while (m.find()) {
				//Como algunos terminos de spam tienen mas de una palabra con StringTokenizer añadimos el numero de palabras del termino de spam
				StringTokenizer stringTokenizer = new StringTokenizer(s);
				Integer ss = stringTokenizer.countTokens();
				palabrasSpam += ss;
			}
		}

		//Contamos el número total de palabras de la cadena
		StringTokenizer stringTokenizer = new StringTokenizer(cadena);
		Integer palabrasTotales = stringTokenizer.countTokens();

		//Dividimos el número anterior entre el número de palabras spam
		Double porcentajeSpam = (double) palabrasSpam / palabrasTotales;

		//Si el porcentaje de palabras spam que aparece en a cadena es mayor que el threehold
		if (porcentajeSpam >= c.getThreshold()) {
			//Entonces la cadena se considera SPAM
			esSpam = true;
		}

		return esSpam;
	}

}
