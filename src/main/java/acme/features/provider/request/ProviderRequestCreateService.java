
package acme.features.provider.request;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.configurations.Configuration;
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

		Boolean spam1, spam2 = null;
		spam1 = this.esSpam(entity.getTitle());
		spam2 = this.esSpam(entity.getText());
		errors.state(request, !spam1, "title", "provider.request.error.spam");
		errors.state(request, !spam2, "text", "provider.request.error.spam");

	}

	@Override
	public void create(final acme.framework.components.Request<Request> request, final Request entity) {
		Date moment;

		moment = new Date(System.currentTimeMillis() - 1);
		entity.setMoment(moment);
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
