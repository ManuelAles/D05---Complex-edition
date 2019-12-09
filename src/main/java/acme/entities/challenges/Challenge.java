
package acme.entities.challenges;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import acme.framework.datatypes.Money;
import acme.framework.entities.DomainEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Challenge extends DomainEntity {

	// Serialisation identifier --------------

	private static final long	serialVersionUID	= 1L;

	// Attributes ----------------------------

	@NotBlank
	private String				title;

	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date				deadline;

	@NotBlank
	private String				description;

	@NotNull
	@Valid
	private Money				goldReward;

	@NotNull
	@Valid
	private Money				silverReward;

	@NotNull
	@Valid
	private Money				bronzeReward;

	@NotBlank
	private String				goldGoal;

	@NotBlank
	private String				silverGoal;

	@NotBlank
	private String				bronzeGoal;

	// Derived attributes ------------------------------

	// Relationships -----------------------------------
}
