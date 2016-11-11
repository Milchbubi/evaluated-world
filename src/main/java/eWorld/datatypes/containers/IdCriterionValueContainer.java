package eWorld.datatypes.containers;

import java.io.Serializable;
import java.util.ArrayList;

import eWorld.datatypes.data.IdCriterionValue;
import eWorld.datatypes.identifiers.CriterionShortIdentifier;
import eWorld.datatypes.identifiers.EntryClassIdentifier;

@SuppressWarnings("serial")
public class IdCriterionValueContainer extends IdDataTypeContainer<
		EntryClassIdentifier,
		CriterionShortIdentifier,
		Long,
		IdCriterionValue<CriterionShortIdentifier>
	> implements Serializable  {
	
	// attributes
	
	
	// constructors

	/** default constructor for remote procedure call (RPC) */
	public IdCriterionValueContainer() {
	}
	
	public IdCriterionValueContainer(
			EntryClassIdentifier classIdentifier, 
			ArrayList<IdCriterionValue<CriterionShortIdentifier>> data) {
		super(classIdentifier, data);
	}
	
	
	// methods
	
}
