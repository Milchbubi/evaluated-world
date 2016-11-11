package eWorld.database.impl.attributeValues;

import com.datastax.driver.core.DataType;
import com.datastax.driver.core.Session;

import eWorld.database.impl.Column;
import eWorld.database.impl.DB;
import eWorld.database.impl.IdGenerator;
import eWorld.database.impl.UpDownCounterSchema;
import eWorld.datatypes.identifiers.AttributeIdentifier;
import eWorld.datatypes.identifiers.AttributeValueIdentifier;
import eWorld.datatypes.identifiers.AttributeValueShortIdentifier;

public class AttributeValueCounterSchema extends UpDownCounterSchema<
		AttributeValueIdentifier,
		AttributeIdentifier,
		AttributeValueShortIdentifier
	> {

	// static finals
	
	private static final Column c_entryId = new Column("entryId", IdGenerator.idType);
	
	private static final Column c_attributeId = new Column("attributeId", IdGenerator.idType);
	
	private static final Column c_value = new Column("value", DataType.varchar());
	
	
	// constructors
	
	public AttributeValueCounterSchema(Session session, String schemaName) {
		super(session, schemaName);
	}
	
	
	// methods

	
	// overridden methods

	@Override
	protected Object[] getIdentifierValues(AttributeValueIdentifier identifier) {
		assert null != identifier;
		
		return new Object[] {identifier.getEntryClassId(), identifier.getAttributeId(), identifier.getValueId()};
	}

	@Override
	protected Column[] getListItemsSelectColumns() {
		return new Column[] {c_value, c_up, c_down};
	}

	@Override
	protected Column[] getListItemsWhereColumns() {
		return new Column[] {c_entryId, c_attributeId};
	}

	@Override
	protected Column getShortIdentifierColumn() {
		return c_value;
	}

	@Override
	protected Column[] getColumns() {
		return new Column[] {c_entryId, c_attributeId, c_value, c_up, c_down};
	}

	@Override
	protected String getPrimaryKey() {
		return c_entryId.getName() + ", " + c_attributeId.getName() + ", " + c_value.getName();
	}

	@Override
	protected Column[] getIdentifierColumns() {
		return new Column[] {c_entryId, c_attributeId, c_value};
	}

	@Override
	protected Object[] getSuperIdentifierValues(
			AttributeIdentifier superIdentifier) {
		return new Object[] {superIdentifier.getEntryClassId(), superIdentifier.getAttributeId()};
	}

}
