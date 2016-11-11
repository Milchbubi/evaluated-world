package eWorld.datatypes.data;

import java.io.Serializable;

import eWorld.datatypes.identifiers.AttributeIdentifier;
import eWorld.datatypes.identifiers.AttributeShortIdentifier;
import eWorld.datatypes.identifiers.EvIdentifier;

@SuppressWarnings("serial")
public class IdAttributeTopValue <IDENT extends EvIdentifier> extends IdDataType<IDENT> implements Serializable {

	private String value;
	
	/** default constructor for remote procedure call (RPC) */
	public IdAttributeTopValue() {
	}
	
	public IdAttributeTopValue(IDENT identifier, String value) {
		super(identifier);
		
		assert null != value;
		
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	public static IdAttributeTopValue<AttributeShortIdentifier> shortCast(IdAttributeTopValue<AttributeIdentifier> attributeTopValue) {
		return new IdAttributeTopValue<AttributeShortIdentifier>(
				attributeTopValue.getIdentifier().getShortIdentifier(),
				attributeTopValue.getValue()
				);
	}
}
