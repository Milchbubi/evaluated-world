package eWorld.database.impl.criterionValues;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.DataType;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import eWorld.database.impl.Column;
import eWorld.database.impl.DB;
import eWorld.database.impl.RatedDataSchema;
import eWorld.database.impl.Util;
import eWorld.datatypes.data.IdCriterionValue;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.identifiers.CriterionIdentifier;
import eWorld.datatypes.identifiers.CriterionShortIdentifier;
import eWorld.datatypes.identifiers.EntryClassIdentifier;

public class CriterionValueDataSchema extends RatedDataSchema<
		CriterionIdentifier,
		EntryClassIdentifier,
		CriterionShortIdentifier,
		Long,
		IdCriterionValue<CriterionIdentifier>,
		IdCriterionValue<EntryClassIdentifier>,
		IdCriterionValue<CriterionShortIdentifier>
	> {

	// static finals
	
	private static final Column c_average = new Column("average", DataType.cfloat());
	
	private static final Column c_votes = new Column("votes", DataType.bigint());
	
	
	// preparedStatements
	
	private PreparedStatement insertStatement;
	
	
	// constructors
	public CriterionValueDataSchema(Session session, String schemaName) {
		super(session, schemaName);
		
		prepareStatements();
	}
	
	
	// methods
	
	private void prepareStatements() {
		
		insertStatement = session.prepare(
				"INSERT INTO " + getSchemaName()
				+ "("
				+ Util.composePreparedStatementSelectPart(new Column[] {c_containerId, c_rating, c_id, c_rank, c_average, c_votes})
				+ ") "
				+ "VALUES (?, ?, ?, ?, ?, ?)"
				+ ";");	// TODO use if not exists
		
	}
	
	/**
	 * executes a select statement
	 * @param identifier
	 * @return the IdCriterionValue and Ev that matches to the given identifier or null if there is none
	 */
	protected CriterionValueAndEv selectOneCriterionValueAndEv(CriterionIdentifier identifier) {
		assert null != identifier;
		
		Row row = selectOneRow(identifier);
		
		if (null != row) {
			return new CriterionValueAndEv(constructEvDataTypeCompleteIdentified(row), constructEv(row));
		} else {
			return null;
		}
	}
	protected class CriterionValueAndEv {
		protected final IdCriterionValue<CriterionIdentifier> criterionValue;
		protected final Ev e;
		
		private CriterionValueAndEv(IdCriterionValue<CriterionIdentifier> criterionValue, Ev e) {
			this.criterionValue = criterionValue;
			this.e = e;
		}
	}
	
	// overridden methods
	
	@Override
	public void insert(IdCriterionValue<EntryClassIdentifier> item,
			CriterionShortIdentifier shortIdentifier, float rating, Integer rank) {
		assert null != item;
		
		BoundStatement boundInsertStatement = new BoundStatement(insertStatement);	// TODO create always new instance of BoundStatement?
		session.execute(boundInsertStatement.bind(
				item.getIdentifier().getEntryClassId(),
				rating,
				shortIdentifier.getShortId(),
				rank,
				item.getAverage(),
				item.getVotes()
				));
		
		// TODO check if inserting was successful (via {@code selectOne(..)}
	}

	@Override
	protected int getListItemsRankedLimit() {
		return 40;
	}

	@Override
	protected IdCriterionValue<EntryClassIdentifier> restrainEvDataTypeCompleteIdentifiedToSuperIdentified(
			IdCriterionValue<CriterionIdentifier> item) {
		assert null != item;
		
		return new IdCriterionValue<EntryClassIdentifier>(
				new EntryClassIdentifier(item.getIdentifier().getEntryClassId()),
				item.getAverage(),
				item.getVotes()
				);
	}

	@Override
	protected IdCriterionValue<CriterionIdentifier> constructEvDataTypeCompleteIdentified(
			Row dataRow) {
		assert null != dataRow;
		
		return new IdCriterionValue<CriterionIdentifier>(
				new CriterionIdentifier(dataRow.getLong(c_containerId.getName()), dataRow.getLong(c_id.getName())),
				dataRow.getFloat(c_average.getName()),
				dataRow.getLong(c_votes.getName())
				);
	}

	@Override
	protected IdCriterionValue<CriterionShortIdentifier> constructEvDataTypeShortIdentified(
			Row dataRow) {
		assert null != dataRow;
		
		return new IdCriterionValue<CriterionShortIdentifier>(
				new CriterionShortIdentifier(dataRow.getLong(c_id.getName())),
				dataRow.getFloat(c_average.getName()),
				dataRow.getLong(c_votes.getName())
				);
	}

	@Override
	protected Column[] getListItemsSelectColumns() {
		return new Column[] {c_id, c_rating, c_rank, c_average, c_votes};
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
		return new Object[] {superIdentifier.getEntryClassId()};
	}

	@Override
	protected Column[] getColumns() {
		return new Column[] {c_containerId, c_rating, c_id, c_rank, c_average, c_votes};
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
		return new Object[] {identifier.getEntryClassId(), identifier.getCriterionId()};
	}

}
