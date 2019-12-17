
package acme.features.administrator.companyRecord;

import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.companyRecords.CompanyRecord;
import acme.entities.configurations.Configuration;
import acme.framework.components.Errors;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Administrator;
import acme.framework.services.AbstractCreateService;

@Service
public class AdministratorCompanyRecordCreateService implements AbstractCreateService<Administrator, CompanyRecord> {

	//	INTERNAL STATES ----------------------------------------

	@Autowired
	AdministratorCompanyRecordRepository repository;


	// AbstractCreateService<Administrator, Announcement> interface -----
	@Override
	public boolean authorise(final Request<CompanyRecord> request) {
		assert request != null;

		return true;
	}

	@Override
	public void bind(final Request<CompanyRecord> request, final CompanyRecord entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		request.bind(entity, errors);
	}

	@Override
	public void unbind(final Request<CompanyRecord> request, final CompanyRecord entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "name", "sector", "ceoName", "activitiesDescription", "web", "phone", "mail", "inc", "stars");

	}

	@Override
	public CompanyRecord instantiate(final Request<CompanyRecord> request) {
		CompanyRecord result;

		result = new CompanyRecord();

		return result;
	}

	@Override
	public void validate(final Request<CompanyRecord> request, final CompanyRecord entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		Boolean spam1, spam2, spam3, spam4 = null;
		spam1 = this.esSpam(entity.getName());
		spam2 = this.esSpam(entity.getSector());
		spam3 = this.esSpam(entity.getCeoName());
		spam4 = this.esSpam(entity.getActivitiesDescription());

		errors.state(request, !spam1, "name", "administrator.company-record.error.spam");
		errors.state(request, !spam2, "sector", "administrator.company-record.error.spam");
		errors.state(request, !spam3, "ceoName", "administrator.company-record.error.spam");
		errors.state(request, !spam4, "activitiesDescription", "administrator.company-record.error.spam");
	}

	@Override
	public void create(final Request<CompanyRecord> request, final CompanyRecord entity) {

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
