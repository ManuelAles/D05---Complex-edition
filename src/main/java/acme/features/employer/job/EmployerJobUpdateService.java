
package acme.features.employer.job;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.configurations.Configuration;
import acme.entities.descriptors.Descriptor;
import acme.entities.duties.Duty;
import acme.entities.jobs.Job;
import acme.entities.roles.Employer;
import acme.framework.components.Errors;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Principal;
import acme.framework.services.AbstractUpdateService;

@Service
public class EmployerJobUpdateService implements AbstractUpdateService<Employer, Job> {

	//	Internal states ------------------

	@Autowired
	private EmployerJobRepository repository;


	// AbstractUpdateService<Employer, Job> interface -----

	@Override
	public boolean authorise(final Request<Job> request) {
		assert request != null;

		boolean result;
		int jobId;
		Job job;
		Employer employer;
		Principal principal;

		jobId = request.getModel().getInteger("id");
		job = this.repository.findJobById(jobId);
		employer = job.getEmployer();
		principal = request.getPrincipal();

		boolean finalMode;
		finalMode = job.isFinalMode();

		result = employer.getUserAccount().getId() == principal.getAccountId() && !finalMode;

		return result;
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

		request.unbind(entity, model, "reference", "title", "deadline", "salary", "moreInfo", "descriptor.description", "finalMode");

	}

	@Override
	public Job findOne(final Request<Job> request) {
		assert request != null;

		Job result;
		int jobId;

		jobId = request.getModel().getInteger("id");
		result = this.repository.findJobById(jobId);

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

		// Un job debe tener un descriptor y el porcentaje de trabajo sumar 100 si va a ser pasado a modo público
		if (!errors.hasErrors("finalMode")) {
			if (request.getModel().getBoolean("finalMode") == true) {
				Boolean notWorkload, notDescriptor;
				Double workload = 0.0;
				Collection<Duty> duties = this.repository.findDutiesByDescriptor(entity.getDescriptor().getId());
				for (Duty d : duties) {
					workload += d.getPercentage();
				}
				notWorkload = workload.equals(100.00);
				notDescriptor = entity.getDescriptor() != null;
				errors.state(request, notDescriptor, "finalMode", "employer.job.error.descriptor");
				errors.state(request, notWorkload, "finalMode", "employer.job.error.workload");
			}
		}

		// Detectar que las cadenas no son spam

		if (!errors.hasErrors("title")) {
			Boolean spam1;
			spam1 = this.esSpam(entity.getTitle());
			errors.state(request, !spam1, "title", "employer.job.error.spam");
		}

		if (!errors.hasErrors("descriptor.description")) {
			Boolean spam2;
			spam2 = this.esSpam(entity.getDescriptor().getDescription());
			errors.state(request, !spam2, "descriptor.description", "employer.job.error.spam");
		}

	}

	@Override
	public void update(final Request<Job> request, final Job entity) {
		assert request != null;
		assert entity != null;

		Descriptor descriptor;
		descriptor = entity.getDescriptor();
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
		int i = 0;

		//Con el repositorio llamamos a la
		Configuration c = this.repository.selectConfiguration();
		String listaSpam = c.getSpamWords();

		//Dividimos las palabras spam por coma y las palabras de la cadena con split
		String[] spam = listaSpam.split(",");
		String[] palabras = cadena.split(" ");

		//Recorremos cada palabra de la cadena
		while (i < palabras.length) {
			//Y cada palabra de la lista de spam
			for (String s : spam) {
				//si detectamos que una palabra de la cadena es igual a una de la lista de spam
				if (palabras[i].equals(s)) {
					//Sumamos uno
					palabrasSpam++;
				}
			}
			i++;
		}

		//Como algunos elementos de la lista de spam tienen mas de una palabra hay que detectar ese spam de otra forma

		//Entonces recorremos la lista de spam
		for (String s2 : spam) {
			//Contamos el número de palabras que tiene ese elemento de spam
			StringTokenizer stringTokenizer = new StringTokenizer(s2);
			Integer ss = stringTokenizer.countTokens();
			//Si la cadena contiene ese elemento añadimos +2 al contador de spam
			if (cadena.contains(s2) && ss == 2) {
				palabrasSpam += 2;
			} else if (cadena.contains(s2) && ss == 3) {
				palabrasSpam += 3;
			} else if (cadena.contains(s2) && ss == 4) {
				palabrasSpam += 4;
			}
			//El único inconveniente de esta forma, es que no lo cuenta si esta duplicado
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
