package eWorld.frontEnd.gwt.client.forms;

import com.google.gwt.user.client.ui.TextBox;

import eWorld.datatypes.data.EvAttributeValue;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.evs.EvVoid;
import eWorld.datatypes.identifiers.AttributeIdentifier;
import eWorld.datatypes.identifiers.AttributeValueIdentifier;
import eWorld.datatypes.identifiers.AttributeValueShortIdentifier;
import eWorld.frontEnd.gwt.client.EvApp;
import eWorld.frontEnd.gwt.client.EvAsyncCallback;
import eWorld.frontEnd.gwt.client.EvObserver;

public class EvAddAttributeValueForm extends EvAddForm<EvAttributeValue<Ev, AttributeValueIdentifier>> {

	// attributes
	
	private final AttributeIdentifier attributeIdent;
	
	
	// components
	
	private TextBox valueInput = new TextBox();
	
	
	// constructors
	
	/**
	 * 
	 * @param attributeIdent
	 * @param obsClose the added item or null when the form is just canceled
	 */
	public EvAddAttributeValueForm(AttributeIdentifier attributeIdent, 
			EvObserver<EvAttributeValue<Ev, AttributeValueIdentifier>> obsClose) {
		super(obsClose);
		
		assert null != attributeIdent;
		
		this.attributeIdent = attributeIdent;
		
		// compose
		super.addToDialogTable("Value", valueInput);
	}

	
	// methods
	
	@Override
	protected void confirm() {
		
		super.clearErrorPanel();
		
		String value = valueInput.getValue();
		
		if (value.isEmpty()) {
			super.addToErrorPanel("Please input a value.");
			return;
		}
		
		EvAttributeValue<EvVoid, AttributeValueIdentifier> attributeValue = new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
				new EvVoid(),
				new AttributeValueIdentifier(attributeIdent, new AttributeValueShortIdentifier(value)),
				null	// author is set by server
				);
		
		EvApp.REQ.addAttributeValue(attributeValue, new EvAsyncCallback<EvAttributeValue<Ev, AttributeValueIdentifier>>() {
			@Override
			public void onSuccess(EvAttributeValue<Ev, AttributeValueIdentifier> result) {
				getCloseObserver().call(result);
			}
		});
	}

}
