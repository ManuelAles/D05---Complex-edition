
package acme.entities.configurations;

import javax.persistence.Entity;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import acme.framework.entities.DomainEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Configuration extends DomainEntity {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@NotBlank
	private String				spamWordsEN;

	@NotBlank
	private String				spamWordsES;

	@NotNull
	@Min(0)
	@Max(100)
	private Double				threshold;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
