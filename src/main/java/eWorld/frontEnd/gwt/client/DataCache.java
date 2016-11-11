package eWorld.frontEnd.gwt.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import eWorld.datatypes.EvPath;
import eWorld.datatypes.containers.EvAttributeContainer;
import eWorld.datatypes.containers.EvAttributeValueContainer;
import eWorld.datatypes.containers.EvEntryContainer;
import eWorld.datatypes.data.EvAttribute;
import eWorld.datatypes.data.EvAttributeValue;
import eWorld.datatypes.data.EvComment;
import eWorld.datatypes.data.EvCriterion;
import eWorld.datatypes.data.EvEntry;
import eWorld.datatypes.data.EvMedium;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.evs.EvVoid;
import eWorld.datatypes.identifiers.AttributeIdentifier;
import eWorld.datatypes.identifiers.AttributeValueIdentifier;
import eWorld.datatypes.identifiers.CommentIdentifier;
import eWorld.datatypes.identifiers.CriterionIdentifier;
import eWorld.datatypes.identifiers.EntryClassIdentifier;
import eWorld.datatypes.identifiers.EntryIdentifier;
import eWorld.datatypes.identifiers.EntryShortIdentifier;
import eWorld.datatypes.identifiers.MediumIdentifier;
import eWorld.datatypes.identifiers.UserIdentifier;
import eWorld.datatypes.packages.EvElement;
import eWorld.datatypes.packages.EvEntryPackage;
import eWorld.datatypes.user.RegisterUser;
import eWorld.datatypes.user.SignInUser;
import eWorld.datatypes.user.User;

public class DataCache {
	
	/** Create a remote service proxy to talk to the server-side GeneralService. */
	private final GeneralServiceAsync generalService = GWT.create(GeneralService.class);
	
	/** stores the user that is signed in, null if not signed in */
	private User<UserIdentifier> signedInUser = null;
	
	public void register(RegisterUser user, EvAsyncCallback<Void> callback) {
		generalService.register(user, callback);
	}
	
	public void signIn(SignInUser user, final EvAsyncCallback<User<UserIdentifier>> callback) {
		generalService.signIn(user, new EvAsyncCallback<User<UserIdentifier>>() {
			@Override
			public void onSuccess(User<UserIdentifier> result) {
				signedInUser = result;
				callback.onSuccess(result);
			}
		});
	}
	
	public void getSignedInUser(final EvAsyncCallback<User<UserIdentifier>> callback) {
		generalService.getSignedInUser(new EvAsyncCallback<User<UserIdentifier>>() {
			@Override
			public void onSuccess(User<UserIdentifier> result) {
				signedInUser = result;
				callback.onSuccess(result);
			}
		});
	}
	
