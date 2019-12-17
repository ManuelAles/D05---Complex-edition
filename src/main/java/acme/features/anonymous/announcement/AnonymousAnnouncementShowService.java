
package acme.features.anonymous.announcement;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.announcements.Announcement;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Anonymous;
import acme.framework.services.AbstractShowService;

@Service
public class AnonymousAnnouncementShowService implements AbstractShowService<Anonymous, Announcement> {

	//		Internal states ------------------

	@Autowired
	private AnonymousAnnouncementRepository repository;


	// AbstractShowService<Administrator, Announcement> interface -----

	@Override
	public boolean authorise(final Request<Announcement> request) {
		assert request != null;

		Boolean result;

		Announcement a;
		int id;
		id = request.getModel().getInteger("id");
		a = this.repository.findOneById(id);

		Calendar calendar = new GregorianCalendar();
		long diff = (calendar.getTime().getTime() - a.getMoment().getTime()) / 1000 / 60 / 60 / 24;

		result = diff < 30;

		return result;
	}

	@Override
	public void unbind(final Request<Announcement> request, final Announcement entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "title", "moment", "moreInfo", "text");

	}

	@Override
	public Announcement findOne(final Request<Announcement> request) {
		assert request != null;

		Announcement result;
		int id;

		id = request.getModel().getInteger("id");
		result = this.repository.findOneById(id);

		return result;
	}
}
