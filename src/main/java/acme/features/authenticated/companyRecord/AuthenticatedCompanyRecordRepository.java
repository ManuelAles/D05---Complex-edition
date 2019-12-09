
package acme.features.authenticated.companyRecord;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.companyRecords.CompanyRecord;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface AuthenticatedCompanyRecordRepository extends AbstractRepository {

	@Query("select cr from CompanyRecord cr")
	Collection<CompanyRecord> findManyAll();

	@Query("select cr from CompanyRecord cr where cr.id = ?1")
	CompanyRecord findOneById(int id);

}
