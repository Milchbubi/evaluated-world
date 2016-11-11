package eWorld.frontEnd.gwt.client.views;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;

import eWorld.datatypes.containers.EvAttributeValueContainer;
import eWorld.datatypes.data.EvAttributeValue;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.identifiers.AttributeIdentifier;
import eWorld.datatypes.identifiers.AttributeValueIdentifier;
import eWorld.frontEnd.gwt.client.EvApp;
import eWorld.frontEnd.gwt.client.EvAsyncCallback;
import eWorld.frontEnd.gwt.client.EvDialog;
import eWorld.frontEnd.gwt.client.EvObserver;
import eWorld.frontEnd.gwt.client.EvStyle;
import eWorld.frontEnd.gwt.client.Images;
import eWorld.frontEnd.gwt.client.forms.EvAddAttributeValueForm;
import eWorld.frontEnd.gwt.client.tables.EvAttributeValueTable;
import eWorld.frontEnd.gwt.client.util.EvImageButton;

public class EvAttributeValueView extends EvView {

	// attributes
	
	/** contains the attributeValues that are viewed by eAttributeValueTable */
	private EvAttributeValueContainer eAttributeValueContainer = null;
	

	// handlers
	
	private ClickHandler haAddAttributeValue = new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
			
			// dialog that pops up
			final EvDialog dialog = new EvDialog("Add AttributeValue");
			
			// the content of the pop up, observer is called when pop up becomes closed (cancel(null) and add(!null))
			EvAddAttributeValueForm addForm = new EvAddAttributeValueForm(
					eAttributeValueContainer.getClassIdentifier(), 
					new EvObserver<EvAttributeValue<Ev, AttributeValueIdentifier>>() {
				@Override
				public void call(EvAttributeValue<Ev, AttributeValueIdentifier> value) {
					dialog.hide();
					
					if (null != value) {
						eAttributeValueTable.addItem(EvAttributeValue.shortCast(value));
					}
				}
			});
			
			// initialize dialog
			dialog.setWidget(addForm);
		}
	};
	
	
	// callbacks
	
	EvAsyncCallback<EvAttributeValueContainer> cbLoad = new EvAsyncCallback<EvAttributeValueContainer>() {
		@Override
		public void onSuccess(EvAttributeValueContainer result) {
			assert null != result;
			
			set(result);
		}
	};
	
	
	// components
	
	private FlowPanel panel = new FlowPanel();
	
	/** button to add attributeValues */
	private EvImageButton addAttributeValueButton = new EvImageButton(new Image(Images.add));
	
	/** views the attributeValues that are contained by eAttributeValueContainer */
	private EvAttributeValueTable eAttributeValueTable;
	
	
	// constructors
	
	/**
	 * 
	 * @param identifier
	 * @param valueColumnWidth the width of the valueColumn in pixel
	 */
	public EvAttributeValueView(AttributeIdentifier identifier, int valueColumnWidth) {
		assert null != identifier;
		
		eAttributeValueTable = new EvAttributeValueTable(valueColumnWidth);
		
		// load attributeValues
		EvApp.REQ.getAttributeValueContainer(identifier, cbLoad);
		
		// compose
		panel.add(addAttributeValueButton);
		panel.add(eAttributeValueTable);
		setWidget(panel);
		
		// style
		addAttributeValueButton.addStyleName(EvStyle.eAttributeValueViewAddButton);
		
		// handlers
		addAttributeValueButton.addClickHandler(haAddAttributeValue);
	}
	
	
	// methods
	
	private void set(EvAttributeValueContainer eAttributeValueContainer) {
		assert null != eAttributeValueContainer;
		
		this.eAttributeValueContainer = eAttributeValueContainer;
		
		eAttributeValueTable.setItemContainer(eAttributeValueContainer, true);
		
	}

	public EvAttributeValueContainer get() {
		return eAttributeValueContainer;
	}
}
