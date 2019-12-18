
package acme.features.employer.duty;

import java.util.Collection;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.configurations.Configuration;
import acme.entities.duties.Duty;
import acme.entities.jobs.Job;
import acme.entities.roles.Employer;
import acme.framework.components.Errors;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Principal;
import acme.framework.services.AbstractCreateService;

@Service
public class EmployerDutyCreateService implements AbstractCreateService<Employer, Duty> {

	//	INTERNAL STATES ----------------------------------------

	@Autowired
	EmployerDutyRepository repository;


	//	AbstractUpdateService<Employer, Duty> interface -----

	@Override
	public boolean authorise(final Request<Duty> request) {
		assert request != null;

		boolean result;
		int jobId;
		Employer employer;
		Principal principal;
		Job job;
		Double workPercentage = 0.0;

		jobId = request.getModel().getInteger("jobId");
		job = this.repository.findJobById(jobId);
		employer = job.getEmployer();
		principal = request.getPrincipal();

		Collection<Duty> collection = this.repository.findDutiesByDescriptor(job.getDescriptor().getId());

		for (Duty d : collection) {
			workPercentage += d.getPercentage();
		}

		result = employer.getUserAccount().getId() == principal.getAccountId() && !job.isFinalMode() && workPercentage < 100.0;

		return result;
	}

	@Override
	public void bind(final Request<Duty> request, final Duty entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		request.bind(entity, errors, "descriptor");
	}

	@Override
	public void unbind(final Request<Duty> request, final Duty entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "title", "percentage", "description");
	}

	@Override
	public Duty instantiate(final Request<Duty> request) {
		Duty result;

		result = new Duty();

		Job job;
		int jobId;
		jobId = request.getModel().getInteger("jobId");
		job = this.repository.findJobById(jobId);

		result.setDescriptor(job.getDescriptor());

		return result;
	}

	@Override
	public void validate(final Request<Duty> request, final Duty entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		int jobId;
		Job job;
		Double workPercentage = 0.0;

		jobId = request.getModel().getInteger("jobId");
		job = this.repository.findJobById(jobId);
		Collection<Duty> collection = this.repository.findDutiesByDescriptor(job.getDescriptor().getId());

		for (Duty d : collection) {
			workPercentage += d.getPercentage();
		}

		Boolean isHigherThan100 = !(request.getModel().getDouble("percentage") + workPercentage > 100.00);
		Boolean isNegative = !(request.getModel().getDouble("percentage") < 0.00);
		Boolean isHigher = !(request.getModel().getDouble("percentage") > 100.00);
		errors.state(request, isHigherThan100, "percentage", "employer.duty.error.higher100");
		errors.state(request, isNegative, "percentage", "employer.duty.error.negative");
		errors.state(request, isHigher, "percentage", "employer.duty.error.higher");

		Boolean spam1, spam2 = null;
		spam1 = this.esSpam(entity.getTitle());
		spam2 = this.esSpam(entity.getDescription());
		errors.state(request, !spam1, "title", "employer.duty.error.spam");
		errors.state(request, !spam2, "description", "employer.duty.error.spam");

	}

	@Override
	public void create(final Request<Duty> request, final Duty entity) {

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
