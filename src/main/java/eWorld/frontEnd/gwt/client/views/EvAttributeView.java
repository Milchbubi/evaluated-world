package eWorld.frontEnd.gwt.client.views;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;

import eWorld.datatypes.containers.EvAttributeContainer;
import eWorld.datatypes.containers.IdAttributeTopValueContainer;
import eWorld.datatypes.data.EvAttribute;
import eWorld.datatypes.data.EvEntry;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.identifiers.AttributeIdentifier;
import eWorld.datatypes.identifiers.EntryIdentifier;
import eWorld.frontEnd.gwt.client.EvDialog;
import eWorld.frontEnd.gwt.client.EvObserver;
import eWorld.frontEnd.gwt.client.EvStyle;
import eWorld.frontEnd.gwt.client.Images;
import eWorld.frontEnd.gwt.client.forms.EvAddAttributeForm;
import eWorld.frontEnd.gwt.client.tables.EvAttributeTable;
import eWorld.frontEnd.gwt.client.util.EvImageButton;

public class EvAttributeView extends EvView {

	// attributes
	
	/** contains the attributes that are viewed by eAttributeTable TODO delete, is stored by eAttributeTable*/
	private EvAttributeContainer eAttributeContainer = null;
	
	
	// handlers
	
	private ClickHandler haAddAttribute = new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
			
			// dialog that pops up
			final EvDialog dialog = new EvDialog("Add Attribute");
			
			// the content of the pop up, observer is called when pop up becomes closed (cancel(false) and add(true))
			EvAddAttributeForm addForm = new EvAddAttributeForm(
					eAttributeContainer.getClassIdentifier(), 
					new EvObserver<EvAttribute<Ev, AttributeIdentifier>>() {
				@Override
				public void call(EvAttribute<Ev, AttributeIdentifier> value) {
					dialog.hide();
					
					if (null != value) {
						eAttributeTable.addItem(EvAttribute.shortCast(value));
					}
				}
			});
			
			// initialize dialog
			dialog.setWidget(addForm);
		}
	};
	
	
	// components
	
	private FlowPanel panel = new FlowPanel();
	
	/** button to add attributes */
	private EvImageButton addAttributeButton = new EvImageButton(new Image(Images.add));
	
	private EvViewCaption eAttributeViewCaption;
	
	/** views the attributes that are contained by eAttributeContainer */
	private EvAttributeTable eAttributeTable;
	
	
	// constructors
	
	public EvAttributeView(boolean classOptionsVisible) {
		
		if (classOptionsVisible) {
			eAttributeViewCaption = new EvViewCaption(new Image(Images.network_grey), "Attributes", addAttributeButton);	// TODO change image
		} else {
			eAttributeViewCaption = new EvViewCaption(new Image(Images.network_grey), "Attributes");	// TODO change image
		}
		eAttributeTable = new EvAttributeTable(classOptionsVisible);
		
		// compose
		panel.add(eAttributeViewCaption);
		panel.add(eAttributeTable);
		
		setWidget(panel);
		
		// style
		eAttributeTable.addStyleName(EvStyle.eViewTable);
		addStyleName(EvStyle.eAttributeView);
		
		// handlers
		addAttributeButton.addClickHandler(haAddAttribute);
	}
	
	
	// methods
	
	public void setContainer(EvAttributeContainer eAttributeContainer) {
		assert null != eAttributeContainer;
		
		this.eAttributeContainer = eAttributeContainer;
		
		eAttributeTable.setItemContainer(eAttributeContainer, true);
	}
	
	public void setTopValueContainer(EvEntry<Ev, EntryIdentifier> entry, IdAttributeTopValueContainer eAttributeTopValueContainer) {
		assert null != eAttributeTopValueContainer;
		
		eAttributeTable.setAttributeValueContainer(entry.getName(), eAttributeTopValueContainer, entry.isElement());
	}
	
	
	// overridden methods 
	
	
}
