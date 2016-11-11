package eWorld.datatypes.data;

import java.io.Serializable;

import eWorld.datatypes.elementars.WoString;
import eWorld.datatypes.evs.EvAbstract;
import eWorld.datatypes.identifiers.AttributeIdentifier;
import eWorld.datatypes.identifiers.AttributeShortIdentifier;
import eWorld.datatypes.identifiers.EntryClassIdentifier;
import eWorld.datatypes.identifiers.EvIdentifier;
import eWorld.datatypes.identifiers.UserIdentifier;

@SuppressWarnings("serial")
public class EvAttribute <EV extends EvAbstract, IDENT extends EvIdentifier> extends EvObject<EV, IDENT> implements Serializable {

	public static final int MAX_NAME_LENGTH = 32;
	public static final int MAX_DESCRIPTION_LENGTH = 300;
	
	/** default constructor for remote procedure call (RPC) */
	public EvAttribute() {
	}
	
	public EvAttribute(EV ev, IDENT identifier, WoString name, WoString description, UserIdentifier author) {
		super(ev, identifier, name, description, author);
	}
	
	public static <EV extends EvAbstract> EvAttribute<EV, AttributeShortIdentifier> shortCast(EvAttribute<EV, AttributeIdentifier> attribute) {
		return new EvAttribute<EV, AttributeShortIdentifier>(
				attribute.getEv(),
				attribute.getIdentifier().getShortIdentifier(),
				attribute.getName(),
				attribute.getDescription(),
				attribute.getAuthor()
				);
	}
	
	/**
	 * TODO is there no common way to do this?
	 * @param attribute
	 * @return
	 */
	public static <EV extends EvAbstract> EvAttribute<EV, EntryClassIdentifier> containerCast(EvAttribute<EV, AttributeIdentifier> attribute) {
		return new EvAttribute<EV, EntryClassIdentifier>(
				attribute.getEv(),
				attribute.getIdentifier(),
				attribute.getName(),
				attribute.getDescription(),
				attribute.getAuthor()
				);
	}

	@Override
	public int getMaxNameLength() {
		return MAX_NAME_LENGTH;
	}

	@Override
	public int getMaxDescriptionLength() {
		return MAX_DESCRIPTION_LENGTH;
	}
}
