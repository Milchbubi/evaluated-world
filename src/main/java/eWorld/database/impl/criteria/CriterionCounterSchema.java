package eWorld.database.impl.criteria;

import com.datastax.driver.core.Session;

import eWorld.database.impl.Column;
import eWorld.database.impl.DB;
import eWorld.database.impl.IdGenerator;
import eWorld.database.impl.UpDownCounterSchema;
import eWorld.datatypes.identifiers.CriterionIdentifier;
import eWorld.datatypes.identifiers.CriterionShortIdentifier;
import eWorld.datatypes.identifiers.EntryClassIdentifier;

public class CriterionCounterSchema extends UpDownCounterSchema<
		CriterionIdentifier,
		EntryClassIdentifier,
		CriterionShortIdentifier
	> {

	// static finals
	
	protected static final Column c_entryClassId = new Column("entryClassId", IdGenerator.idType);
	
	protected static final Column c_criterionId = new Column("criterionId", IdGenerator.idType);
	
	
	// constructors
	
	public CriterionCounterSchema(Session session, String schemaName) {
		super(session, schemaName);
	}

	
	// methods
	
	
	// overridden methods
	
	@Override
	protected Column[] getListItemsSelectColumns() {
		return new Column[] {c_criterionId, c_up, c_down};
	}

	@Override
	protected Column[] getListItemsWhereColumns() {
		return new Column[] {c_entryClassId};
	}

	@Override
	protected Column getShortIdentifierColumn() {
		return c_criterionId;
	}

	@Override
	protected Object[] getSuperIdentifierValues(
			EntryClassIdentifier superIdentifier) {
		return new Long[] {superIdentifier.getEntryClassId()};
	}

	@Override
	protected Column[] getColumns() {
		return new Column[] {c_entryClassId, c_criterionId, c_up, c_down};
	}

	@Override
	protected String getPrimaryKey() {
		return c_entryClassId.getName() + ", " + c_criterionId.getName();
	}

	@Override
	protected Column[] getIdentifierColumns() {
		return new Column[] {c_entryClassId, c_criterionId};
	}

	@Override
	protected Object[] getIdentifierValues(CriterionIdentifier identifier) {
		return new Long[] {identifier.getEntryClassId(), identifier.getCriterionId()};
	}

}
