package eWorld.datatypes.data;

import java.io.Serializable;

import eWorld.datatypes.elementars.IntegerVote;
import eWorld.datatypes.identifiers.CriterionIdentifier;
import eWorld.datatypes.identifiers.CriterionShortIdentifier;
import eWorld.datatypes.identifiers.EvIdentifier;

@SuppressWarnings("serial")
public class IdCriterionValue <IDENT extends EvIdentifier> extends IdDataType<IDENT> implements Serializable {

	private float average;
	
	private long votes;
	
	/** the vote for the criterion of the specified user or null if user is not specified (| user has not yet voted) */
	private IntegerVote individualVote;
	
	
	/** default constructor for remote procedure call (RPC) */
	public IdCriterionValue() {
	}
	
	public IdCriterionValue(IDENT identifier, float average, long votes) {
		super(identifier);
		
		this.average = average;
		this.votes = votes;
	}
	
	/**
	 * @return the average of the votes
	 */
	public float getAverage() {
		return average;
	}
	
	/**
	 * @return the number of users|votes that where involved in the voting
	 */
	public long getVotes() {
		return votes;
	}
	
	/**
	 * 
	 * @return the vote for the criterion of the specified user or null if user is not specified (| user has not yet voted)
	 */
	public IntegerVote getIndividualVote() {
		return individualVote;
	}
	
	/**
	 * 
	 * @param individualVote the vote for the criterion of the specified user or null if user is not specified (| user has not yet voted)
	 */
	public void setIndividualVote(IntegerVote individualVote) {
		this.individualVote = individualVote;
	}
	
	public static IdCriterionValue<CriterionShortIdentifier> shortCast(IdCriterionValue<CriterionIdentifier> criterionValue) {
		return new IdCriterionValue<CriterionShortIdentifier>(
				criterionValue.getIdentifier().getShortIdentifier(),
				criterionValue.getAverage(),
				criterionValue.getVotes()
				);
	}
}
