
package acme.features.employer.job;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.configurations.Configuration;
import acme.entities.descriptors.Descriptor;
import acme.entities.jobs.Job;
import acme.entities.roles.Employer;
import acme.framework.components.Errors;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Principal;
import acme.framework.services.AbstractCreateService;

@Service
public class EmployerJobCreateService implements AbstractCreateService<Employer, Job> {

	//	Internal states ------------------

	@Autowired
	private EmployerJobRepository repository;


	// AbstractCreateService<Employer, Job> interface -----

	@Override
	public boolean authorise(final Request<Job> request) {
		assert request != null;

		return true;
	}

	@Override
	public void bind(final Request<Job> request, final Job entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		request.bind(entity, errors);
	}

	@Override
	public void unbind(final Request<Job> request, final Job entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "reference", "title", "deadline", "salary", "moreInfo", "descriptor", "finalMode");

	}

	@Override
	public Job instantiate(final Request<Job> request) {

		Job result;
		Principal principal;
		int userAccountId;
		Employer employer;

		principal = request.getPrincipal();
		userAccountId = principal.getActiveRoleId();
		employer = this.repository.findEmployerById(userAccountId);

		result = new Job();
		result.setEmployer(employer);

		result.setFinalMode(false);

		return result;
	}

	@Override
	public void validate(final Request<Job> request, final Job entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		Calendar calendar;
		Date minimumDeadline;

		// validar deadline
		if (!errors.hasErrors("deadline")) {
			calendar = new GregorianCalendar();
			minimumDeadline = calendar.getTime();
			Boolean isAfter = entity.getDeadline().after(minimumDeadline);
			errors.state(request, isAfter, "deadline", "employer.job.deadline.before");
		}
		// Validar moneda del salario
		if (!errors.hasErrors("salary")) {
			Boolean isEUR = entity.getSalary().getCurrency().equals("€") || entity.getSalary().getCurrency().equals("EUR");
			errors.state(request, isEUR, "salary", "employer.job.salary.eur");
		}
		// Validar salario que negativo, ni 0
		if (!errors.hasErrors("salary")) {
			Boolean higher = entity.getSalary().getAmount() > 0.00;
			errors.state(request, higher, "salary", "employer.job.salary.higher");
		}

		//Comprueba que el reference es único
		Boolean notUnique = null;
		notUnique = this.repository.findByRefence(entity.getReference()) != null;
		errors.state(request, !notUnique, "reference", "employer.job.error.reference");

		// Detectar que las cadenas no son spam

		if (!errors.hasErrors("reference")) {
			Boolean spam0;
			spam0 = this.esSpam(entity.getReference());
			errors.state(request, !spam0, "reference", "employer.job.error.spam");
		}

		if (!errors.hasErrors("title")) {
			Boolean spam1;
			spam1 = this.esSpam(entity.getTitle());
			errors.state(request, !spam1, "title", "employer.job.error.spam");
		}

		if (!errors.hasErrors("descriptor.description")) {
			Boolean spam2;
			spam2 = this.esSpam(request.getModel().getString("descriptor.description"));
			errors.state(request, !spam2, "descriptor.description", "employer.job.error.spam");
		}

	}

	@Override
	public void create(final Request<Job> request, final Job entity) {
		assert request != null;
		assert entity != null;

		Descriptor descriptor;
		descriptor = new Descriptor();
		String description;

		description = request.getModel().getString("descriptor.description");
		descriptor.setDescription(description);
		entity.setDescriptor(descriptor);

		this.repository.save(descriptor);
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
