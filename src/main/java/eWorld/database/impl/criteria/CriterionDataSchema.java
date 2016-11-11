package eWorld.database.impl.criteria;

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
import eWorld.datatypes.data.EvCriterion;
import eWorld.datatypes.elementars.WoString;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.evs.EvVoid;
import eWorld.datatypes.identifiers.CriterionIdentifier;
import eWorld.datatypes.identifiers.CriterionShortIdentifier;
import eWorld.datatypes.identifiers.EntryClassIdentifier;
import eWorld.datatypes.identifiers.UserIdentifier;

public class CriterionDataSchema extends EvDataSchema<
		CriterionIdentifier,
		EntryClassIdentifier,
		CriterionShortIdentifier,
		Long,
		EvCriterion<Ev, CriterionIdentifier>,
		EvCriterion<?, EntryClassIdentifier>,
		EvCriterion<Ev, CriterionShortIdentifier>
	> {

	// static finals
	
	protected static final Column c_name = new Column("name", DataType.varchar());
	
	protected static final Column c_description = new Column("description", DataType.text());
	
	/** the worst value the criterion can be evaluated with */
	protected static final Column c_worst = new Column("worst", DataType.cint());
	
	/** the best value the criterion can be evaluated with */
	protected static final Column c_best = new Column("best", DataType.cint());
	
	
	// preparedStatements
	
	private PreparedStatement insertStatement;
	
	
	// attributes
	
	private final IdGenerator idGenerator;
	
	
	// constructors
	
	public CriterionDataSchema(Session session, String schemaName) {
		super(session, schemaName);

		idGenerator = new IdGenerator(session, getSchemaName());
		
		prepareStatements();
	}

	
	// methods
	
	private void prepareStatements() {
		
		insertStatement = session.prepare(
				"INSERT INTO " + getSchemaName()
				+ "("
				+ Util.composePreparedStatementSelectPart(new Column[] {c_containerId, c_rating, c_id, c_rank, c_name, c_description, c_worst, c_best, c_authorId})
				+ ") "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
				+ ";");	// TODO use if not exists
		
	}
	
	/**
	 * generates a new id that can be used for the {@code insert} method
	 * @return the generated id
	 */
	public CriterionShortIdentifier generateNewId() {
		return new CriterionShortIdentifier(idGenerator.generateId());
	}
	
	public EvCriterion<Ev, EntryClassIdentifier> toClassIdentifiedCriterion(EvCriterion<Ev, ?> criterion, EntryClassIdentifier classIdentifier) {
		return new EvCriterion<Ev, EntryClassIdentifier>(
				criterion.getEv(),
				classIdentifier,
				criterion.getName(),
				criterion.getDescription(),
				criterion.getWorst(),
				criterion.getBest(),
				criterion.getAuthor()
				);
	}
	
	
	// overridden methods
	
	@Override
	public void insert(EvCriterion<?, EntryClassIdentifier> item,
			CriterionShortIdentifier shortIdentifier, float rating, Integer rank) {
		assert null != item;
		
		BoundStatement boundInsertStatement = new BoundStatement(insertStatement);	// TODO create always new instance of BoundStatement?
		session.execute(boundInsertStatement.bind(
				item.getIdentifier().getEntryClassId(),
				rating,
				shortIdentifier.getShortId(),
				rank,
				item.getName().getString(),
				item.getDescriptionString(),
				item.getWorst(),
				item.getBest(),
				item.getAuthor().getUserId()
				));
		
		// TODO check if inserting was successful (via {@code selectOne(..)})
	}

	@Override
	protected int getListItemsRankedLimit() {
		return 20;
	}

	@Override
	protected EvCriterion<EvVoid, EntryClassIdentifier> restrainEvDataTypeCompleteIdentifiedToSuperIdentified(
			EvCriterion<Ev, CriterionIdentifier> item) {
		assert null != item;
		
		return new EvCriterion<EvVoid, EntryClassIdentifier>(
				new EvVoid(),
				new EntryClassIdentifier(item.getIdentifier().getEntryClassId()),
				item.getName(),
				item.getDescription(),
				item.getWorst(),
				item.getBest(),
				item.getAuthor()
				);
	}

	@Override
	protected EvCriterion<Ev, CriterionIdentifier> constructEvDataTypeCompleteIdentified(
			Row dataRow) {
		assert null != dataRow;
		
		return new EvCriterion<Ev, CriterionIdentifier>(
				constructEv(dataRow),
				new CriterionIdentifier(dataRow.getLong(c_containerId.getName()), dataRow.getLong(c_id.getName())),
				new WoString(dataRow.getString(c_name.getName())),
				new WoString(dataRow.getString(c_description.getName())),
				dataRow.getInt(c_worst.getName()),
				dataRow.getInt(c_best.getName()),
				new UserIdentifier(dataRow.getLong(c_authorId.getName()))
				);
	}

	@Override
	protected EvCriterion<Ev, CriterionShortIdentifier> constructEvDataTypeShortIdentified(
			Row dataRow) {
		assert null != dataRow;
		
		return new EvCriterion<Ev, CriterionShortIdentifier>(
				constructEv(dataRow),
				new CriterionShortIdentifier(dataRow.getLong(c_id.getName())),
				new WoString(dataRow.getString(c_name.getName())),
				new WoString(dataRow.getString(c_description.getName())),
				dataRow.getInt(c_worst.getName()),
				dataRow.getInt(c_best.getName()),
				new UserIdentifier(dataRow.getLong(c_authorId.getName()))
				);
	}

	@Override
	protected Column[] getListItemsSelectColumns() {
		return new Column[] {c_id, c_rank, c_rating, c_name, c_description, c_worst, c_best, c_authorId};
	}

	@Override
	protected Column[] getListItemsWhereColumns() {
		return new Column[] {c_containerId};
	}

	@Override
	protected Column getShortIdentifierColumn() {
		return c_id;
	}

	@Override
	protected Object[] getSuperIdentifierValues(
			EntryClassIdentifier superIdentifier) {
		return new Long[] {superIdentifier.getEntryClassId()};
	}

	@Override
	protected Column[] getColumns() {
		return new Column[] {c_containerId, c_rating, c_id, c_rank, c_name, c_description, c_worst, c_best, c_authorId};
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
	protected Object[] getIdentifierValues(CriterionIdentifier identifier) {
		return new Long[] {identifier.getEntryClassId(), identifier.getCriterionId()};
	}

}
