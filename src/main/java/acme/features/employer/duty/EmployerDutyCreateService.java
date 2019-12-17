
package acme.features.employer.duty;

import java.util.Collection;
import java.util.StringTokenizer;

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
