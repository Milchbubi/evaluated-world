package eWorld.datatypes.data;

import java.io.Serializable;

import eWorld.datatypes.evs.EvAbstract;
import eWorld.datatypes.evs.EvVoid;
import eWorld.datatypes.exceptions.EvIllegalDataException;
import eWorld.datatypes.identifiers.AttributeValueIdentifier;
import eWorld.datatypes.identifiers.AttributeValueShortIdentifier;
import eWorld.datatypes.identifiers.EvIdentifier;
import eWorld.datatypes.identifiers.HasAttributeValueShortIdentifier;
import eWorld.datatypes.identifiers.UserIdentifier;

@SuppressWarnings("serial")
public class EvAttributeValue <EV extends EvAbstract, IDENT extends EvIdentifier> extends EvDataType<EV, IDENT> implements Serializable {
	
	public static final int MAX_VALUE_LENGTH = 128;
	
	/** default constructor for remote procedure call (RPC) */
	public EvAttributeValue() {
	}
	
	public EvAttributeValue(EV ev, IDENT identifier, UserIdentifier author) {
		super(ev, identifier, author);
	}
	
	public static <EV extends EvAbstract> EvAttributeValue<EV, AttributeValueShortIdentifier> shortCast(EvAttributeValue<EV, AttributeValueIdentifier> attributeValue) {
		return new EvAttributeValue<EV, AttributeValueShortIdentifier>(
				attributeValue.getEv(), 
				attributeValue.getIdentifier().getShortIdentifier(),
				attributeValue.getAuthor()
				);
	}
	
	/**
	 * checks if the attributes are valid
	 * @throws EvIllegalDataException if attributes are not valid
	 */
	public static void check(EvAttributeValue<EvVoid, AttributeValueIdentifier> attributeValue) throws EvIllegalDataException {
		if (MAX_VALUE_LENGTH < attributeValue.getIdentifier().getShortIdentifier().getValue().length()) {
			throw new EvIllegalDataException("the maximum length of argument 'value' is " + MAX_VALUE_LENGTH);
		}
	}
}
