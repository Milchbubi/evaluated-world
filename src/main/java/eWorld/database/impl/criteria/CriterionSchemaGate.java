package eWorld.database.impl.criteria;

import java.util.ArrayList;

import eWorld.database.impl.RatedSchemaGate;
import eWorld.datatypes.containers.EvCriterionContainer;
import eWorld.datatypes.data.EvCriterion;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.evs.EvVoid;
import eWorld.datatypes.identifiers.CriterionIdentifier;
import eWorld.datatypes.identifiers.CriterionShortIdentifier;
import eWorld.datatypes.identifiers.EntryClassIdentifier;

public class CriterionSchemaGate extends RatedSchemaGate<
		CriterionIdentifier,
		EntryClassIdentifier,
		CriterionShortIdentifier,
		Long,
		EvCriterion<Ev, CriterionIdentifier>,
		EvCriterion<?, EntryClassIdentifier>,
		EvCriterion<Ev, CriterionShortIdentifier>,
		EvCriterionContainer
	> {

	public CriterionSchemaGate(
			CriterionDataSchema dataSchema,
			CriterionCounterSchema counterSchema,
			CriterionVoteSchema voteSchema) {
		super(dataSchema, counterSchema, voteSchema);
	}

	@Override
	protected EvCriterionContainer constructEvDataTypeContainer(
			EntryClassIdentifier superIdentifier,
			ArrayList<EvCriterion<Ev, CriterionShortIdentifier>> data) {
		
		return new EvCriterionContainer(
				superIdentifier,
				data);
	}

}
