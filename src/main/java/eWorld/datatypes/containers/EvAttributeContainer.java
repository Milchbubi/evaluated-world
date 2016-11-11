package eWorld.datatypes.containers;

import java.io.Serializable;
import java.util.ArrayList;

import eWorld.datatypes.data.EvAttribute;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.identifiers.AttributeShortIdentifier;
import eWorld.datatypes.identifiers.EntryClassIdentifier;

@SuppressWarnings("serial")
public class EvAttributeContainer extends EvObjectContainer<
		EntryClassIdentifier,
		AttributeShortIdentifier,
		Long,
		EvAttribute<Ev, AttributeShortIdentifier>
	>
	implements Serializable  {
	
	// attributes
	
	
	// constructors

	/** default constructor for remote procedure call (RPC) */
	public EvAttributeContainer() {
	}
	
	public EvAttributeContainer(
			EntryClassIdentifier classIdentifier, 
			ArrayList<EvAttribute<Ev, AttributeShortIdentifier>> data) {
		super(classIdentifier, data);
	}
	
	
	// methods
	
}
