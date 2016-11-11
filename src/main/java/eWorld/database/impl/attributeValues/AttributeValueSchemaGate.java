package eWorld.database.impl.attributeValues;

import java.util.ArrayList;

import com.datastax.driver.core.Row;

import eWorld.database.impl.RatedSchemaGate;
import eWorld.database.impl.Column;
import eWorld.datatypes.containers.EvAttributeContainer;
import eWorld.datatypes.containers.EvAttributeValueContainer;
import eWorld.datatypes.data.EvAttributeValue;
import eWorld.datatypes.data.IdAttributeTopValue;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.evs.EvVoid;
import eWorld.datatypes.identifiers.AttributeIdentifier;
import eWorld.datatypes.identifiers.AttributeValueIdentifier;
import eWorld.datatypes.identifiers.AttributeValueShortIdentifier;
import eWorld.datatypes.identifiers.EntryClassIdentifier;
import eWorld.datatypes.identifiers.UserIdentifier;

public class AttributeValueSchemaGate extends RatedSchemaGate<
		AttributeValueIdentifier,
		AttributeIdentifier,
		AttributeValueShortIdentifier,
		String,
		EvAttributeValue<Ev, AttributeValueIdentifier>,
		EvAttributeValue<?, AttributeIdentifier>,
		EvAttributeValue<Ev, AttributeValueShortIdentifier>,
		EvAttributeValueContainer
	> {

	// attributes
	
	private final AttributeValueDataSchema dataSchema;
	private final AttributeValueCounterSchema counterSchema;
	private final AttributeValueVoteSchema voteSchema;
	
	
	// constructors
	
	public AttributeValueSchemaGate(
			AttributeValueDataSchema dataSchema,
			AttributeValueCounterSchema counterSchema,
			AttributeValueVoteSchema voteSchema) {
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
	protected Object getShortIdentifierOfRow(Column shortIdentifierColumn, Row row) {
		return row.getString(shortIdentifierColumn.getName());
	}

	@Override
	protected EvAttributeValueContainer constructEvDataTypeContainer(
			AttributeIdentifier superIdentifier,
			ArrayList<EvAttributeValue<Ev, AttributeValueShortIdentifier>> data) {
		
		return new EvAttributeValueContainer(
				superIdentifier,
				data);
	}

}
