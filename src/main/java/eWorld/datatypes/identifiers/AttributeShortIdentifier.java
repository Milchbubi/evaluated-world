package eWorld.datatypes.identifiers;

import java.io.Serializable;

@SuppressWarnings("serial")
public class AttributeShortIdentifier extends EvShortIdentifier<Long> implements HasAttributeShortIdentifier, Serializable {

	// attributes
	
	private long attributeId;
	
	
	// constructors
	
	/** default constructor for remote procedure call (RPC) */
	public AttributeShortIdentifier() {
	}
	
	public AttributeShortIdentifier(long attributeId) {
		assert 0 < attributeId;
		
		this.attributeId = attributeId;
	}
	
	
	// methods

	public long getAttributeId() {
		return attributeId;
	}
	
	@Override
	public Long getShortId() {
		return attributeId;
	}

	@Override
	public AttributeShortIdentifier getShortIdentifier() {
		return this;
	}

}
