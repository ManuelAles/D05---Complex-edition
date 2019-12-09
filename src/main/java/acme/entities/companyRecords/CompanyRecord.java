
package acme.entities.companyRecords;

import javax.persistence.Entity;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

import acme.framework.entities.DomainEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CompanyRecord extends DomainEntity {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@NotBlank
	private String				name;

	@NotBlank
	private String				sector;

	@NotBlank
	private String				ceoName;

	@NotBlank
	private String				activitiesDescription;

	@URL
	@NotBlank
	private String				web;

	@NotBlank
	@Pattern(regexp = "^([+]([1-9][0-9]{0,3}\\s))?([(]{0,1}[0-9]{1,4}[)]){0,1}?[0-9]{6,9}$")
	/*
	 * +999 (9999)999999
	 * +999 optional, rang(1,999)
	 * (9999) optional, rang(0,9999)
	 * 999999 min 6 digits, max 9 digits
	 */
	private String				phone;

	@Email
	@NotBlank
	private String				mail;

	@NotNull
	private boolean				inc;

	@Range(min = 0, max = 5)
	private Integer				stars;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
