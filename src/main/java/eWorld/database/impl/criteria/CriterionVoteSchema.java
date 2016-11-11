package eWorld.database.impl.criteria;

import com.datastax.driver.core.Session;

import eWorld.database.impl.BooleanVoteSchema;
import eWorld.database.impl.Column;
import eWorld.database.impl.DB;
import eWorld.database.impl.IdGenerator;
import eWorld.datatypes.identifiers.CriterionIdentifier;
import eWorld.datatypes.identifiers.CriterionShortIdentifier;
import eWorld.datatypes.identifiers.EntryClassIdentifier;
import eWorld.datatypes.identifiers.VoteIdentifier;

public class CriterionVoteSchema extends BooleanVoteSchema<
		VoteIdentifier<CriterionIdentifier>,
		VoteIdentifier<EntryClassIdentifier>,
		CriterionShortIdentifier,
		CriterionIdentifier,
		EntryClassIdentifier
	> {

	// static finals
	
	protected static final Column c_entryClassId = new Column("entryClassId", IdGenerator.idType);
	
	protected static final Column c_criterionId = new Column("criterionId", IdGenerator.idType);
	
	
	// constructors
	
	public CriterionVoteSchema(Session session, String schemaName) {
		super(session, schemaName);
	}

	
	// methods
	
	
	// overridden methods
	
	@Override
	protected Column[] getListItemsSelectColumns() {
		return new Column[] {c_criterionId, c_vote};
	}

	@Override
	protected Column[] getListItemsWhereColumns() {
		return new Column[] {c_userId, c_entryClassId};
	}

	@Override
	protected Column getShortIdentifierColumn() {
		return c_criterionId;
	}

	@Override
	protected Object[] getSuperIdentifierValues(
			VoteIdentifier<EntryClassIdentifier> superIdentifier) {
		return new Long[] {superIdentifier.getUserIdent().getUserId(), superIdentifier.getEvIdent().getEntryClassId()};
	}

	@Override
	protected Column[] getColumns() {
		return new Column[] {c_userId, c_entryClassId, c_criterionId, c_vote};
	}

	@Override
	protected String getPrimaryKey() {
		return c_userId.getName() + ", " + c_entryClassId.getName() + ", " + c_criterionId.getName();
	}

	@Override
	protected Column[] getIdentifierColumns() {
		return new Column[] {c_userId, c_entryClassId, c_criterionId};
	}

	@Override
	protected Object[] getIdentifierValues(
			VoteIdentifier<CriterionIdentifier> identifier) {
		return new Long[] {identifier.getUserIdent().getUserId(),
				identifier.getEvIdent().getEntryClassId(), identifier.getEvIdent().getCriterionId()};
	}

}
