package eWorld.database.impl.attributes;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.DataType;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import eWorld.database.impl.Column;
import eWorld.database.impl.DB;
import eWorld.database.impl.EvDataSchema;
import eWorld.database.impl.IdGenerator;
import eWorld.database.impl.Util;
import eWorld.datatypes.data.EvAttribute;
import eWorld.datatypes.elementars.WoString;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.evs.EvVoid;
import eWorld.datatypes.identifiers.AttributeIdentifier;
import eWorld.datatypes.identifiers.AttributeShortIdentifier;
import eWorld.datatypes.identifiers.EntryClassIdentifier;
import eWorld.datatypes.identifiers.UserIdentifier;

public class AttributeDataSchema extends EvDataSchema<
		AttributeIdentifier,
		EntryClassIdentifier,
		AttributeShortIdentifier,
		Long,
		EvAttribute<Ev, AttributeIdentifier>,
		EvAttribute<?, EntryClassIdentifier>,
		EvAttribute<Ev, AttributeShortIdentifier>
	> {

	// static finals
	
	protected static final Column c_name = new Column("name", DataType.varchar());
	
	protected static final Column c_description = new Column("description", DataType.text());
	
	
	// preparedStatements
	
	private PreparedStatement insertStatement;
	
	
	// attributes
	
	private final IdGenerator idGenerator;
	
	
	// constructors
	
	public AttributeDataSchema(Session session, String schemaName) {
		super(session, schemaName);
		
		idGenerator = new IdGenerator(session, getSchemaName());
		
		prepareStatements();
	}

	
	// methods
	
	private void prepareStatements() {
		
		insertStatement = session.prepare(
				"INSERT INTO " + getSchemaName()
				+ "("
				+ Util.composePreparedStatementSelectPart(new Column[] {c_containerId, c_rating, c_id, c_rank, c_name, c_description, c_authorId})
				+ ") "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?)"
				+ ";");	// TODO use if not exists
		
	}
	
	/**
	 * generates a new id that can be used for the {@code insert} method
	 * @return the generated id
	 */
	public AttributeShortIdentifier generateNewId() {
		return new AttributeShortIdentifier(idGenerator.generateId());
	}
	
	public EvAttribute<Ev, EntryClassIdentifier> toClassIdentifiedAttribute(EvAttribute<Ev, ?> attribute, EntryClassIdentifier classIdentifier) {
		return new EvAttribute<Ev, EntryClassIdentifier>(
				attribute.getEv(),
				classIdentifier,
				attribute.getName(),
				attribute.getDescription(),
				attribute.getAuthor()
				);
	}
	
	
	// overridden methods
	
	@Override
	public void insert(EvAttribute<?, EntryClassIdentifier> item,
			AttributeShortIdentifier shortIdentifier, float rating, Integer rank) {
		assert null != item;
		
		BoundStatement boundInsertStatement = new BoundStatement(insertStatement);	// TODO create always new instance of BoundStatement?
		session.execute(boundInsertStatement.bind(
				item.getIdentifier().getEntryClassId(),
				rating,
				shortIdentifier.getAttributeId(),
				rank,
				item.getName().getString(),
				item.getDescriptionString(),
				item.getAuthor().getUserId()
				));
		
		// TODO check if inserting was successful (via {@code selectOne(..)})
	}

	@Override
	protected int getListItemsRankedLimit() {
		return 25;
	}

	@Override
	protected EvAttribute<EvVoid, EntryClassIdentifier> restrainEvDataTypeCompleteIdentifiedToSuperIdentified(
			EvAttribute<Ev, AttributeIdentifier> item) {
		assert null != item;
		
		return new EvAttribute<EvVoid, EntryClassIdentifier>(
				new EvVoid(),
				new EntryClassIdentifier(item.getIdentifier().getEntryClassId()),
				item.getName(),
				item.getDescription(),
				item.getAuthor()
				);
	}

	@Override
	protected EvAttribute<Ev, AttributeIdentifier> constructEvDataTypeCompleteIdentified(
			Row dataRow) {
		assert null != dataRow;
		
		return new EvAttribute<Ev, AttributeIdentifier>(
				constructEv(dataRow),
				new AttributeIdentifier(dataRow.getLong(c_containerId.getName()), dataRow.getLong(c_id.getName())),
				new WoString(dataRow.getString(c_name.getName())),
				new WoString(dataRow.getString(c_description.getName())),
				new UserIdentifier(dataRow.getLong(c_authorId.getName()))
				);
	}

	@Override
	protected EvAttribute<Ev, AttributeShortIdentifier> constructEvDataTypeShortIdentified(
			Row dataRow) {
		assert null != dataRow;
		
		return new EvAttribute<Ev, AttributeShortIdentifier>(
				constructEv(dataRow),
				new AttributeShortIdentifier(dataRow.getLong(c_id.getName())),
				new WoString(dataRow.getString(c_name.getName())),
				new WoString(dataRow.getString(c_description.getName())),
				new UserIdentifier(dataRow.getLong(c_authorId.getName()))
				);
	}

	@Override
	protected Column[] getListItemsSelectColumns() {
		return new Column[] {c_id, c_rank, c_rating, c_name, c_description, c_authorId};
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
		return new Column[] {c_containerId, c_rating, c_id, c_rank, c_name, c_description, c_authorId};
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
	protected Object[] getIdentifierValues(AttributeIdentifier identifier) {
		assert null != identifier;
		
		return new Long[] {identifier.getEntryClassId(), identifier.getAttributeId()};
	}

}
