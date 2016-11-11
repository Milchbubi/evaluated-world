package eWorld.frontEnd.gwt.client.tables;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IntegerBox;

import eWorld.datatypes.data.EvCriterion;
import eWorld.datatypes.data.IdCriterionValue;
import eWorld.datatypes.elementars.IntegerVote;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.identifiers.CriterionIdentifier;
import eWorld.datatypes.identifiers.CriterionShortIdentifier;
import eWorld.frontEnd.gwt.client.EvApp;
import eWorld.frontEnd.gwt.client.EvAsyncCallback;
import eWorld.frontEnd.gwt.client.EvInputBox;

public class EvCriterionTableTextual extends EvCriterionTable {

	// column-indices:
	private final int colIndexWorst = getColIndexExtensionStartCriterionTable();
	private final int colIndexBest = getColIndexExtensionStartCriterionTable()+1;
	private final int colIndexCriterionValueVotes = getColIndexExtensionStartCriterionTable()+2;
	private final int colIndexCriterionValueAverage = getColIndexExtensionStartCriterionTable()+3;
	private final int colIndexCriterionValueVote = getColIndexExtensionStartCriterionTable()+4;
	private final int colIndexCriterionValueSubmitVote = getColIndexExtensionStartCriterionTable()+5;
	
	
	// constructors
	
	public EvCriterionTableTextual(boolean eColsVisible) {
		super(eColsVisible);
	}
	
	
	// overridden methods
	
	@Override
	protected void extendCaptionRowCriterion(int row) {
		setText(row, colIndexWorst, "Worst");
		setText(row, colIndexBest, "Best");
	}

	@Override
	protected void extendDataRowCriterion(int row,
			EvCriterion<Ev, CriterionShortIdentifier> criterion) {
		setText(row, colIndexWorst, String.valueOf(criterion.getWorst()));
		setText(row, colIndexBest, String.valueOf(criterion.getBest()));
	}

	@Override
	protected void setCriterionValueCaption(int row) {
		setText(row, colIndexCriterionValueVotes, "Votes");
		setText(row, colIndexCriterionValueAverage, "Average");
		setText(row, colIndexCriterionValueVote, "Your Vote");
	}

	@Override
	public void displayCriterionValueContainer() {
		assert null != getValueItemName();
		assert null != getValueContainer();
		
		// caption
		setCriterionValueCaption(0);
		
		ArrayList<EvCriterion<Ev, CriterionShortIdentifier>> criteria = getItemContainer().getData();
		ArrayList<IdCriterionValue<CriterionShortIdentifier>> criterionValues = getValueContainer().getData();
		
		int searchIndex = 0;
		for (int i = 0; i < criteria.size(); i++) {
			int foundIndex = findMatching(criteria.get(i), searchIndex, criterionValues);
			int row = i +1;
			
			if (-1 < foundIndex) {
				// value does exist
				setCriterionValue(row, criterionValues.get(foundIndex));
				searchIndex = foundIndex +1;
			} else {
				// value does not yet exist
				setCriterionValue(row, null);
			}
		}
	}
	/**
	 * @param row the index of the row where the given criterionValue should be set
	 * @param criterionValue null when criterionValue is not(yet) available
	 */
	private void setCriterionValue(int row,
			IdCriterionValue<CriterionShortIdentifier> criterionValue) {
		
		EvInputBox<IntegerBox> voteBox = new EvInputBox<IntegerBox>(new IntegerBox());
		setWidget(row, colIndexCriterionValueVote, voteBox);
		setWidget(row, colIndexCriterionValueSubmitVote, new Button("vote"));
		
		if (null != criterionValue) {
			// value does exist
			setText(row, colIndexCriterionValueVotes, String.valueOf(criterionValue.getVotes()));
			setText(row, colIndexCriterionValueAverage, String.valueOf(criterionValue.getAverage()));
			IntegerVote vote = criterionValue.getIndividualVote();
			if (null != vote && null != vote.getVote()) {
				voteBox.getInputBox().setText(String.valueOf(vote.getVote()));
			}
		} else {
			// value does not yet exist
			setText(row, colIndexCriterionValueVotes, "-");
			setText(row, colIndexCriterionValueAverage, "-");
		}
	}

	@Override
	protected void rowSelectedCriterionTable(
			EvCriterion<Ev, CriterionShortIdentifier> item, Cell cell, ClickEvent event) {
		
		if (cell.getCellIndex() == colIndexCriterionValueSubmitVote &&
				true == isEvaluable() &&
				null != getValueContainer()) {
			
			final EvInputBox<IntegerBox> voteBox = (EvInputBox<IntegerBox>)this.getWidget(cell.getRowIndex(), colIndexCriterionValueVote);
			assert null != voteBox;
			
			Integer vote = voteBox.getInputBox().getValue();
			
			if (null == vote) {
				voteBox.setLabel("Please input an integer");
			} else {
				
				EvApp.REQ.voteCriterionValue(new CriterionIdentifier(getValueContainer().getClassIdentifier(), item.getIdentifier()), vote, new EvAsyncCallback<Void>() {
					@Override
					public void onSuccess(Void result) {
						voteBox.setLabel("evaluated");
					}
				});
				
			}
		}
	}
	
	
}
