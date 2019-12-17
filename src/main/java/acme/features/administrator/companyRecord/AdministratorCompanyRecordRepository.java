
package acme.features.administrator.companyRecord;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.companyRecords.CompanyRecord;
import acme.entities.configurations.Configuration;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface AdministratorCompanyRecordRepository extends AbstractRepository {

	@Query("select cr from CompanyRecord cr")
	Collection<CompanyRecord> findManyCompanyRecord();

	@Query("select cr from CompanyRecord cr where cr.id = ?1")
	CompanyRecord findOneCompanyRecordById(int id);

	@Query("select c from Configuration c")
	Configuration selectConfiguration();

}
