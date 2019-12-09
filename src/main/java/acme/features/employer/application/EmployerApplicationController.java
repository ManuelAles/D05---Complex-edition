
package acme.features.employer.application;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import acme.components.CustomCommand;
import acme.entities.applications.Application;
import acme.entities.roles.Employer;
import acme.framework.components.BasicCommand;
import acme.framework.controllers.AbstractController;

@Controller
@RequestMapping("/employer/application")
public class EmployerApplicationController extends AbstractController<Employer, Application> {

	//	Internal states -------------

	@Autowired
	private EmployerApplicationListByJobService	listByJobService;

	@Autowired
	private EmployerApplicationShowService		showService;


	//	Constructors ------

	@PostConstruct
	private void initialise() {
		super.addBasicCommand(BasicCommand.SHOW, this.showService);
		super.addCustomCommand(CustomCommand.LIST_BY_JOB, BasicCommand.LIST, this.listByJobService);
	}
}
