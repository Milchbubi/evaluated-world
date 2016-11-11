package eWorld.frontEnd.gwt.client.tables;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IntegerBox;

import eWorld.datatypes.containers.EvCriterionContainer;
import eWorld.datatypes.containers.IdCriterionValueContainer;
import eWorld.datatypes.data.EvAttribute;
import eWorld.datatypes.data.EvCriterion;
import eWorld.datatypes.data.IdAttributeTopValue;
import eWorld.datatypes.data.IdCriterionValue;
import eWorld.datatypes.elementars.IntegerVote;
import eWorld.datatypes.elementars.WoString;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.identifiers.AttributeShortIdentifier;
import eWorld.datatypes.identifiers.CriterionIdentifier;
import eWorld.datatypes.identifiers.CriterionShortIdentifier;
import eWorld.datatypes.identifiers.EntryClassIdentifier;
import eWorld.frontEnd.gwt.client.EvApp;
import eWorld.frontEnd.gwt.client.EvAsyncCallback;
import eWorld.frontEnd.gwt.client.EvInputBox;
import eWorld.frontEnd.gwt.client.EvNameWidget;
import eWorld.frontEnd.gwt.client.EvStyle;

public abstract class EvCriterionTable extends EvComparativeTable<
		EntryClassIdentifier,
		CriterionShortIdentifier,
		Long,
		EvCriterion<Ev, CriterionShortIdentifier>,
		EvCriterionContainer,
		IdCriterionValue<CriterionShortIdentifier>,
		IdCriterionValueContainer
	> {
	
	// column-indices:
	protected final int colIndexName = getColIndexExtensionStart();
	
	/** the name of the entry or element which values are displayed and can be evaluated */
	private WoString valueItemName = null;
	
	/** contains the values that should be displayed */
	private IdCriterionValueContainer valueContainer = null;
	
	/** true when criteria can be evaluated */
	private boolean evaluable = false;
	
	
	// constructors
	
	public EvCriterionTable(boolean eColsVisible) {
		super(eColsVisible);
	}
	
	
	// methods
	
	/**
	 * 
	 * @return the start-index of the column where subclasses put additional data in
	 */
	protected int getColIndexExtensionStartCriterionTable() {
		return getColIndexExtensionStart()+1;
	}
	
	public WoString getValueItemName() {
		return valueItemName;
	}
	
	public IdCriterionValueContainer getValueContainer() {
		return valueContainer;
	}
	
	public boolean isEvaluable() {
		return evaluable;
	}
	
	/**
	 * 
	 * @param itemName name of the item the values belong to
	 * @param valueContainer contains the values that should be displayed
	 * @param evaluable true when criteria can be evaluated
	 */
	public void setCriterionValueContainer(WoString itemName, IdCriterionValueContainer valueContainer, boolean evaluable) {
		assert null != itemName;
		assert null != valueContainer;
		
		this.valueItemName = itemName;
		this.valueContainer = valueContainer;
		this.evaluable = evaluable;
		
		displayCriterionValueContainer();
	}
	
	
	// abstract methods
	
	/**
	 * 
	 * @param row the index of the captionRow (in most cases 0)
	 */
	protected abstract void extendCaptionRowCriterion(int row);
	
	/**
	 * 
	 * @param row the index of the row where the given criterion should be set
	 * @param criterion
	 */
	protected abstract void extendDataRowCriterion(int row, EvCriterion<Ev, CriterionShortIdentifier> criterion);
	
	/**
	 * 
	 * @param row the index of the captionRow (in most cases 0)
	 */
	protected abstract void setCriterionValueCaption(int row);
	
	/**
	 * displays valueContainer
	 */
	protected abstract void displayCriterionValueContainer();
	
	/**
	 * called when a row becomes selected and the selected cell is not handled by superclass
	 * @param item the item that is displayed in the selected row
	 * @param cell the cell that is selected, can be used f.e. to get columnIndex|cellIndex
	 * @param event can be used to get f.e. clickPosition
	 */
	protected abstract void rowSelectedCriterionTable(EvCriterion<Ev, CriterionShortIdentifier> item, Cell cell, ClickEvent event);
	
	// overridden methods
	
	@Override
	protected String getAdditionalRowStyle() {
		return EvStyle.eCriterionTableRow;
	}

	@Override
	protected int extendCaptionRow(int row, int startCol) {
		
		setText(row, colIndexName, "Criterion");
		
		extendCaptionRowCriterion(row);
		
		return startCol+3;
	}

	@Override
	protected int extendDataRow(int row, int startCol,
			EvCriterion<Ev, CriterionShortIdentifier> item) {
		
		setWidget(row, colIndexName, new EvNameWidget(item.getName(), item.getDescription()));
		
		extendDataRowCriterion(row, item);
		
		return startCol+3;
	}

	@Override
	protected void vote(CriterionShortIdentifier shortIdentifier, final boolean vote,
			final Cell cell) {
		assert null != shortIdentifier;
		assert null != cell;
		
		CriterionIdentifier completeIdentifier = new CriterionIdentifier(getItemContainer().getClassIdentifier(), shortIdentifier);
		
		EvApp.REQ.voteCriterion(completeIdentifier, vote, new EvAsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				setRowVoteUpdate(vote, cell);
			}
		});
		
		setRowVoteRequested(vote, cell);
	}

	@Override
	protected void rowSelected(EvCriterion<Ev, CriterionShortIdentifier> item, Cell cell, ClickEvent event) {
		
		rowSelectedCriterionTable(item, cell, event);
	}

}
