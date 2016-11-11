package eWorld.datatypes.data;

import java.io.Serializable;

import eWorld.datatypes.elementars.WoString;
import eWorld.datatypes.evs.EvAbstract;
import eWorld.datatypes.identifiers.AttributeIdentifier;
import eWorld.datatypes.identifiers.CriterionIdentifier;
import eWorld.datatypes.identifiers.CriterionShortIdentifier;
import eWorld.datatypes.identifiers.EntryClassIdentifier;
import eWorld.datatypes.identifiers.EvIdentifier;
import eWorld.datatypes.identifiers.UserIdentifier;

@SuppressWarnings("serial")
public class EvCriterion <EV extends EvAbstract, IDENT extends EvIdentifier> extends EvObject<EV, IDENT> implements Serializable {

	public static final int MAX_NAME_LENGTH = 32;
	public static final int MAX_DESCRIPTION_LENGTH = 300;
	
	/** the worst value the criterion can be evaluated with */
	private int worst;
	
	/** the best value the criterion can be evaluated with */
	private int best;
	
	
	/** default constructor for remote procedure call (RPC) */
	public EvCriterion() {
	}
	
	public EvCriterion(EV ev, IDENT identifier, WoString name, WoString description, int worst, int best, UserIdentifier author) {
		super(ev, identifier, name, description, author);
		
		this.worst = worst;
		this.best = best;
	}
	
	
	/**
	 * @return the worst value the criterion can be evaluated with
	 */
	public int getWorst() {
		return worst;
	}
	
	/**
	 * @return the best value the criterion can be evaluated with
	 */
	public int getBest() {
		return best;
	}
	
	public static <EV extends EvAbstract> EvCriterion<EV, CriterionShortIdentifier> shortCast(EvCriterion<EV, CriterionIdentifier> criterion) {
		return new EvCriterion<EV, CriterionShortIdentifier>(
				criterion.getEv(),
				criterion.getIdentifier().getShortIdentifier(),
				criterion.getName(),
				criterion.getDescription(),
				criterion.getWorst(),
				criterion.getBest(),
				criterion.getAuthor()
				);
	}
	
	/**
	 * TODO is there no common way to do this?
	 * @param criterion
	 * @return
	 */
	public static <EV extends EvAbstract> EvCriterion<EV, EntryClassIdentifier> containerCast(EvCriterion<EV, CriterionIdentifier> criterion) {
		return new EvCriterion<EV, EntryClassIdentifier>(
				criterion.getEv(),
				criterion.getIdentifier(),
				criterion.getName(),
				criterion.getDescription(),
				criterion.getWorst(),
				criterion.getBest(),
				criterion.getAuthor()
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
