package eWorld.frontEnd.gwt.client.tables;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.HTMLTable;

import eWorld.datatypes.containers.EvAttributeValueContainer;
import eWorld.datatypes.data.EvAttributeValue;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.identifiers.AttributeIdentifier;
import eWorld.datatypes.identifiers.AttributeValueIdentifier;
import eWorld.datatypes.identifiers.AttributeValueShortIdentifier;
import eWorld.frontEnd.gwt.client.EvApp;
import eWorld.frontEnd.gwt.client.EvAsyncCallback;
import eWorld.frontEnd.gwt.client.EvStyle;

public class EvAttributeValueTable extends EvTable<
		AttributeIdentifier,
		AttributeValueShortIdentifier,
		String,
		EvAttributeValue<Ev, AttributeValueShortIdentifier>,
		EvAttributeValueContainer
	> {

	// constructors
	
	/**
	 * 
	 * @param valueColumnWidth the width of the valueColumn in pixel
	 */
	public EvAttributeValueTable(int valueColumnWidth) {
		super(4, 3, 1, 2, 0);
		
		// style
		this.addStyleName(EvStyle.eAttributeValueTable);
		this.getColumnFormatter().getElement(getColIndexValue()).getStyle().setWidth(valueColumnWidth, Style.Unit.PX);
	}
	
	
	// methods
	
	protected int getColIndexValue() {
		return 0;
	}
	
	
	// overridden methods
	
	@Override
	protected String getAdditionalRowStyle() {
		return EvStyle.eAttributeValueTableRow;
	}
	
	@Override
	protected int extendCaptionRow(int row, int startCol) {		
		setText(row, getColIndexValue(), "Value");
		
		return startCol+1;
	}

	@Override
	protected int extendDataRow(int row, int startCol,
			EvAttributeValue<Ev, AttributeValueShortIdentifier> item) {
		setText(row, getColIndexValue(), item.getIdentifier().getShortId());
		
		return startCol+1;
	}

	@Override
	protected void vote(AttributeValueShortIdentifier shortIdentifier,
			final boolean vote, final Cell cell) {
		assert null != shortIdentifier;
		assert null != cell;
		
		AttributeValueIdentifier completeIdentifier = new AttributeValueIdentifier(getItemContainer().getClassIdentifier(), shortIdentifier);
		
		EvApp.REQ.voteAttributeValue(completeIdentifier, vote, new EvAsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				setRowVoteUpdate(vote, cell);
			}
		});
		
		setRowVoteRequested(vote, cell);
	}

	@Override
	protected void rowSelected(
			EvAttributeValue<Ev, AttributeValueShortIdentifier> item, HTMLTable.Cell cell, ClickEvent event) {
		// atm nothing
	}

}
