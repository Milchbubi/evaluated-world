package eWorld.datatypes.identifiers;

import java.io.Serializable;

@SuppressWarnings("serial")
public class AttributeIdentifier extends EntryClassIdentifier implements HasAttributeShortIdentifier, Serializable {

	// attributes
	
	private AttributeShortIdentifier attributeShortIdentifier;
	
	
	// constructors
	
	/** default constructor for remote procedure call (RPC) */
	public AttributeIdentifier() {
	}
	
	public AttributeIdentifier(final long entryClassId, final long attributeId) {
		super(entryClassId);
		
		this.attributeShortIdentifier = new AttributeShortIdentifier(attributeId);
	}
	
	public AttributeIdentifier(final EntryClassIdentifier entryClassIdent, final AttributeShortIdentifier attributeShortIdent) {
		super(entryClassIdent.getEntryClassId());
		
		assert null != attributeShortIdent;
		
		this.attributeShortIdentifier = attributeShortIdent;
	}
	
	
	// methods
	
	public long getAttributeId() {
		return attributeShortIdentifier.getAttributeId();
	}

	@Override
	public AttributeShortIdentifier getShortIdentifier() {
		return attributeShortIdentifier;
	}
	
}
