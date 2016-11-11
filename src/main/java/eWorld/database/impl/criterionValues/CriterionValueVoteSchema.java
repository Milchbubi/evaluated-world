package eWorld.database.impl.criterionValues;

import com.datastax.driver.core.DataType;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import eWorld.database.impl.Column;
import eWorld.database.impl.DB;
import eWorld.database.impl.IdGenerator;
import eWorld.database.impl.VoteSchema;
import eWorld.datatypes.identifiers.CriterionIdentifier;
import eWorld.datatypes.identifiers.CriterionShortIdentifier;
import eWorld.datatypes.identifiers.EntryClassIdentifier;
import eWorld.datatypes.identifiers.VoteIdentifier;

public class CriterionValueVoteSchema extends VoteSchema<
		VoteIdentifier<CriterionIdentifier>,
		VoteIdentifier<EntryClassIdentifier>,
		CriterionShortIdentifier,
		CriterionIdentifier,
		EntryClassIdentifier,
		Integer
	> {

	// static finals
	
	private static final Column c_entryId = new Column("entryId", IdGenerator.idType);
	
	private static final Column c_criterionId = new Column("criterionId", IdGenerator.idType);
	
	private static final Column c_vote = new Column("vote", DataType.cint());
	
	
	// constructors
	
	public CriterionValueVoteSchema(Session session, String schemaName) {
		super(session, schemaName);
	}

	@Override
	protected Column getVoteColumn() {
		return c_vote;
	}

	@Override
	protected Integer getVoteFromRow(Row row) {
		return row.getInt(c_vote.getName());
	}

	@Override
	protected Column[] getListItemsSelectColumns() {
		return new Column[] {c_criterionId, c_vote};
	}

	@Override
	protected Column[] getListItemsWhereColumns() {
		return new Column[] {c_userId, c_entryId};
	}

	@Override
	protected Column getShortIdentifierColumn() {
		return c_criterionId;
	}

	@Override
	protected Object[] getSuperIdentifierValues(
			VoteIdentifier<EntryClassIdentifier> superIdentifier) {
		return new Object[] {
				superIdentifier.getUserIdent().getUserId(),
				superIdentifier.getEvIdent().getEntryClassId()};
	}

	@Override
	protected Column[] getColumns() {
		return new Column[] {c_userId, c_entryId, c_criterionId, c_vote};
	}

	@Override
	protected String getPrimaryKey() {
		return c_userId.getName() + ", " + c_entryId.getName() + ", " + c_criterionId.getName();
	}

	@Override
	protected Column[] getIdentifierColumns() {
		return new Column[] {c_userId, c_entryId, c_criterionId};
	}

	@Override
	protected Object[] getIdentifierValues(
			VoteIdentifier<CriterionIdentifier> identifier) {
		return new Object[] {
				identifier.getUserIdent().getUserId(),
				identifier.getEvIdent().getEntryClassId(), 
				identifier.getEvIdent().getCriterionId()};
	}

	
	
}
