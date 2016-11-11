package eWorld.datatypes.identifiers;

import java.io.Serializable;

@SuppressWarnings("serial")
public class AttributeValueIdentifier extends EntryClassIdentifier implements HasAttributeValueShortIdentifier, Serializable {

	// attributes
	
	private AttributeShortIdentifier attributeShortIdentifier;
	
	private AttributeValueShortIdentifier attributeValueShortIdentifier;
	
	
	// constructors
	
	/** default constructor for remote procedure call (RPC) */
	public AttributeValueIdentifier() {
	}
	
	public AttributeValueIdentifier(final long entryId, final long attributeId, String value) {
		super(entryId);
		
		assert null != value;
		
		this.attributeShortIdentifier = new AttributeShortIdentifier(attributeId);
		this.attributeValueShortIdentifier = new AttributeValueShortIdentifier(value);
	}
	
	public AttributeValueIdentifier(final EntryClassIdentifier entryIdent, final AttributeShortIdentifier attributeIdent, final AttributeValueShortIdentifier valueIdent) {
		super(entryIdent.getEntryClassId());
		
		assert null != attributeIdent;
		assert null != valueIdent;
		
		this.attributeShortIdentifier = attributeIdent;
		this.attributeValueShortIdentifier = valueIdent;
	}
	
	public AttributeValueIdentifier(AttributeIdentifier attributeIdent, AttributeValueShortIdentifier valueIdent) {
		super(attributeIdent.getEntryClassId());
		
		assert null != attributeIdent;
		assert null != valueIdent;
		
		this.attributeShortIdentifier = attributeIdent.getShortIdentifier();
		this.attributeValueShortIdentifier = valueIdent;
	}
	
	
	// methods
	
	public long getAttributeId() {
		return attributeShortIdentifier.getAttributeId();
	}
	
	public AttributeShortIdentifier getAttributeShortIdentifier() {
		return attributeShortIdentifier;
	}
	
	public AttributeIdentifier getAttributeIdentifier() {
		return new AttributeIdentifier(getEntryClassId(), getAttributeId());
	}
	
	public String getValueId() {
		return attributeValueShortIdentifier.getValue();
	}

	@Override
	public AttributeValueShortIdentifier getShortIdentifier() {
		return attributeValueShortIdentifier;
	}
	
}
