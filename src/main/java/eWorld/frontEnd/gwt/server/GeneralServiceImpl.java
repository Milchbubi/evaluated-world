package eWorld.frontEnd.gwt.server;

import javax.servlet.http.HttpSession;

import eWorld.database.ApacheCassandraGate;
import eWorld.database.Gate;
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
import eWorld.datatypes.exceptions.EvIllegalDataException;
import eWorld.datatypes.exceptions.EvRequestException;
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
import eWorld.datatypes.user.AdvancedUser;
import eWorld.datatypes.user.RegisterUser;
import eWorld.datatypes.user.SignInUser;
import eWorld.datatypes.user.User;
import eWorld.frontEnd.gwt.client.GeneralService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GeneralServiceImpl extends RemoteServiceServlet implements
		GeneralService {
	
	// attributes
	
	private static final String DATABASE_NODE = "127.0.0.1";
	private static final Gate DB_GATE = new ApacheCassandraGate(DATABASE_NODE);
	
	private static final String SESSION_ATTRIBUTE_ADVANCED_USER = "advancedUser";
	
	
	// methods
	
	/**
	 * prefer {@code getAdvancedUserOrThrow()}
	 * @return the user associated with the request or null if user is not signed in
	 */
	private AdvancedUser getAdvancedUserOrNull() {
		HttpSession session = getThreadLocalRequest().getSession();
		
		return (AdvancedUser)session.getAttribute(SESSION_ATTRIBUTE_ADVANCED_USER);
	}
	
	/**
	 * @return the user associated with the request or throws NullPointerException if user is not signed in
	 * @throws EvRequestException when user is not signed in
	 */
	private AdvancedUser getAdvancedUserOrThrow() throws EvRequestException {
		AdvancedUser user = getAdvancedUserOrNull();
		
		if (null == user) {
			throw new EvRequestException("user is not signed in");
		}
		
		return user;
	}
	
	
	@Override
	public void register(RegisterUser user) throws EvRequestException {
		if (null == user) throw new NullPointerException("argument 'user' is null");
		
		DB_GATE.registerUser(user);
	}

	@Override
	public User<UserIdentifier> signIn(SignInUser user) throws EvRequestException {
		if (null == user) throw new NullPointerException("argument 'user' is null");
		
		AdvancedUser advancedUser = DB_GATE.signInUser(user);
		
		getThreadLocalRequest().getSession().setAttribute(SESSION_ATTRIBUTE_ADVANCED_USER, advancedUser);
		
		return advancedUser.constructUser();
	}
	
	@Override
	public User<UserIdentifier> getSignedInUser() {
		AdvancedUser user = getAdvancedUserOrNull();
		if (null == user) {
			return null;
		} else {
			return user.constructUser();
		}
	}

	@Override
	public void signOut() {
		getThreadLocalRequest().getSession().invalidate();
	}
	
	@Override
	public EvEntry<Ev, EntryIdentifier> getRootEntry() {
		return DB_GATE.getRootEntry();
	}
	
	@Override
	public EvEntry<Ev, EntryIdentifier> getStartEntry() {
		EvEntry<Ev, EntryIdentifier> rootEntry = DB_GATE.getRootEntry();
		assert null != rootEntry;
		EvEntry<Ev, EntryIdentifier> startEntry = DB_GATE.getTopRatedEntry(new EntryClassIdentifier(rootEntry.getIdentifier().getEntryId()));
		
		if (null != startEntry) {
			return startEntry;
		} else {
			return rootEntry;
		}
	}
	
	@Override
	public EvEntry<Ev, EntryIdentifier> getEntry(EntryShortIdentifier identifier)
			throws EvRequestException {
		if (null == identifier) throw new NullPointerException("argument 'identifier' is null");
		
		return DB_GATE.getEntry(identifier);
	}
	
	@Override
	@Deprecated
	public EvEntryPackage getStartEntryPackage() {
		
		AdvancedUser user = getAdvancedUserOrNull();
		UserIdentifier userIdentifier;
		if (null == user) {
			userIdentifier = null;
		} else {
			userIdentifier = user.getIdentifier();
		}
		
		return DB_GATE.getStartEntryPackage(userIdentifier);
	}
	
	@Override
	public EvEntryPackage getEntryPackage(EntryClassIdentifier identifier) {
		if (null == identifier) throw new NullPointerException("argument 'identifier' is null");
		
		AdvancedUser user = getAdvancedUserOrNull();
		UserIdentifier userIdentifier;
		if (null == user) {
			userIdentifier = null;
		} else {
			userIdentifier = user.getIdentifier();
		}
		
		return DB_GATE.getEntryPackage(userIdentifier, identifier);
	}

	@Override
	public EvEntry<Ev, EntryIdentifier> addEntry(EvEntry<EvVoid, EntryClassIdentifier> entry) throws EvRequestException, EvIllegalDataException {
		if (null == entry) throw new NullPointerException("argument 'entry' is null");
		
		entry.check();
		
		AdvancedUser user = getAdvancedUserOrThrow();
		
		entry.setAuthor(user.getIdentifier());
		return DB_GATE.addEntry(entry, user.getIdentifier(), user.getVotes());
	}

	@Override
	public EvEntry<Ev, EntryIdentifier> linkEntry(EntryIdentifier source,
			EntryClassIdentifier destination) throws EvRequestException {
		if (null == source) throw new NullPointerException("argument 'source' is null");
		if (null == destination) throw new NullPointerException("argument 'destination' is null");
		
		AdvancedUser user = getAdvancedUserOrThrow();
		
		return DB_GATE.copyEntry(source, destination, user.getIdentifier(), user.getVotes());
	}

	@Override
	public void voteEntry(EntryIdentifier entryIdent, boolean vote) throws EvRequestException {
		if (null == entryIdent) throw new NullPointerException("argument 'entryIdent' is null");
		
		AdvancedUser user = getAdvancedUserOrThrow();
		
		DB_GATE.voteEntry(user.getIdentifier(), entryIdent, vote, user.getVotes());
	}

	@Override
	public EvPath getPath(EntryShortIdentifier identifier) {
		if (null == identifier) throw new NullPointerException("argument 'identifier' is null");
		
		return DB_GATE.getPath(identifier);
	}
	
	@Override
	public EvElement getElement(EntryClassIdentifier classIdentifier, EntryShortIdentifier valueIdentifier) {
		if (null == classIdentifier) throw new NullPointerException("argument 'classIdentifier' is null");
		if (null == valueIdentifier) throw new NullPointerException("argument 'valueIdentifier' is null");
		
		AdvancedUser user = getAdvancedUserOrNull();
		UserIdentifier userIdentifier;
		if (null == user) {
			userIdentifier = null;
		} else {
			userIdentifier = user.getIdentifier();
		}
		
		return DB_GATE.getElement(userIdentifier, classIdentifier, valueIdentifier);
	}

	@Override
	public EvAttribute<Ev, AttributeIdentifier> addAttribute(EvAttribute<EvVoid, EntryClassIdentifier> attribute)
			throws EvRequestException, EvIllegalDataException {
		if (null == attribute) throw new NullPointerException("argument 'attribute' is null");
		
		attribute.check();
		
		AdvancedUser user = getAdvancedUserOrThrow();
		
		attribute.setAuthor(user.getIdentifier());
		return DB_GATE.addAttribute(attribute, user.getIdentifier(), user.getVotes());
	}

	@Override
	public void voteAttribute(AttributeIdentifier attributeIdent, boolean vote)
			throws EvRequestException {
		if (null == attributeIdent) throw new NullPointerException("argument 'attributeIdent' is null");
		
		AdvancedUser user = getAdvancedUserOrThrow();
		
		DB_GATE.voteAttribute(user.getIdentifier(), attributeIdent, vote, user.getVotes());
	}

	@Override
	public EvAttributeValue<Ev, AttributeValueIdentifier> addAttributeValue(
			EvAttributeValue<EvVoid, AttributeValueIdentifier> attributeValue)
			throws EvRequestException, EvIllegalDataException {
		if (null == attributeValue) throw new NullPointerException("argument 'attributeValue' is null");
		
		EvAttributeValue.check(attributeValue);
		
		AdvancedUser user = getAdvancedUserOrThrow();
		
		attributeValue.setAuthor(user.getIdentifier());
		return DB_GATE.addAttributeValue(attributeValue, user.getIdentifier(), user.getVotes());
	}

	@Override
	public void voteAttributeValue(AttributeValueIdentifier attributeValueIdent,
			boolean vote) throws EvRequestException {
		if (null == attributeValueIdent) throw new NullPointerException("argument 'attributeValueIdent' is null");
		
		AdvancedUser user = getAdvancedUserOrThrow();
		
		DB_GATE.voteAttributeValue(user.getIdentifier(), attributeValueIdent, vote, user.getVotes());
	}

	@Override
	public EvAttributeValueContainer getAttributeValueContainer(
			AttributeIdentifier attributeIdent) {
		if (null == attributeIdent) throw new NullPointerException("argument 'attributeIdent' is null");
		
		AdvancedUser user = getAdvancedUserOrNull();
		UserIdentifier userIdentifier;
		if (null == user) {
			userIdentifier = null;
		} else {
			userIdentifier = user.getIdentifier();
		}
		
		return DB_GATE.getAttributeValueContainer(userIdentifier, attributeIdent);
	}

	@Override
	public EvMedium<Ev, MediumIdentifier> addMedium(EvMedium<EvVoid, EntryClassIdentifier> medium)
			throws EvRequestException, EvIllegalDataException {
		if (null == medium) throw new NullPointerException("argument 'medium' is null");
		
		medium.check();
		
		AdvancedUser user = getAdvancedUserOrThrow();
		
		medium.setAuthor(user.getIdentifier());
		return DB_GATE.addMedium(medium, user.getIdentifier(), user.getVotes());
	}

	@Override
	public void voteMedium(MediumIdentifier mediumIdent, boolean vote)
			throws EvRequestException {
		if (null == mediumIdent) throw new NullPointerException("argument 'mediumIdent' is null");
		
		AdvancedUser user = getAdvancedUserOrThrow();
		
		DB_GATE.voteMedium(user.getIdentifier(), mediumIdent, vote, user.getVotes());
	}

	@Override
	public EvCriterion<Ev, CriterionIdentifier> addCriterion(
			EvCriterion<EvVoid, EntryClassIdentifier> criterion)
			throws EvRequestException, EvIllegalDataException {
		if (null == criterion) throw new NullPointerException("argument 'criterion' is null");
		
		criterion.check();
		
		AdvancedUser user = getAdvancedUserOrThrow();
		
		criterion.setAuthor(user.getIdentifier());
		return DB_GATE.addCriterion(criterion, user.getIdentifier(), user.getVotes());
	}

	@Override
	public void voteCriterion(CriterionIdentifier criterionIdent, boolean vote)
			throws EvRequestException {
		if (null == criterionIdent) throw new NullPointerException("argument 'criterionIdent' is null");
		
		AdvancedUser user = getAdvancedUserOrThrow();
		
		DB_GATE.voteCriterion(user.getIdentifier(), criterionIdent, vote, user.getVotes());
	}

	@Override
	public void voteCriterionValue(CriterionIdentifier criterionValueIdent,
			int vote) throws EvRequestException {
		if (null == criterionValueIdent) throw new NullPointerException("argument 'criterionValueIdent' is null");
		
		AdvancedUser user = getAdvancedUserOrThrow();
		
		if (user.getVotes() > 0) {
			DB_GATE.voteCriterionValue(user.getIdentifier(), criterionValueIdent, vote, 1);	// TODO in that case each user should have the same number of votes...
		} else {
			DB_GATE.voteCriterionValue(user.getIdentifier(), criterionValueIdent, vote, 0);
		}
		
//		Main.DB_GATE.voteCriterionValue(user.getIdentifier(), criterionValueIdent, vote, user.getVotes());	// TODO ...but this can be used to filter spam
	}

	@Override
	public EvComment<Ev, CommentIdentifier> addComment(
			EvComment<EvVoid, EntryClassIdentifier> comment)
			throws EvRequestException, EvIllegalDataException {
		if (null == comment) throw new NullPointerException("argument 'comment' is null");
		
		comment.check();
		
		AdvancedUser user = getAdvancedUserOrThrow();
		
		comment.setAuthor(user.getIdentifier());
		comment.setAuthorPseudonym(user.getPseudonym());
		return DB_GATE.addComment(comment, user.getIdentifier(), user.getVotes());
	}

	@Override
	public void voteComment(CommentIdentifier commentIdent, boolean vote)
			throws EvRequestException {
		if (null == commentIdent) throw new NullPointerException("argument 'commentIdent' is null");
		
		AdvancedUser user = getAdvancedUserOrThrow();
		
		DB_GATE.voteComment(user.getIdentifier(), commentIdent, vote, user.getVotes());
	}

}