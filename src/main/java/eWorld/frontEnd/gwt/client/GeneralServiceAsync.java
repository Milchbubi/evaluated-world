package eWorld.frontEnd.gwt.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import eWorld.datatypes.EvPath;
import eWorld.datatypes.containers.EvAttributeValueContainer;
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

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GeneralServiceAsync {
	
	void register(RegisterUser user, AsyncCallback<Void> callback);
	
	void signIn(SignInUser user, AsyncCallback<User<UserIdentifier>> callback);
	void getSignedInUser(AsyncCallback<User<UserIdentifier>> callback);
	void signOut(AsyncCallback<Void> callback);
	
	// entries
	void getRootEntry(AsyncCallback<EvEntry<Ev, EntryIdentifier>> callback);
	void getStartEntry(AsyncCallback<EvEntry<Ev, EntryIdentifier>> callback);
	void getEntry(EntryShortIdentifier identifier, AsyncCallback<EvEntry<Ev, EntryIdentifier>> callback);
	void getStartEntryPackage(AsyncCallback<EvEntryPackage> callback);
	void getEntryPackage(EntryClassIdentifier identifier, AsyncCallback<EvEntryPackage> callback);
	void addEntry(EvEntry<EvVoid, EntryClassIdentifier> entry, AsyncCallback<EvEntry<Ev, EntryIdentifier>> callback);
	void linkEntry(EntryIdentifier source, EntryClassIdentifier destination, AsyncCallback<EvEntry<Ev, EntryIdentifier>> callback);
	void voteEntry(EntryIdentifier entryIdent, boolean vote, AsyncCallback<Void> callback);
	void getPath(EntryShortIdentifier identifier, AsyncCallback<EvPath> callback);
	void getElement(EntryClassIdentifier classIdentifier, EntryShortIdentifier valueIdentifier, AsyncCallback<EvElement> callback);
	
	// attributes
	void addAttribute(EvAttribute<EvVoid, EntryClassIdentifier> attribute, AsyncCallback<EvAttribute<Ev, AttributeIdentifier>> callback);
	void voteAttribute(AttributeIdentifier attributeIdent, boolean vote, AsyncCallback<Void> callback);
	
	// attributeValues
	void addAttributeValue(EvAttributeValue<EvVoid, AttributeValueIdentifier> attributeValue, AsyncCallback<EvAttributeValue<Ev, AttributeValueIdentifier>> callback);
	void voteAttributeValue(AttributeValueIdentifier attributeValueIdent, boolean vote, AsyncCallback<Void> callback);
	void getAttributeValueContainer(AttributeIdentifier attributeIdent, AsyncCallback<EvAttributeValueContainer> callback);
	
	// media
	void addMedium(EvMedium<EvVoid, EntryClassIdentifier> medium, AsyncCallback<EvMedium<Ev, MediumIdentifier>> callback);
	void voteMedium(MediumIdentifier mediumIdent, boolean vote, AsyncCallback<Void> callback);
	
	// criteria
	void addCriterion(EvCriterion<EvVoid, EntryClassIdentifier> criterion, AsyncCallback<EvCriterion<Ev, CriterionIdentifier>> callback);
	void voteCriterion(CriterionIdentifier criterionIdent, boolean vote, AsyncCallback<Void> callback);
	
	// criterionValues
	void voteCriterionValue(CriterionIdentifier criterionValueIdent, int vote, AsyncCallback<Void> callback);
	
	// comments
	void addComment(EvComment<EvVoid, EntryClassIdentifier> comment, AsyncCallback<EvComment<Ev, CommentIdentifier>> callback);
	void voteComment(CommentIdentifier commentIdent, boolean vote, AsyncCallback<Void> callback);
	
}
