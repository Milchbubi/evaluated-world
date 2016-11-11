package eWorld.frontEnd.gwt.client.tables;

import java.util.ArrayList;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;

import eWorld.datatypes.containers.EvAttributeContainer;
import eWorld.datatypes.containers.IdAttributeTopValueContainer;
import eWorld.datatypes.data.EvAttribute;
import eWorld.datatypes.data.EvAttributeValue;
import eWorld.datatypes.data.IdAttributeTopValue;
import eWorld.datatypes.elementars.WoString;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.identifiers.AttributeIdentifier;
import eWorld.datatypes.identifiers.AttributeShortIdentifier;
import eWorld.datatypes.identifiers.AttributeValueShortIdentifier;
import eWorld.datatypes.identifiers.EntryClassIdentifier;
import eWorld.frontEnd.gwt.client.EvApp;
import eWorld.frontEnd.gwt.client.EvAsyncCallback;
import eWorld.frontEnd.gwt.client.EvNameWidget;
import eWorld.frontEnd.gwt.client.EvObserver;
import eWorld.frontEnd.gwt.client.EvStyle;
import eWorld.frontEnd.gwt.client.Images;
import eWorld.frontEnd.gwt.client.util.EvImageButton;
import eWorld.frontEnd.gwt.client.views.EvAttributeValueView;

