package eWorld.datatypes.containers;

import java.io.Serializable;
import java.util.ArrayList;

import eWorld.datatypes.data.IdAttributeTopValue;
import eWorld.datatypes.identifiers.AttributeShortIdentifier;
import eWorld.datatypes.identifiers.EntryClassIdentifier;

@SuppressWarnings("serial")
public class IdAttributeTopValueContainer extends IdDataTypeContainer<
		EntryClassIdentifier,
		AttributeShortIdentifier,
		Long,
		IdAttributeTopValue<AttributeShortIdentifier>
	> implements Serializable  {
	
	// attributes
	
	
	// constructors

	/** default constructor for remote procedure call (RPC) */
	public IdAttributeTopValueContainer() {
	}
	
	public IdAttributeTopValueContainer(
			EntryClassIdentifier classIdentifier, 
			ArrayList<IdAttributeTopValue<AttributeShortIdentifier>> data) {
		super(classIdentifier, data);
	}
	
	
	// methods
	
}
