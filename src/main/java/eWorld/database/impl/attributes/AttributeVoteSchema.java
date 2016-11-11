package eWorld.database.impl.attributes;

import com.datastax.driver.core.Session;

import eWorld.database.impl.BooleanVoteSchema;
import eWorld.database.impl.Column;
import eWorld.database.impl.DB;
import eWorld.database.impl.IdGenerator;
import eWorld.datatypes.identifiers.AttributeIdentifier;
import eWorld.datatypes.identifiers.AttributeShortIdentifier;
import eWorld.datatypes.identifiers.EntryClassIdentifier;
import eWorld.datatypes.identifiers.VoteIdentifier;

public class AttributeVoteSchema extends BooleanVoteSchema<
		VoteIdentifier<AttributeIdentifier>,
		VoteIdentifier<EntryClassIdentifier>,
		AttributeShortIdentifier,
		AttributeIdentifier,
		EntryClassIdentifier
	> {

	// static finals
	
	protected static final Column c_entryClassId = new Column("entryClassId", IdGenerator.idType);
	
	protected static final Column c_attributeId = new Column("attributeId", IdGenerator.idType);
	
	
	// constructors
	
	public AttributeVoteSchema(Session session, String schemaName) {
		super(session, schemaName);
	}

	
	// methods
	
	
	// overridden methods

	@Override
	protected Object[] getIdentifierValues(
			VoteIdentifier<AttributeIdentifier> identifier) {
		assert null != identifier;
		
		return new Long[] {identifier.getUserIdent().getUserId(),
				identifier.getEvIdent().getEntryClassId(), identifier.getEvIdent().getAttributeId()};
	}

	@Override
	protected Column[] getListItemsSelectColumns() {
		return new Column[] {c_attributeId, c_vote};
	}

	@Override
	protected Column[] getListItemsWhereColumns() {
		return new Column[] {c_userId, c_entryClassId};
	}

	@Override
	protected Column getShortIdentifierColumn() {
		return c_attributeId;
	}

	@Override
	protected Column[] getColumns() {
		return new Column[] {c_userId, c_entryClassId, c_attributeId, c_vote};
	}

	@Override
	protected String getPrimaryKey() {
		return c_userId.getName() + ", " + c_entryClassId.getName() + ", " + c_attributeId.getName();
	}

	@Override
	protected Column[] getIdentifierColumns() {
		return new Column[] {c_userId, c_entryClassId, c_attributeId};
	}

	@Override
	protected Object[] getSuperIdentifierValues(
			VoteIdentifier<EntryClassIdentifier> superIdentifier) {
		return new Long[] {superIdentifier.getUserIdent().getUserId(), superIdentifier.getEvIdent().getEntryClassId()};
	}

}
