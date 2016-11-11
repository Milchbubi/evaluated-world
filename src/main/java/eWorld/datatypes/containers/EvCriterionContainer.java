package eWorld.datatypes.containers;

import java.io.Serializable;
import java.util.ArrayList;

import eWorld.datatypes.data.EvCriterion;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.identifiers.CriterionShortIdentifier;
import eWorld.datatypes.identifiers.EntryClassIdentifier;

@SuppressWarnings("serial")
public class EvCriterionContainer extends EvObjectContainer<
		EntryClassIdentifier,
		CriterionShortIdentifier,
		Long,
		EvCriterion<Ev, CriterionShortIdentifier>
	>
	implements Serializable  {
	
	// attributes
	
	
	// constructors
	
	/** default constructor for remote procedure call (RPC) */
	public EvCriterionContainer() {
	}
	
	public EvCriterionContainer(
			EntryClassIdentifier classIdentifier, 
			ArrayList<EvCriterion<Ev, CriterionShortIdentifier>> data) {
		super(classIdentifier, data);
	}
	
	
	// methods

}
