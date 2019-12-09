
package acme.features.anonymous.announcement;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.announcements.Announcement;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface AnonymousAnnouncementRepository extends AbstractRepository {

	@Query("select a from Announcement a where a.id = ?1")
	Announcement findOneById(int id);

	// Filtrando por los que tengan desde la fecha de creación hasta hoy, menos de un mes de antiguedad

	@Query("select a from Announcement a where DATEDIFF(current_timestamp, a.moment) < 30")
	Collection<Announcement> findManyAll();
}
