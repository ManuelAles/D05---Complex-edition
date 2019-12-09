
package acme.features.administrator.investorRecord;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.investorRecords.InvestorRecord;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface AdministratorInvestorRecordRepository extends AbstractRepository {

	@Query("select ir from InvestorRecord ir where ir.id = ?1")
	InvestorRecord findOneById(int id);

	@Query("select ir from InvestorRecord ir")
	Collection<InvestorRecord> findManyAll();
}
