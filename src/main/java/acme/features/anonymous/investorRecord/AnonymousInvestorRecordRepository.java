
package acme.features.anonymous.investorRecord;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.investorRecords.InvestorRecord;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface AnonymousInvestorRecordRepository extends AbstractRepository {

	@Query("select ir from InvestorRecord ir")
	Collection<InvestorRecord> findManyAll();

	@Query("select ir from InvestorRecord ir where ir.id = ?1")
	InvestorRecord findOneById(int id);

}
