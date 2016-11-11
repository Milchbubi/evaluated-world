package eWorld.datatypes.data;

import java.io.Serializable;

import eWorld.datatypes.evs.EvAbstract;
import eWorld.datatypes.identifiers.EvIdentifier;
import eWorld.datatypes.identifiers.UserIdentifier;

@SuppressWarnings("serial")
public abstract class EvDataType <EV extends EvAbstract, IDENT extends EvIdentifier> extends IdDataType<IDENT> implements Serializable {

	// attributes

	private EV ev;
	
	/** specifies the user who suggested the entry or null if not specified */
	private UserIdentifier author = null;
	
	// constructors

	/** default constructor for remote procedure call (RPC) */
	public EvDataType() {
	}
	
	/**
	 * 
	 * @param ev
	 * @param identifier
	 * @param author the id of the user who suggested the item, can be null
	 */
	public EvDataType(EV ev, IDENT identifier, UserIdentifier author) {
		super(identifier);
		
		assert null != ev;
		
		this.ev = ev;
		this.author = author;
	}
	
	
	// methods

	public EV getEv() {
		return ev;
	}
	
	public UserIdentifier getAuthor() {
		return author;
	}
	
	public void setAuthor(UserIdentifier author) {
		this.author = author;
	}
	
}
