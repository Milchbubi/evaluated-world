package eWorld.frontEnd.gwt.client.views;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import eWorld.datatypes.containers.IdAttributeTopValueContainer;
import eWorld.datatypes.containers.IdCriterionValueContainer;
import eWorld.datatypes.identifiers.EntryClassIdentifier;
import eWorld.datatypes.identifiers.EntryIdentifier;
import eWorld.datatypes.identifiers.EntryShortIdentifier;
import eWorld.datatypes.packages.EvElement;
import eWorld.datatypes.packages.EvPackage;
import eWorld.frontEnd.gwt.client.EvApp;
import eWorld.frontEnd.gwt.client.EvAsyncCallback;
import eWorld.frontEnd.gwt.client.EvStyle;

public abstract class EvOrdinaryElementView extends EvNaviPackageView {

	// attributes
	
	/** specifies the element that is loaded and displayed */
	private EntryIdentifier identifier;
	
	/** contains the attributes that are viewed by eAttributeTable */
	private EvElement eElement = null;
	
	
	// handlers
	
	
	// callbacks
	
	EvAsyncCallback<EvElement> cbLoad = new EvAsyncCallback<EvElement>() {
		@Override
		public void onSuccess(EvElement result) {
			assert null != result;
			
			setEvElement(result);
		}
	};
	
	
	// components
	
	private VerticalPanel panel = new VerticalPanel();
	
	private HorizontalPanel topPanel = new HorizontalPanel();
	
	private EvInfoView eInfoView = new EvInfoView();
	
	private FlowPanel viewsPanel = new FlowPanel();
	
//	private HorizontalPanel containerViewsPanel1 = new HorizontalPanel();
	
	private EvMediumView mediumView = new EvMediumView();
	
	private EvAttributeView eAttributeView;
	
//	private HorizontalPanel containerViewsPanel2 = new HorizontalPanel();
	
	private EvCriterionView eCriterionView;
	
	private EvCommentView eCommentView = new EvCommentView();
	
	
	// constructors
	
	public EvOrdinaryElementView(EntryIdentifier identifier, boolean classOptionsVisible) {
		assert null != identifier;
		
		this.identifier = identifier;
		
		eAttributeView = new EvAttributeView(classOptionsVisible);
		eCriterionView = new EvCriterionView(classOptionsVisible);
		
		// load element
		EvApp.REQ.getElement(
				getCurrentSuperClassIdent(), 
				new EntryShortIdentifier(identifier.getEntryId()), 
				cbLoad);
		
		// compose
		topPanel.add(eInfoView);
		
		viewsPanel.add(mediumView);
		viewsPanel.add(eAttributeView);
		viewsPanel.add(eCriterionView);
		viewsPanel.add(eCommentView);
		
		panel.add(topPanel);
		panel.add(viewsPanel);
		
		setWidget(panel);
		
		// style
		topPanel.addStyleName(EvStyle.eNaviPackageViewTopPanel);
		mediumView.addStyleName(EvStyle.eElementViewCompactView);
//		eCommentView.addStyleName(EvStyle.eElementViewCompactView);
		eAttributeView.addStyleName(EvStyle.eElementViewCompactView);
		viewsPanel.addStyleName(EvStyle.eElementViewViewsPanel);
		panel.addStyleName(EvStyle.eNaviPackageViewPanel);
		
		// handlers
		
	}
	
	
	// methods
	
	protected EntryIdentifier getIdentifier() {
		return identifier;
	}
	
	private void setEvElement(EvElement eElement) {
		assert null != eElement;
		
		this.eElement = eElement;
		
		eInfoView.set(eElement.getHeader());
		
		mediumView.setContainer(eElement.getMediumContainer());
		eAttributeView.setContainer(eElement.getAttributeContainer());
		eCriterionView.setContainer(eElement.getCriterionContainer());
		eCommentView.setContainer(eElement.getCommentContainer(), eElement.getHeader().getName().getString());
		
		IdAttributeTopValueContainer attributeTopValueContainer = eElement.getAttributeValueContainers().get(0);
		assert null != attributeTopValueContainer;
		if (null != attributeTopValueContainer && (eElement.getHeader().isElement() || !attributeTopValueContainer.getData().isEmpty())) {
			eAttributeView.setTopValueContainer(eElement.getHeader(), attributeTopValueContainer);
		}
		
		IdCriterionValueContainer criterionValueContainer = eElement.getCriterionValueContainers().get(0);
		assert null != criterionValueContainer;
		if (null != criterionValueContainer && (eElement.getHeader().isElement() || !criterionValueContainer.getData().isEmpty())) {
			eCriterionView.setValueContainer(eElement.getHeader(), criterionValueContainer);
		}
	}
	
	
	// overridden methods
	
	@Override
	public EntryClassIdentifier getCurrentClassIdent() {
		if (null != eElement) {
			return new EntryClassIdentifier(eElement.getHeader().getIdentifier().getEntryId());
		} else {
			return null;
		}
	}
	
	@Override
	public EvPackage getDataPackage() {
		return eElement;
	}

	@Override
	public void update() {
		assert null != eElement;
		
		if (null != eElement) {
			EvApp.REQ.getElement(
					getCurrentSuperClassIdent(), 
					new EntryShortIdentifier(eElement.getHeader().getIdentifier().getEntryId()), 
					cbLoad);
		}
	}

}
