
package acme.features.worker.application;

import java.util.Date;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.applications.Application;
import acme.entities.configurations.Configuration;
import acme.entities.jobs.Job;
import acme.entities.roles.Worker;
import acme.framework.components.Errors;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Principal;
import acme.framework.services.AbstractCreateService;

@Service
public class WorkerApplicationCreateService implements AbstractCreateService<Worker, Application> {

	// Internal state ---------------------------------------------------------

	@Autowired
	WorkerApplicationRepository repository;


	// AbstractCreateService<Worker, Application> interface ---------------

	@Override
	public boolean authorise(final Request<Application> request) {
		assert request != null;
		Boolean result = true;

		Job job;
		int jobId;
		jobId = request.getModel().getInteger("jobId");
		job = this.repository.findJobById(jobId);

		Principal principal;
		principal = request.getPrincipal();
		int workerId = this.repository.findWorkerByUserAccountId(principal.getAccountId()).getId();

		Date d;
		d = new Date(System.currentTimeMillis() - 1);

		result = this.repository.findByJobAndWorker(workerId, jobId) == null && job.isFinalMode() == true && job.getDeadline().after(d);

		return result;
	}

	@Override
	public void bind(final Request<Application> request, final Application entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		request.bind(entity, errors, "moment", "worker");
	}

	@Override
	public void unbind(final Request<Application> request, final Application entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "reference", "status", "statement", "skills", "qualifications");
	}

	@Override
	public Application instantiate(final Request<Application> request) {
		assert request != null;

		Application result;
		result = new Application();

		Job job;
		int jobId;
		jobId = request.getModel().getInteger("jobId");
		job = this.repository.findJobById(jobId);
		result.setJob(job);

		Worker worker;
		int workerId;
		Principal principal;
		principal = request.getPrincipal();
		workerId = principal.getAccountId();
		worker = this.repository.findWorkerByUserAccountId(workerId);
		result.setWorker(worker);

		result.setQualifications(worker.getQualificationRecord());
		result.setSkills(worker.getSkillRecord());

		String status;
		status = "PENDING";
		result.setStatus(status);

		return result;
	}

	@Override
	public void validate(final Request<Application> request, final Application entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		//Comprueba que el reference es único
		Boolean notUnique = null;
		notUnique = this.repository.findByRefence(entity.getReference()) != null;

		//Comprueba que las cadenas no tienen spam
		Boolean spamR, spamT = null;
		spamR = this.esSpam(entity.getReference());
		spamT = this.esSpam(entity.getStatement());

		errors.state(request, !notUnique, "reference", "worker.application.error.reference");
		errors.state(request, !spamR, "reference", "worker.application.error.spam");
		errors.state(request, !spamT, "statement", "worker.application.error.spam");

	}

	@Override
	public void create(final Request<Application> request, final Application entity) {
		Date moment;

		moment = new Date(System.currentTimeMillis() - 1);
		entity.setMoment(moment);

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
