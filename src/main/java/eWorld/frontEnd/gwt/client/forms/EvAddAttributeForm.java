package eWorld.frontEnd.gwt.client.forms;

import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

import eWorld.datatypes.data.EvAttribute;
import eWorld.datatypes.elementars.WoString;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.evs.EvVoid;
import eWorld.datatypes.identifiers.AttributeIdentifier;
import eWorld.datatypes.identifiers.EntryClassIdentifier;
import eWorld.frontEnd.gwt.client.EvApp;
import eWorld.frontEnd.gwt.client.EvAsyncCallback;
import eWorld.frontEnd.gwt.client.EvObserver;

public class EvAddAttributeForm extends EvAddForm<EvAttribute<Ev, AttributeIdentifier>> {

	// attributes
	
	private final EntryClassIdentifier classIdentifier;
	
	
	// components
	
	private TextBox nameInput = new TextBox();
	private TextArea descriptionInput = new TextArea();
	
	
	// constructors
	
	/**
	 * 
	 * @param classIdentifier
	 * @param obsClose the added item or null when the form is just canceled
	 */
	public EvAddAttributeForm(EntryClassIdentifier classIdentifier, EvObserver<EvAttribute<Ev, AttributeIdentifier>> obsClose) {
		super(obsClose);
		
		assert null != classIdentifier;
		
		this.classIdentifier = classIdentifier;
		
		// compose
		super.addToDialogTable("Name", nameInput);
		super.addToDialogTable("Description", descriptionInput);
	}

	
	// methods

	@Override
	protected void confirm() {
		
		super.clearErrorPanel();
		
		String name = nameInput.getValue();
		String description = descriptionInput.getValue();
		
		if (name.isEmpty()) {
			super.addToErrorPanel("Please input a name.");
			return;
		}
		
		EvAttribute<EvVoid, EntryClassIdentifier> attribute = new EvAttribute<EvVoid, EntryClassIdentifier>(
				new EvVoid(),
				classIdentifier,
				new WoString(name),
				new WoString(description),
				null	// author is set by server
				);
		
		EvApp.REQ.addAttribute(attribute, new EvAsyncCallback<EvAttribute<Ev, AttributeIdentifier>>() {
			@Override
			public void onSuccess(EvAttribute<Ev, AttributeIdentifier> result) {
				getCloseObserver().call(result);
			}
		});
	}

}
