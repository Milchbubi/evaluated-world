package eWorld.frontEnd.gwt.client.forms;

import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

import eWorld.datatypes.data.EvCriterion;
import eWorld.datatypes.elementars.WoString;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.evs.EvVoid;
import eWorld.datatypes.identifiers.CriterionIdentifier;
import eWorld.datatypes.identifiers.EntryClassIdentifier;
import eWorld.frontEnd.gwt.client.EvApp;
import eWorld.frontEnd.gwt.client.EvAsyncCallback;
import eWorld.frontEnd.gwt.client.EvObserver;

public class EvAddCriterionForm extends EvAddForm<EvCriterion<Ev, CriterionIdentifier>> {

	// attributes
	
	private final EntryClassIdentifier classIdentifier;
	
	
	// components
	
	private TextBox nameInput = new TextBox();
	private TextArea descriptionInput = new TextArea();
	private IntegerBox worstInput = new IntegerBox();
	private IntegerBox bestInput = new IntegerBox();
	
	
	// constructors
	
	/**
	 * 
	 * @param classIdentifier
	 * @param obsClose the added item or null when the form is just canceled
	 */
	public EvAddCriterionForm(EntryClassIdentifier classIdentifier, EvObserver<EvCriterion<Ev, CriterionIdentifier>> obsClose) {
		super(obsClose);
		
		assert null != classIdentifier;
		
		this.classIdentifier = classIdentifier;
		
		// predefine input
		worstInput.setValue(1);
		bestInput.setValue(10);
		
		// compose
		super.addToDialogTable("Name", nameInput);
		super.addToDialogTable("Description", descriptionInput);
		super.addToDialogTable("Worst", worstInput);
		super.addToDialogTable("Best", bestInput);
	}

	
	// methods
	
	@Override
	protected void confirm() {
		
		super.clearErrorPanel();
		
		String name = nameInput.getValue();
		String description = descriptionInput.getValue();
		Integer worst = worstInput.getValue();
		Integer best = bestInput.getValue();
		
		if (name.isEmpty()) {
			super.addToErrorPanel("Please input a name.");
			return;
		}
		if (null == worst) {
			super.addToErrorPanel("Please input the worst value the criterion can be evaluated with as integer");
			return;
		}
		if (null == best) {
			super.addToErrorPanel("Please input the best value the criterion can be evaluated with as integer");
			return;
		}
		
		EvCriterion<EvVoid, EntryClassIdentifier> criterion = new EvCriterion<EvVoid, EntryClassIdentifier>(
				new EvVoid(),
				classIdentifier,
				new WoString(name),
				new WoString(description),
				worst,
				best,
				null	// author is set by server
				);
		
		EvApp.REQ.addCriterion(criterion, new EvAsyncCallback<EvCriterion<Ev, CriterionIdentifier>>() {
			@Override
			public void onSuccess(EvCriterion<Ev, CriterionIdentifier> result) {
				getCloseObserver().call(result);
			}
		});
	}

}
