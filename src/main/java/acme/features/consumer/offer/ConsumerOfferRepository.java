
package acme.features.consumer.offer;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.offers.Offer;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface ConsumerOfferRepository extends AbstractRepository {

	@Query("select o from Offer o where o.ticker = ?1")
	Offer findByTicker(String ticker);
}
