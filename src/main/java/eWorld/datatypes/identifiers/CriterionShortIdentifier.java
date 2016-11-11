package eWorld.datatypes.identifiers;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CriterionShortIdentifier extends EvShortIdentifier<Long> implements Serializable {

	// attributes
	
	private long criterionId;
	
	
	// constructors
	
	/** default constructor for remote procedure call (RPC) */
	public CriterionShortIdentifier() {
	}
	
	public CriterionShortIdentifier(long criterionId) {
		assert 0 < criterionId;
		
		this.criterionId = criterionId;
	}
	
	
	// methods
	
	@Override
	public Long getShortId() {
		return criterionId;
	}

}
