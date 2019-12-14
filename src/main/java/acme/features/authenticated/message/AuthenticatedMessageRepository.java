
package acme.features.authenticated.message;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.messages.Message;
import acme.framework.entities.Authenticated;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface AuthenticatedMessageRepository extends AbstractRepository {

	@Query("select m from Message m where m.id = ?1")
	Message findMessageById(int id);

	@Query("select m from Message m where m.thread.id = ?1")
	Collection<Message> findAllByThread(int id);

	@Query("select a from Authenticated a where a.userAccount.id = ?1")
	Authenticated findOneAuthenticatedByUserAccountId(int id);

	@Query("select t from Thread t join t.users u where u.id = ?1")
	Collection<acme.entities.threads.Thread> findThreadsByUserId(int id);
}
