package eWorld.datatypes.identifiers;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CriterionIdentifier extends EntryClassIdentifier implements Serializable {

	// attributes
	
	private CriterionShortIdentifier criterionShortIdentifier;
	
	
	// constructors
	
	/** default constructor for remote procedure call (RPC) */
	public CriterionIdentifier() {
	}
	
	public CriterionIdentifier(final long entryClassId, final long criterionId) {
		super(entryClassId);
		
		this.criterionShortIdentifier = new CriterionShortIdentifier(criterionId);
	}
	
	public CriterionIdentifier(final EntryClassIdentifier entryClassIdent, final CriterionShortIdentifier criterionShortIdent) {
		super(entryClassIdent.getEntryClassId());
		
		assert null != criterionShortIdent;
		
		this.criterionShortIdentifier = criterionShortIdent;
	}
	
	
	// methods
	
	public long getCriterionId() {
		return criterionShortIdentifier.getShortId();
	}

//	@Override
	public CriterionShortIdentifier getShortIdentifier() {
		return criterionShortIdentifier;
	}
}
