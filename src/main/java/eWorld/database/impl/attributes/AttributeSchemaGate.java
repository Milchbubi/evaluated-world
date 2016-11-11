package eWorld.database.impl.attributes;

import java.util.ArrayList;

import eWorld.database.impl.RatedSchemaGate;
import eWorld.datatypes.containers.EvAttributeContainer;
import eWorld.datatypes.containers.EvEntryContainer;
import eWorld.datatypes.data.EvAttribute;
import eWorld.datatypes.data.EvEntry;
import eWorld.datatypes.elementars.UpDownVote;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.evs.EvVoid;
import eWorld.datatypes.identifiers.AttributeIdentifier;
import eWorld.datatypes.identifiers.AttributeShortIdentifier;
import eWorld.datatypes.identifiers.EntryClassIdentifier;
import eWorld.datatypes.identifiers.EntryIdentifier;
import eWorld.datatypes.identifiers.EntryShortIdentifier;
import eWorld.datatypes.identifiers.UserIdentifier;
import eWorld.datatypes.identifiers.VoteIdentifier;

public class AttributeSchemaGate extends RatedSchemaGate<
		AttributeIdentifier,
		EntryClassIdentifier,
		AttributeShortIdentifier,
		Long,
		EvAttribute<Ev, AttributeIdentifier>,
		EvAttribute<?, EntryClassIdentifier>,
		EvAttribute<Ev, AttributeShortIdentifier>,
		EvAttributeContainer
	> {

	// attributes
	
	private final AttributeDataSchema dataSchema;
	private final AttributeCounterSchema counterSchema;
	private final AttributeVoteSchema voteSchema;
	
	
	// constructors
	
	public AttributeSchemaGate(AttributeDataSchema dataSchema, AttributeCounterSchema counterSchema, AttributeVoteSchema voteSchema) {
		super(dataSchema, counterSchema, voteSchema);
		
		assert null != dataSchema;
		assert null != counterSchema;
		assert null != voteSchema;
		
		this.dataSchema = dataSchema;
		this.counterSchema = counterSchema;
		this.voteSchema = voteSchema;
	}

	
	// methods
	
	
	// overridden methods
	
	@Override
	protected EvAttributeContainer constructEvDataTypeContainer(
			EntryClassIdentifier superIdentifier,
			ArrayList<EvAttribute<Ev, AttributeShortIdentifier>> data) {
		
		return new EvAttributeContainer(
				superIdentifier,
				data);
	}

}
