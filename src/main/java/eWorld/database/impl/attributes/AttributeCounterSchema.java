package eWorld.database.impl.attributes;

import com.datastax.driver.core.Session;

import eWorld.database.impl.Column;
import eWorld.database.impl.DB;
import eWorld.database.impl.IdGenerator;
import eWorld.database.impl.UpDownCounterSchema;
import eWorld.datatypes.identifiers.AttributeIdentifier;
import eWorld.datatypes.identifiers.AttributeShortIdentifier;
import eWorld.datatypes.identifiers.EntryClassIdentifier;

public class AttributeCounterSchema extends UpDownCounterSchema<
		AttributeIdentifier,
		EntryClassIdentifier,
		AttributeShortIdentifier
	> {

	// static finals
	
	protected static final Column c_entryClassId = new Column("entryClassId", IdGenerator.idType);
	
	protected static final Column c_attributeId = new Column("attributeId", IdGenerator.idType);
	
	
	// constructors
	
	public AttributeCounterSchema(Session session, String schemaName) {
		super(session, schemaName);
	}
	
	
	// methods

	
	// overridden methods
	
	@Override
	protected Object[] getIdentifierValues(AttributeIdentifier identifier) {
		assert null != identifier;
		
		return new Long[] {identifier.getEntryClassId(), identifier.getAttributeId()};
	}

	@Override
	protected Column[] getListItemsSelectColumns() {
		return new Column[] {c_attributeId, c_up, c_down};
	}

	@Override
	protected Column[] getListItemsWhereColumns() {
		return new Column[] {c_entryClassId};
	}

	@Override
	protected Column getShortIdentifierColumn() {
		return c_attributeId;
	}

	@Override
	protected Column[] getColumns() {
		return new Column[] {c_entryClassId, c_attributeId, c_up, c_down};
	}

	@Override
	protected String getPrimaryKey() {
		return c_entryClassId.getName() + ", " + c_attributeId.getName();
	}

	@Override
	protected Column[] getIdentifierColumns() {
		return new Column[] {c_entryClassId, c_attributeId};
	}


	@Override
	protected Object[] getSuperIdentifierValues(
			EntryClassIdentifier superIdentifier) {
		return new Long[] {superIdentifier.getEntryClassId()};
	}

}
