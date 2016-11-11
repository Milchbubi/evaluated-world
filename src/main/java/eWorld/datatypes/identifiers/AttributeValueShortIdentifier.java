package eWorld.datatypes.identifiers;

import java.io.Serializable;

@SuppressWarnings("serial")
public class AttributeValueShortIdentifier extends EvShortIdentifier<String> implements HasAttributeValueShortIdentifier, Serializable {

	// attributes
	
	private String value;
	
	
	// constructors
	
	/** default constructor for remote procedure call (RPC) */
	public AttributeValueShortIdentifier() {
		
	}
	
	public AttributeValueShortIdentifier(String value) {
		assert null != value;
		
		this.value = value;
	}
	
	
	// methods

	public String getValue() {
		return value;
	}
	
	@Override
	public String getShortId() {
		return value;
	}

	@Override
	public AttributeValueShortIdentifier getShortIdentifier() {
		return this;
	}

}
