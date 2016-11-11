package eWorld.database.impl.attributeValues;

import com.datastax.driver.core.DataType;
import com.datastax.driver.core.Session;

import eWorld.database.impl.BooleanVoteSchema;
import eWorld.database.impl.Column;
import eWorld.database.impl.DB;
import eWorld.database.impl.IdGenerator;
import eWorld.datatypes.identifiers.AttributeIdentifier;
import eWorld.datatypes.identifiers.AttributeValueIdentifier;
import eWorld.datatypes.identifiers.AttributeValueShortIdentifier;
import eWorld.datatypes.identifiers.VoteIdentifier;

public class AttributeValueVoteSchema extends BooleanVoteSchema<
		VoteIdentifier<AttributeValueIdentifier>,
		VoteIdentifier<AttributeIdentifier>,
		AttributeValueShortIdentifier,
		AttributeValueIdentifier,
		AttributeIdentifier
	> {

	// static finals
	
	private static final Column c_entryId = new Column("entryId", IdGenerator.idType);
	
	private static final Column c_attributeId = new Column("attributeId", IdGenerator.idType);
	
	private static final Column c_value = new Column("value", DataType.varchar());
	
	
	// constructors
	
	public AttributeValueVoteSchema(Session session, String schemaName) {
		super(session, schemaName);
	}

	@Override
	protected Object[] getIdentifierValues(
			VoteIdentifier<AttributeValueIdentifier> identifier) {
		assert null != identifier;
		
		return new Object[] {
				identifier.getUserIdent().getUserId(),
				identifier.getEvIdent().getEntryClassId(), 
				identifier.getEvIdent().getAttributeId(), 
				identifier.getEvIdent().getValueId()};
	}

	@Override
	protected Column[] getListItemsSelectColumns() {
		return new Column[] {c_value, c_vote};
	}

	@Override
	protected Column[] getListItemsWhereColumns() {
		return new Column[] {c_userId, c_entryId, c_attributeId};
	}

	@Override
	protected Column getShortIdentifierColumn() {
		return c_value;
	}
	
	@Override
	protected Column[] getColumns() {
		return new Column[] {c_userId, c_entryId, c_attributeId, c_value, c_vote};
	}

	@Override
	protected String getPrimaryKey() {
		return c_userId.getName() + ", " + c_entryId.getName() + ", " + c_attributeId.getName() + ", " + c_value.getName();
	}

	@Override
	protected Column[] getIdentifierColumns() {
		return new Column[] {c_userId, c_entryId, c_attributeId, c_value};
	}

	@Override
	protected Object[] getSuperIdentifierValues(
			VoteIdentifier<AttributeIdentifier> superIdentifier) {
		return new Object[] {
				superIdentifier.getUserIdent().getUserId(),
				superIdentifier.getEvIdent().getEntryClassId(), 
				superIdentifier.getEvIdent().getAttributeId()};
	}

}
