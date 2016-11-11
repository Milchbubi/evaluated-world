package eWorld.datatypes.containers;

import java.io.Serializable;
import java.util.ArrayList;

import eWorld.datatypes.data.EvAttributeValue;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.identifiers.AttributeIdentifier;
import eWorld.datatypes.identifiers.AttributeValueShortIdentifier;

@SuppressWarnings("serial")
public class EvAttributeValueContainer extends EvDataTypeContainer<
		AttributeIdentifier,
		AttributeValueShortIdentifier,
		String,
		EvAttributeValue<Ev, AttributeValueShortIdentifier>
	> implements Serializable  {
	
	// attributes
	
	
	// constructors
	
	/** default constructor for remote procedure call (RPC) */
	public EvAttributeValueContainer() {
	}
	
	public EvAttributeValueContainer(
			AttributeIdentifier classIdentifier, 
			ArrayList<EvAttributeValue<Ev, AttributeValueShortIdentifier>> data) {
		super(classIdentifier, data);
	}
	
	
	// methods

}