	public void signOut(final EvAsyncCallback<Void> callback) {
		generalService.signOut(new EvAsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				signedInUser = null;
				callback.onSuccess(result);
			}
		});
	}
	
	public void getRootEntry(EvAsyncCallback<EvEntry<Ev, EntryIdentifier>> callback) {
		generalService.getRootEntry(callback);
	}
	
	public void getStartEntry(EvAsyncCallback<EvEntry<Ev, EntryIdentifier>> callback) {
		generalService.getStartEntry(callback);
	}
	
	public void getEntry(EntryShortIdentifier identifier, EvAsyncCallback<EvEntry<Ev, EntryIdentifier>> callback) {
		generalService.getEntry(identifier, callback);
	}
	
	@Deprecated
	public void getStartEntryPackage(EvAsyncCallback<EvEntryPackage> callback) {
		generalService.getStartEntryPackage(callback);
	}
	
	public void getEntryPackage(EntryClassIdentifier identifier, EvAsyncCallback<EvEntryPackage> callback) {
		getEntryPackage(identifier, false, callback);
	}
	public void getEntryPackage(EntryClassIdentifier identifier, boolean force, EvAsyncCallback<EvEntryPackage> callback) {
		// TODO use the force not anytime
		generalService.getEntryPackage(identifier, callback);
	}

	public void addEntry(EvEntry<EvVoid, EntryClassIdentifier> entry, EvAsyncCallback<EvEntry<Ev, EntryIdentifier>> callback) {
		generalService.addEntry(entry, callback);
	}
	
	public void linkEntry(EntryIdentifier source, EntryClassIdentifier destination, EvAsyncCallback<EvEntry<Ev, EntryIdentifier>> callback) {
		generalService.linkEntry(source, destination, callback);
	}
	
	public void voteEntry(EntryIdentifier entryIdent, boolean vote, EvAsyncCallback<Void> callback) {
		// TODO cache them if often voted and send a list to the server
		generalService.voteEntry(entryIdent, vote, callback);
	}
	
	public void getPath(EntryShortIdentifier identifier, EvAsyncCallback<EvPath> callback) {
		generalService.getPath(identifier, callback);
	}
	
	public void getElement(EntryClassIdentifier classIdentifier, EntryShortIdentifier valueIdentifier, EvAsyncCallback<EvElement> callback) {
		getElement(classIdentifier, valueIdentifier, false, callback);
	}
	public void getElement(EntryClassIdentifier classIdentifier, EntryShortIdentifier valueIdentifier, boolean force, EvAsyncCallback<EvElement> callback) {
		// TODO use the force not anytime
		generalService.getElement(classIdentifier, valueIdentifier, callback);
	}

	public void addAttribute(EvAttribute<EvVoid, EntryClassIdentifier> attribute, EvAsyncCallback<EvAttribute<Ev, AttributeIdentifier>> callback) {
		generalService.addAttribute(attribute, callback);
	}
	
	public void voteAttribute(AttributeIdentifier attributeIdent, boolean vote, EvAsyncCallback<Void> callback) {
		// TODO cache them if often voted and send a list to the server
		generalService.voteAttribute(attributeIdent, vote, callback);
	}
	
	public void addAttributeValue(EvAttributeValue<EvVoid, AttributeValueIdentifier> attributeValue, EvAsyncCallback<EvAttributeValue<Ev, AttributeValueIdentifier>> callback) {
		generalService.addAttributeValue(attributeValue, callback);
	}
	
	public void voteAttributeValue(AttributeValueIdentifier attributeValueIdent, boolean vote, EvAsyncCallback<Void> callback) {
		// TODO cache them if often voted and send a list to the server
		generalService.voteAttributeValue(attributeValueIdent, vote, callback);
	}
	
	public void getAttributeValueContainer(AttributeIdentifier attributeIdent, EvAsyncCallback<EvAttributeValueContainer> callback) {
		getAttributeValueContainer(attributeIdent, false, callback);
	}
	public void getAttributeValueContainer(AttributeIdentifier attributeIdent, boolean force, EvAsyncCallback<EvAttributeValueContainer> callback) {
		// TODO use the force not anytime
		generalService.getAttributeValueContainer(attributeIdent, callback);
	}
	
	public void addMedium(EvMedium<EvVoid, EntryClassIdentifier> medium, EvAsyncCallback<EvMedium<Ev, MediumIdentifier>> callback){
		generalService.addMedium(medium, callback);
	}
	
	public void voteMedium(MediumIdentifier mediumIdent, boolean vote, EvAsyncCallback<Void> callback){
		// TODO cache them if often voted and send a list to the server
		generalService.voteMedium(mediumIdent, vote, callback);
	}
	
	public void addCriterion(EvCriterion<EvVoid, EntryClassIdentifier> criterion, EvAsyncCallback<EvCriterion<Ev, CriterionIdentifier>> callback) {
		generalService.addCriterion(criterion, callback);
	}
	
	public void voteCriterion(CriterionIdentifier criterionIdent, boolean vote, EvAsyncCallback<Void> callback) {
		// TODO cache them if often voted and send a list to the server
		generalService.voteCriterion(criterionIdent, vote, callback);
	}
	
	public void voteCriterionValue(CriterionIdentifier criterionValueIdent, int vote, EvAsyncCallback<Void> callback) {
		// TODO cache them if often voted and send a list to the server
		generalService.voteCriterionValue(criterionValueIdent, vote, callback);
	}
	
	public void addComment(EvComment<EvVoid, EntryClassIdentifier> comment, EvAsyncCallback<EvComment<Ev, CommentIdentifier>> callback){
		generalService.addComment(comment, callback);
	}
	
	public void voteComment(CommentIdentifier commentIdent, boolean vote, EvAsyncCallback<Void> callback){
		// TODO cache them if often voted and send a list to the server
		generalService.voteComment(commentIdent, vote, callback);
	}
}
