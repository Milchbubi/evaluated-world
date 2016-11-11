package eWorld.frontEnd.gwt.client.views;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;

import eWorld.datatypes.containers.EvCriterionContainer;
import eWorld.datatypes.containers.IdCriterionValueContainer;
import eWorld.datatypes.data.EvCriterion;
import eWorld.datatypes.data.EvEntry;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.identifiers.CriterionIdentifier;
import eWorld.datatypes.identifiers.EntryIdentifier;
import eWorld.frontEnd.gwt.client.EvDialog;
import eWorld.frontEnd.gwt.client.EvObserver;
import eWorld.frontEnd.gwt.client.EvStyle;
import eWorld.frontEnd.gwt.client.Images;
import eWorld.frontEnd.gwt.client.forms.EvAddCriterionForm;
import eWorld.frontEnd.gwt.client.tables.EvCriterionTable;
import eWorld.frontEnd.gwt.client.tables.EvCriterionTableCanvas;
import eWorld.frontEnd.gwt.client.tables.EvCriterionTableTextual;
import eWorld.frontEnd.gwt.client.util.EvImageButton;

public class EvCriterionView extends EvView {

	// attributes
	
	
	// handlers
	
	private ClickHandler haAddCriterion = new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
			
			// dialog that pops up
			final EvDialog dialog = new EvDialog("Add Criterion");
			
			// the content of the pop up, observer is called when pop up becomes closed (cancel(null) and add(otherwise))
			EvAddCriterionForm addForm = new EvAddCriterionForm(
					eCriterionTable.getItemContainer().getClassIdentifier(), 
					new EvObserver<EvCriterion<Ev, CriterionIdentifier>>() {
				@Override
				public void call(EvCriterion<Ev, CriterionIdentifier> value) {
					dialog.hide();
					
					if (null != value) {
						eCriterionTable.addItem(EvCriterion.shortCast(value));
					}
				}
			});
			
			// initialize dialog
			dialog.setWidget(addForm);
		}
	};
	
	
	// components
	
	private FlowPanel panel = new FlowPanel();
	
	/** button to add criteria */
	private EvImageButton addCriterionButton = new EvImageButton(new Image(Images.add));
	
	private EvViewCaption eCriterionViewCaption;
	
	/** views the criteria */
	private EvCriterionTable eCriterionTable;
	
	
	// constructors
	
	public EvCriterionView(boolean classOptionsVisible) {
		
		if (classOptionsVisible) {
			eCriterionViewCaption = new EvViewCaption(new Image(Images.network_grey), "Criteria", addCriterionButton);	// TODO change image
		} else {
			eCriterionViewCaption = new EvViewCaption(new Image(Images.network_grey), "Criteria");	// TODO change image
		}
		if (Canvas.isSupported()) {
			eCriterionTable = new EvCriterionTableCanvas(classOptionsVisible);
		} else {
			eCriterionTable = new EvCriterionTableTextual(classOptionsVisible);
		}
		
		// compose
		panel.add(eCriterionViewCaption);
		panel.add(eCriterionTable);
		
		setWidget(panel);
		
		// style
		eCriterionTable.addStyleName(EvStyle.eViewTable);
		
		// handlers
		addCriterionButton.addClickHandler(haAddCriterion);
	}
	
	
	// methods
	
	public void setContainer(EvCriterionContainer eCriterionContainer) {
		assert null != eCriterionContainer;
		
		eCriterionTable.setItemContainer(eCriterionContainer, true);
	}
	
	public void setValueContainer(EvEntry<Ev, EntryIdentifier> entry, IdCriterionValueContainer eCriterionValueContainer) {
		assert null != eCriterionValueContainer;
		
		eCriterionTable.setCriterionValueContainer(entry.getName(), eCriterionValueContainer, entry.isElement());
	}
	
	
	// overridden methods 
	
	
}
