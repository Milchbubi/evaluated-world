package eWorld.database.impl.criterionValues;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.DataType;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import eWorld.database.impl.Column;
import eWorld.database.impl.CounterSchema;
import eWorld.database.impl.DB;
import eWorld.database.impl.IdGenerator;
import eWorld.database.impl.Util;
import eWorld.datatypes.elementars.IntegerVotes;
import eWorld.datatypes.identifiers.CriterionIdentifier;
import eWorld.datatypes.identifiers.CriterionShortIdentifier;
import eWorld.datatypes.identifiers.EntryClassIdentifier;

public class CriterionValueCounterSchema extends CounterSchema<
		CriterionIdentifier,
		EntryClassIdentifier,
		CriterionShortIdentifier
	> {

	// static finals
	
	private static final Column c_entryId = new Column("entryId", IdGenerator.idType);
	
	private static final Column c_criterionId = new Column("criterionId", IdGenerator.idType);
	
	private static final Column c_sum = new Column("sum", DataType.counter());
	
	private static final Column c_votes = new Column("votes", DataType.counter());
	
	
	// preparedStatements
	
	private PreparedStatement updateStatement;
	private PreparedStatement selectOneStatement;
	
	
	// constructors
	
	public CriterionValueCounterSchema(Session session, String schemaName) {
		super(session, schemaName);
		
		prepareStatements();
	}

	// methods

	private void prepareStatements() {
		
		updateStatement = session.prepare(
				"UPDATE " + getSchemaName()
				+ " SET " + c_sum.getName() + " = " + c_sum.getName() + " + ?"
				+ ", " + c_votes.getName() + " = " + c_votes.getName() + " + ?"
				+ " WHERE "
				+ Util.composePreparedStatementWherePart(getIdentifierColumns())
				+ ";");
		
		selectOneStatement = session.prepare(
				"SELECT " 
				+ Util.composePreparedStatementSelectPart(getColumns())
				+ " FROM " + getSchemaName()
				+ " WHERE "
				+ Util.composePreparedStatementWherePart(getIdentifierColumns())
				+ ";");
	}
	
	/**
	 * 
	 * @param identifier
	 * @param value increments|decrements c_sum
	 * @param votes increments|decrements c_votes
	 */
	public void update(CriterionIdentifier identifier, long value, long votes) {
		assert null != identifier;
		
		BoundStatement boundStatement = new BoundStatement(updateStatement);	// TODO create always new instance of BoundStatement?
		session.execute(boundStatement.bind(value, votes, identifier.getEntryClassId(), identifier.getCriterionId()));
	}
	
	/**
	 * executes a select statement
	 * @param identifier
	 * @return the object that matches to the given identifier
	 */
	public IntegerVotes selectOne(CriterionIdentifier identifier) {
		assert null != identifier;
		
		BoundStatement boundStatement = new BoundStatement(selectOneStatement);	// TODO create always new instance of BoundStatement?
		ResultSet results = session.execute(boundStatement.bind(getIdentifierValues(identifier)));
		
		Row row = results.one();
		
		IntegerVotes votes = new IntegerVotes(
				row.getLong(c_sum.getName()),
				row.getLong(c_votes.getName())
				);
		
		assert results.isExhausted();
		
		return votes;
	}
	
	
	// overridden methods
	
	@Override
	protected Column[] getListItemsSelectColumns() {
		return new Column[] {c_criterionId, c_sum, c_votes};
	}

	@Override
	protected Column[] getListItemsWhereColumns() {
		return new Column[] {c_entryId};
	}

	@Override
	protected Column getShortIdentifierColumn() {
		return c_criterionId;
	}

	@Override
	protected Object[] getSuperIdentifierValues(
			EntryClassIdentifier superIdentifier) {
		return new Object[] {superIdentifier.getEntryClassId()};
	}

	@Override
	protected Column[] getColumns() {
		return new Column[] {c_entryId, c_criterionId, c_sum, c_votes};
	}

	@Override
	protected String getPrimaryKey() {
		return c_entryId.getName() + ", " + c_criterionId.getName();
	}

	@Override
	protected Column[] getIdentifierColumns() {
		return new Column[] {c_entryId, c_criterionId};
	}

	@Override
	protected Object[] getIdentifierValues(CriterionIdentifier identifier) {
		return new Object[] {identifier.getEntryClassId(), identifier.getCriterionId()};
	}

}
