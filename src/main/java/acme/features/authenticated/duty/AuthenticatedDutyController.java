
package acme.features.authenticated.duty;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import acme.components.CustomCommand;
import acme.entities.duties.Duty;
import acme.framework.components.BasicCommand;
import acme.framework.controllers.AbstractController;
import acme.framework.entities.Authenticated;

@Controller
@RequestMapping("/authenticated/duty/")
public class AuthenticatedDutyController extends AbstractController<Authenticated, Duty> {

	//	Internal states -------------

	@Autowired
	private AuthenticatedDutyListByJobService	listByJobService;

	@Autowired
	private AuthenticatedDutyShowService		showService;


	//	Constructors ------

	@PostConstruct
	private void initialise() {
		super.addBasicCommand(BasicCommand.SHOW, this.showService);
		super.addCustomCommand(CustomCommand.LIST_BY_JOB, BasicCommand.LIST, this.listByJobService);
	}
}