public class EvAttributeTable extends EvComparativeTable<
		EntryClassIdentifier,
		AttributeShortIdentifier,
		Long,
		EvAttribute<Ev, AttributeShortIdentifier>,
		EvAttributeContainer,
		IdAttributeTopValue<AttributeShortIdentifier>,
		IdAttributeTopValueContainer
	> {

	// attributes
	
	/** the start-index of the column where attributeValues are displayed */
	private final int colIndexAttributeValueStart = getColIndexExtensionStart() + 2;
	
	/** contains the topValues that are displayed */
	private IdAttributeTopValueContainer topValueContainer = null;
	
	/** true when values can be suggested */
	private boolean addableValues = false;
	
	/** contains all items which value-column is selected (null when false == addableValues) */
	private ArrayList<EvAttribute<Ev, AttributeShortIdentifier>> selectedValueColumns = null;
	
	
	// constructors
	
	public EvAttributeTable(boolean eColsVisible) {
		super(eColsVisible);
		
		// style
		this.addStyleName(EvStyle.eAttributeTable);
	}
	
	
	// methods
	
	/**
	 * TODO use method findMatching(..) from superclass
	 * fills the column(s) for the attributeValues with data
	 * @param itemName name of the item the values belong to
	 * @param topValueContainer contains the values that should be displayed
	 * @param addableValues true when values can be suggested
	 */
	public void setAttributeValueContainer(WoString itemName, IdAttributeTopValueContainer topValueContainer, boolean addableValues) {
		assert null != itemName;
		assert null != topValueContainer;
		
		this.topValueContainer = topValueContainer;
		this.addableValues = addableValues;
		
		if (true == addableValues) {
			selectedValueColumns = new ArrayList<EvAttribute<Ev, AttributeShortIdentifier>>();
		}
		
		// caption
		setText(0, colIndexAttributeValueStart, itemName.getString());
		
		ArrayList<EvAttribute<Ev, AttributeShortIdentifier>> attributes = getItemContainer().getData();
		ArrayList<IdAttributeTopValue<AttributeShortIdentifier>> attributeValues = topValueContainer.getData();
		
		// search for each row(attribute) the matching value and fill it in the right cell
		int lastFoundIndex = 0;
		for (int rowIndex = 1; rowIndex <= attributes.size(); rowIndex++) {
			AttributeShortIdentifier attributeIdentifier = attributes.get(rowIndex -1).getIdentifier();
			
			boolean found = false; // becomes true if matching attributeValue is found
			
			// search from last index to end
			for (int i = lastFoundIndex; i < attributeValues.size(); i++) {
				// TODO redundant to other for-loop, reduce redundancy
				if (attributeIdentifier.getAttributeId() == attributeValues.get(i).getIdentifier().getAttributeId()) {
					// matching value for row found
					setAttributeTopValue(rowIndex, attributeValues.get(i).getValue());
					lastFoundIndex = i;
					found = true;
					break;
				}
			}
			
			// search from beginning to last index
			for (int i = 0; false == found && i < lastFoundIndex; i++) {
				// TODO redundant to other for-loop, reduce redundancy
				if (attributeIdentifier.getAttributeId() == attributeValues.get(i).getIdentifier().getAttributeId()) {
					// matching value for row found
					setAttributeTopValue(rowIndex, attributeValues.get(i).getValue());
					lastFoundIndex = i;
					found = true;
					break;
				}
			}
			
			// no matching attributeValue was found
			if (false == found) {
				setAttributeTopValueEmpty(rowIndex);
			}
			
			// add style to cell
			if (addableValues) {
				getCellFormatter().addStyleName(rowIndex, colIndexAttributeValueStart, EvStyle.eAttributeTableAddValueCell);
			}
		}
	}
	
	private void setAttributeTopValue(int row, String value) {
		assert null != value;
		
		setWidget(row, colIndexAttributeValueStart, new EvCell(value));
	}
	private void setAttributeTopValueEmpty(int row) {
		if (true == addableValues) {
			Image e = new Image(Images.e);
			e.addStyleName(EvStyle.eAttributeTableAddValueCellEmptyEvImage);
			EvImageButton eButton = new EvImageButton(e);
			eButton.addStyleName(EvStyle.eAttributeTableAddValueCellEmptyEvButton);
			setWidget(row, colIndexAttributeValueStart, eButton);
		} else {
			setText(row, colIndexAttributeValueStart, "-");
		}
	}
	
	
	// overridden methods
	
	@Override
	public void addItem(EvAttribute<Ev, AttributeShortIdentifier> item) {
		super.addItem(item);
		int row = getItemContainer().getData().size();	// notice index 0 is reserved for caption
		if (true == addableValues) {
			setAttributeTopValueEmpty(row);
		}
		getCellFormatter().addStyleName(row, colIndexAttributeValueStart, EvStyle.eAttributeTableAddValueCell);
	}
	
	@Override
	protected String getAdditionalRowStyle() {
		return EvStyle.eAttributeTableRow;
	}
	
	@Override
	protected int extendCaptionRow(int row, int startCol) {
		
		int col = startCol;
		
		setText(row, col++, "Attribute");
		
		return col;
	}

	@Override
	protected int extendDataRow(int row, int startCol,
			EvAttribute<Ev, AttributeShortIdentifier> item) {

		int col = startCol;
		
		setWidget(row, col++, new EvNameWidget(item.getName(), item.getDescription()));
		
		return col;
	}

	@Override
	protected void vote(AttributeShortIdentifier shortIdentifier, final boolean vote,
			final Cell cell) {
		assert null != shortIdentifier;
		assert null != cell;
		
		AttributeIdentifier completeIdentifier = new AttributeIdentifier(getItemContainer().getClassIdentifier(), shortIdentifier);
		
		EvApp.REQ.voteAttribute(completeIdentifier, vote, new EvAsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				setRowVoteUpdate(vote, cell);
			}
		});
		
		setRowVoteRequested(vote, cell);
	}

	@Override
	protected void rowSelected(final EvAttribute<Ev, AttributeShortIdentifier> item, final HTMLTable.Cell cell, ClickEvent event) {
		
		if (
				colIndexAttributeValueStart == cell.getCellIndex() && 
				true == addableValues && 
				null != topValueContainer &&
				!selectedValueColumns.contains(item)) {
			// show attributeValues
			
			final EvAttributeValueView valueView = new EvAttributeValueView(
					new AttributeIdentifier(topValueContainer.getClassIdentifier(), item.getIdentifier()),
					cell.getElement().getOffsetWidth()
					);
			
			EvNestedTableView nestedView = new EvNestedTableView(valueView, new EvObserver<Void>() {
				// called when nested view should be closed
				@Override
				public void call(Void value) {
					selectedValueColumns.remove(item);
					getRowFormatter().removeStyleName(cell.getRowIndex(), EvStyle.eAttributeTableRowValueSelected);
					ArrayList<EvAttributeValue<Ev, AttributeValueShortIdentifier>> attributeValueContainer = valueView.get().getData();
					if (!attributeValueContainer.isEmpty()) {
						String attributeTopValue = attributeValueContainer.get(0).getIdentifier().getValue();
						topValueContainer.addItem(new IdAttributeTopValue<AttributeShortIdentifier>(
								item.getIdentifier(),
								attributeTopValue
								));
						setAttributeTopValue(cell.getRowIndex(), attributeTopValue);
					} else {
						setAttributeTopValueEmpty(cell.getRowIndex());
					}
				}
			});
			nestedView.addStyleName(EvStyle.eNestedTableView);
			
			getRowFormatter().addStyleName(cell.getRowIndex(), EvStyle.eAttributeTableRowValueSelected);
			
			FlowPanel placeHolder = new FlowPanel();	// avoids width-changes of the cell
			placeHolder.add(nestedView);
			placeHolder.add(getWidget(cell.getRowIndex(), colIndexAttributeValueStart));	// what happens if getWidget(..) is null?
			setWidget(cell.getRowIndex(), colIndexAttributeValueStart, placeHolder);
			
			selectedValueColumns.add(item);
		}
		
	}

}
