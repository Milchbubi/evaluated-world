package eWorld.database.impl.attributeValues;

import java.util.ArrayList;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.DataType;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import eWorld.database.impl.Column;
import eWorld.database.impl.DB;
import eWorld.database.impl.RatedDataSchema;
import eWorld.database.impl.Util;
import eWorld.datatypes.containers.IdAttributeTopValueContainer;
import eWorld.datatypes.data.IdAttributeTopValue;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.evs.EvVoid;
import eWorld.datatypes.identifiers.AttributeIdentifier;
import eWorld.datatypes.identifiers.AttributeShortIdentifier;
import eWorld.datatypes.identifiers.EntryClassIdentifier;

public class AttributeTopValueDataSchema extends RatedDataSchema<
		AttributeIdentifier,
		EntryClassIdentifier,
		AttributeShortIdentifier,
		Long,
		IdAttributeTopValue<AttributeIdentifier>,
		IdAttributeTopValue<EntryClassIdentifier>,
		IdAttributeTopValue<AttributeShortIdentifier>
	> {
	
	// static finals
	
	private static final Column c_value = new Column("value", DataType.varchar());
	
	
	// preparedStatements
	
	private PreparedStatement insertStatement;
	
	private PreparedStatement updateStatement;
	
	
	// constructors
	
	public AttributeTopValueDataSchema(Session session, String schemaName) {
		super(session, schemaName);
		
		prepareStatements();
	}

	
	// methods
	
	private void prepareStatements() {
		
		insertStatement = session.prepare(
				"INSERT INTO " + getSchemaName()
				+ "("
				+ Util.composePreparedStatementSelectPart(new Column[] {c_containerId, c_rating, c_id, c_rank, c_value})
				+ ") "
				+ "VALUES (?, ?, ?, ?, ?)"
				+ ";");	// TODO use if not exists
		
		updateStatement = session.prepare(
				"UPDATE " + getSchemaName()
				+ " SET " + c_value.getName() + " = ?"
				+ " WHERE " + Util.composePreparedStatementWherePart(new Column[] {c_containerId, c_rating, c_id})
				+ ";");
	}
	
	/**
	 * returns a {@code IdAttributeTopValueContainer} that lists attributeValues of an entry in rated order
	 * @param superIdentifier specifies the entry which values should be loaded
	 * @return a {@code IdAttributeTopValueContainer} that contains elements of the type {@code IdAttributeTopValue}
	 * TODO? check if superIdentifier is valid?
	 */
	public IdAttributeTopValueContainer listValuesOfEntryRated(EntryClassIdentifier superIdentifier) {
		assert null != superIdentifier;
		
		ArrayList<IdAttributeTopValue<AttributeShortIdentifier>> data = new ArrayList<IdAttributeTopValue<AttributeShortIdentifier>>();
		for (Row row : super.listItems(superIdentifier)) {
			data.add(constructEvDataTypeShortIdentified(row));
		}
		
		return new IdAttributeTopValueContainer(superIdentifier, data);
	}
	
	/**
	 * updates the topValue of the specified attribute
	 * @param item specifies the attribute
	 */
	public void update(IdAttributeTopValue<AttributeIdentifier> item, float rating) {
		assert null != item;
		
		BoundStatement boundStatement = new BoundStatement(updateStatement);	// TODO create always new instance of BoundStatement?
		session.execute(boundStatement.bind(
				item.getValue(),
				item.getIdentifier().getEntryClassId(),
				rating,
				item.getIdentifier().getAttributeId()
				));
		
		// TODO check if Attribute is not redundant (two rows with different rating)
	}
	
	
	// overridden methods
	
	@Override
	public void insert(IdAttributeTopValue<EntryClassIdentifier> item,
			AttributeShortIdentifier shortIdentifier, float rating, Integer rank) {
		assert null != item;
		
		BoundStatement boundInsertStatement = new BoundStatement(insertStatement);	// TODO create always new instance of BoundStatement?
		session.execute(boundInsertStatement.bind(
				item.getIdentifier().getEntryClassId(),
				rating,
				shortIdentifier.getAttributeId(),
				rank,
				item.getValue()
				));
		
		// TODO check if inserting was successful (via {@code selectOne(..)}
	}

	@Override
	protected int getListItemsRankedLimit() {
		return 50;
	}

	@Override
	protected IdAttributeTopValue<EntryClassIdentifier> restrainEvDataTypeCompleteIdentifiedToSuperIdentified(
			IdAttributeTopValue<AttributeIdentifier> item) {
		assert null != item;
		
		return new IdAttributeTopValue<EntryClassIdentifier>(
				new EntryClassIdentifier(item.getIdentifier().getEntryClassId()),
				item.getValue()
				);
	}

	@Override
	protected IdAttributeTopValue<AttributeIdentifier> constructEvDataTypeCompleteIdentified(
			Row dataRow) {
		assert null != dataRow;
		
		return new IdAttributeTopValue<AttributeIdentifier>(
				new AttributeIdentifier(dataRow.getLong(c_containerId.getName()), dataRow.getLong(c_id.getName())),
				dataRow.getString(c_value.getName())
				);
	}

	@Override
	protected IdAttributeTopValue<AttributeShortIdentifier> constructEvDataTypeShortIdentified(
			Row dataRow) {
		assert null != dataRow;
		
		return new IdAttributeTopValue<AttributeShortIdentifier>(
				new AttributeShortIdentifier(dataRow.getLong(c_id.getName())),
				dataRow.getString(c_value.getName())
				);
	}

	@Override
	protected Column[] getListItemsSelectColumns() {
		return new Column[] {c_id, c_rank, c_rating, c_value};
	}

	@Override
	protected Column[] getListItemsWhereColumns() {
		return new Column[] {c_containerId};
	}
	
	@Override
	protected Object[] getSuperIdentifierValues(
			EntryClassIdentifier superIdentifier) {
		return new Long[] {superIdentifier.getEntryClassId()};
	}

	@Override
	protected Column getShortIdentifierColumn() {
		return c_id;
	}

	@Override
	protected Column[] getColumns() {
		return new Column[] {c_containerId, c_rating, c_id, c_rank, c_value};
	}

	@Override
	protected String getPrimaryKey() {
		return c_containerId.getName() + ", " + c_rating.getName() + ", " + c_id.getName();
	}

	@Override
	protected Column[] getIdentifierColumns() {
		return new Column[] {c_containerId, c_id};
	}

	@Override
	protected Object[] getIdentifierValues(
			AttributeIdentifier identifier) {
		assert null != identifier;
		
		return new Long[] {identifier.getEntryClassId(), identifier.getAttributeId()};
	}

}
