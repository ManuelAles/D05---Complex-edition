
package acme.features.administrator.challenge;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.challenges.Challenge;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface AdministratorChallengeRepository extends AbstractRepository {

	@Query("select c from Challenge c where c.id = ?1")
	Challenge findOneById(int id);

	// Filtrando por los que tengan desde la fecha de creación hasta hoy, menos de un mes de antiguedad

	@Query("select c from Challenge c where c.deadline > current_timestamp")
	Collection<Challenge> findManyAll();
}
