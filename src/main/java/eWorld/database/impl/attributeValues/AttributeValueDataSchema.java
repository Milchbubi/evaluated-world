package eWorld.database.impl.attributeValues;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.DataType;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import eWorld.database.impl.Column;
import eWorld.database.impl.DB;
import eWorld.database.impl.EvDataSchema;
import eWorld.database.impl.Util;
import eWorld.datatypes.data.EvAttributeValue;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.evs.EvVoid;
import eWorld.datatypes.identifiers.AttributeIdentifier;
import eWorld.datatypes.identifiers.AttributeValueIdentifier;
import eWorld.datatypes.identifiers.AttributeValueShortIdentifier;
import eWorld.datatypes.identifiers.UserIdentifier;

public class AttributeValueDataSchema extends EvDataSchema<
		AttributeValueIdentifier,
		AttributeIdentifier,
		AttributeValueShortIdentifier,
		String,
		EvAttributeValue<Ev, AttributeValueIdentifier>,
		EvAttributeValue<?, AttributeIdentifier>,
		EvAttributeValue<Ev, AttributeValueShortIdentifier>
	> {

	// static finals
	
	private static final Column c_value = new Column("value", DataType.varchar());
	
	
	// preparedStatements
	
	private PreparedStatement insertStatement;
	
	
	// constructors
	
	public AttributeValueDataSchema(Session session, String schemaName) {
		super(session, schemaName);
		
		prepareStatements();
	}
	
	
	// methods
	
	private void prepareStatements() {
		
		insertStatement = session.prepare(
				"INSERT INTO " + getSchemaName()
				+ "("
				+ Util.composePreparedStatementSelectPart(new Column[] {c_containerId, c_id, c_rating, c_value, c_rank, c_authorId})
				+ ") "
				+ "VALUES (?, ?, ?, ?, ?, ?)"
				+ ";");	// TODO use if not exists
		
	}
	
	
	// overridden methods

	@Override
	protected void createIndices() {
		super.createIndices();
		
		// index for value
		session.execute(
				"CREATE INDEX IF NOT EXISTS " + getSchemaName() + "_" + c_value.getName() + "_index"
				+ " ON " + getSchemaName()
				+ "(" + c_value.getName() + ")"
				+ ";");
		
	}
	
	@Override
	public void insert(
			EvAttributeValue<?, AttributeIdentifier> item,
			AttributeValueShortIdentifier shortIdentifier, float rating,
			Integer rank) {
		assert null != item;
		
		BoundStatement boundInsertStatement = new BoundStatement(insertStatement);	// TODO create always new instance of BoundStatement?
		session.execute(boundInsertStatement.bind(
				item.getIdentifier().getEntryClassId(),
				item.getIdentifier().getAttributeId(),
				rating,
				shortIdentifier.getValue(),
				rank,
				item.getAuthor().getUserId()
				));
		
		// TODO check if inserting was successful (via {@code selectOne(..)}
	}

	@Override
	protected int getListItemsRankedLimit() {
		return 20;
	}

	@Override
	protected EvAttributeValue<EvVoid, AttributeIdentifier> restrainEvDataTypeCompleteIdentifiedToSuperIdentified(
			EvAttributeValue<Ev, AttributeValueIdentifier> item) {
		assert null != item;
		
		return new EvAttributeValue<EvVoid, AttributeIdentifier>(
				new EvVoid(),
				new AttributeIdentifier(item.getIdentifier().getEntryClassId(), item.getIdentifier().getAttributeId()),
				item.getAuthor()
				);
	}

	@Override
	protected EvAttributeValue<Ev, AttributeValueIdentifier> constructEvDataTypeCompleteIdentified(
			Row dataRow) {
		assert null != dataRow;
		
		String value = dataRow.getString(c_value.getName());
		
		return new EvAttributeValue<Ev, AttributeValueIdentifier>(
				constructEv(dataRow),
				new AttributeValueIdentifier(dataRow.getLong(c_containerId.getName()), dataRow.getLong(c_id.getName()), value),
				new UserIdentifier(dataRow.getLong(c_authorId.getName()))
				);
	}

	@Override
	protected EvAttributeValue<Ev, AttributeValueShortIdentifier> constructEvDataTypeShortIdentified(
			Row dataRow) {
		assert null != dataRow;
		
		String value = dataRow.getString(c_value.getName());
		
		return new EvAttributeValue<Ev, AttributeValueShortIdentifier>(
				constructEv(dataRow),
				new AttributeValueShortIdentifier(value),
				new UserIdentifier(dataRow.getLong(c_authorId.getName()))
				);
	}

	@Override
	protected Column[] getListItemsSelectColumns() {
		return new Column[] {c_rating, c_value, c_rank, c_authorId};
	}

	@Override
	protected Column[] getListItemsWhereColumns() {
		return new Column[] {c_containerId, c_id};
	}
	
	@Override
	protected Object[] getSuperIdentifierValues(
			AttributeIdentifier superIdentifier) {
		return new Object[] {superIdentifier.getEntryClassId(), superIdentifier.getAttributeId()};
	}

	@Override
	protected Column getShortIdentifierColumn() {
		return c_value;
	}

	@Override
	protected Column[] getColumns() {
		return new Column[] {c_containerId, c_id, c_rating, c_value, c_rank, c_authorId};
	}

	@Override
	protected String getPrimaryKey() {
		return "(" + c_containerId.getName() + ", " + c_id.getName() + "), " + c_rating.getName() + ", " + c_value.getName();
	}

	@Override
	protected Column[] getIdentifierColumns() {
		return new Column[] {c_containerId, c_id, c_value};
	}

	@Override
	protected Object[] getIdentifierValues(AttributeValueIdentifier identifier) {
		assert null != identifier;
		
		return new Object[] {identifier.getEntryClassId(), identifier.getAttributeId(), identifier.getValueId()};
	}

}
