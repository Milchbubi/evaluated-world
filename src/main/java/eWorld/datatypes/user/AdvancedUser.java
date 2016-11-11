package eWorld.datatypes.user;

import java.io.Serializable;

import eWorld.datatypes.identifiers.UserIdentifier;

@SuppressWarnings("serial")
public class AdvancedUser extends User<UserIdentifier> implements Serializable {

	// attributes

	private int votes;
	
	
	// constructors

	/** default constructor for remote procedure call (RPC) */
	public AdvancedUser() {
	}
	
	public AdvancedUser(UserIdentifier identifier, String pseudonym, int votes) {
		super(identifier, pseudonym);
		
		this.votes = votes;
	}
	
	
	// methods

	public int getVotes() {
		return votes;
	}
	
	public User<UserIdentifier> constructUser() {
		return new User<UserIdentifier>(getIdentifier(), getPseudonym());
	}
}
