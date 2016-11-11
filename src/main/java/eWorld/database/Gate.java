package eWorld.database;

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

/**
 * describes a facade to interact with the database
 * @author michael
 *
 */
public interface Gate {
	
	/**
	 * closes the connection to the database cluster
	 */
	public void closeDatabase();
	
	/**
	 * @param user specifies the user that should be added
	 * @throws EvRequestException if the user couldn't be added for some reason
	 */
	public void registerUser(final RegisterUser user) throws EvRequestException;
	
	/**
	 * @param user specifies the user that should be signed in
	 * @return the user that signs in (contains also the id of the user)
	 * @throws EvRequestException if the user cannot be signed in for some reason
	 */
	public AdvancedUser signInUser(final SignInUser user) throws EvRequestException;
	
	/**
	 * @return the entry that is on the top of the hierarchy
	 */
	public EvEntry<Ev, EntryIdentifier> getRootEntry();
	
	/**
	 * @param 
	 * @return the top rated entry of the specified class|directory or null if class|directory contains no items (or does not exist)
	 */
	public EvEntry<Ev, EntryIdentifier> getTopRatedEntry(EntryClassIdentifier directory);
	
	/**
	 * TODO? UserIdentifier as argument and load votes?
	 * @param identifier
	 * @return an entry that matches the given identifier
	 * @throws EvRequestException if specified entry does not exist
	 */
	public EvEntry<Ev, EntryIdentifier> getEntry(EntryShortIdentifier identifier) throws EvRequestException;
	
	/**
	 * adds the given entry to the database
	 * ensures that entryClass does exist and that entryClass describes not an element
	 * executes also a upVote for the given user for the added entry
	 * @param entry the entry which is to add
	 * @param userIdentifier the user who adds the entry	TODO this information is now already in param entry given (author)
	 * @param votes the initial-value for votes
	 * @return the added entry
	 * @throws EvRequestException if entryClass where entry should be added does not exist or if entryClass describes an element
	 */
	public EvEntry<Ev,EntryIdentifier> addEntry(final EvEntry<EvVoid, EntryClassIdentifier> entry, final UserIdentifier userIdentifier, final int votes) throws EvRequestException;
	
	/**
	 * copies the specified entry from source to destination, the name and description are retained
	 * this creates only a linkage, the super|parent-entry of the copied entry remains the one of source
	 * ensures that entryClass does exist
	 * ensures that entryClass describes not an element
	 * ensures that entryClass does not contain the entry already
	 * executes also a upVote for the given user for the added entry
	 * @param source specified the entry that is to copy
	 * @param destination the entryClass where the specified entry should be copied to
	 * @param userIdentifier specifies the user who copies the entry
	 * @param votes the initial-value for votes
	 * @return the copied entry
	 * @throws EvRequestException if entryClass where entry should be copied to does not exist or if it describes an element or if it contains the entry already
	 */
	public EvEntry<Ev, EntryIdentifier> copyEntry(final EntryIdentifier source, final EntryClassIdentifier destination, final UserIdentifier userIdentifier, final int votes) throws EvRequestException;
	
	/**
	 * executes a vote for an entry
	 * ensures that specified entry does exists
	 * manages if specified user has already voted for the specified entry
	 * @param userIdent specifies the user who votes
	 * @param entryIdent specifies the entry to vote for
	 * @param vote true for upVote, false for downVote
	 * @param votes the number of votes the specified user has
	 * @throws EvRequestException if the entry to vote for does not exist
	 */
	public void voteEntry(final UserIdentifier userIdent, final EntryIdentifier entryIdent, final boolean vote, final int votes) throws EvRequestException;
	
	/**
	 * returns the path of an entry that is specified by the given identifier
	 * @param identifier specifies the entry
	 * @return the path that leads to the entry
	 */
	public EvPath getPath(EntryShortIdentifier identifier);
	
	/**
	 * returns a {@code EvEntryContainer} that lists entries in ranked order
	 * @param userIdentifier identifies the user whose votes should be loaded or null when user is not signed in
	 * @param superIdentifier specifies the entries that should be loaded
	 * @return a {@code EvEntryContainer} that contains elements of the type {@code Entry}
	 */
	@Deprecated
	public EvEntryContainer listEntriesRanked(UserIdentifier userIdentifier, EntryClassIdentifier superIdentifier);
	
	/**
	 * adds the given attribute to the database
	 * ensures that entryClass does exist
	 * ensures that entryClass describes a directory and not an element
	 * executes also a upVote for the given user for the added attribute
	 * @param attribute the attribute which is to add
	 * @param userIdentifier the user who adds the attribute
	 * @param votes the initial-value for votes
	 * @return the added attribute
	 * @throws EvRequestException if entryClass where attribute should be added does not exist or describes an element and not a directory
	 */
	public EvAttribute<Ev, AttributeIdentifier> addAttribute(final EvAttribute<EvVoid, EntryClassIdentifier> attribute, final UserIdentifier userIdentifier, final int votes) throws EvRequestException;
	
	/**
	 * adds an existing attribute to the database under another name and description
	 * ensures that entryClass that is specified by attribute.getIdentifier().getEntryClassId() exists
	 * ensures that entryClass describes a directory and not an element
	 * ensures that attribute that is specified by attribute.getIdentifier().getAttributeId() exists
	 * ensures that entryClass does not already have the attribute
	 * executes also a upVote for the given user for the added attribute
	 * @param attribute the attribute which is to add
	 * @param userIdentifier the user who adds the attribute
	 * @param votes the initial-value for votes
	 * @return the added attribute
	 * @throws EvRequestException 
	 * 		if attribute does not exist 
	 * 		or entryClass where attribute should be added does not exist 
	 * 		or entryClass describes an element and not a directory 
	 * 		or entryClass already has the attribute
	 */
	public EvAttribute<Ev, AttributeIdentifier> addExistingAttribute(final EvAttribute<EvVoid, AttributeIdentifier> attribute, final UserIdentifier userIdentifier, final int votes) throws EvRequestException;
	
	/**
	 * executes a vote for an attribute
	 * ensures that specified attribute does exists
	 * manages if specified user has already voted for the specified attribute
	 * @param userIdent specifies the user who votes
	 * @param attributeIdent specifies the attribute to vote for
	 * @param vote true for upVote, false for downVote
	 * @param votes the number of votes the specified user has
	 * @throws EvRequestException if the attribute to vote for does not exist
	 */
	public void voteAttribute(final UserIdentifier userIdent, final AttributeIdentifier attributeIdent, final boolean vote, final int votes) throws EvRequestException;
	
	/**
	 * returns a {@code EvAttributeContainer} that lists attributes in ranked order (also grouped in groups)
	 * @param userIdentifier identifies the user whose votes should be loaded or null when user is not signed in
	 * @param superIdentifier specifies the attributes that should be loaded
	 * @return a {@code EvAttributeContainer} that contains elements of the type {@code Attribute}
	 */
	@Deprecated
	public EvAttributeContainer listAttributesRanked(UserIdentifier userIdentifier, EntryClassIdentifier superIdentifier);
	
	/**
	 * 
	 * @param userIdentifier
	 * @return the entryPackage or directory that can be loaded as start-page
	 */
	public EvEntryPackage getStartEntryPackage(UserIdentifier userIdentifier);
	
	public EvEntryPackage getEntryPackage(UserIdentifier userIdentifier, EntryClassIdentifier superIdentifier);
	
	/**
	 * 
	 * @param userIdentifier
	 * @param classdentifier identifies i.a. the header and attributes that should be loaded
	 * @param valueIdentifier identifies i.a. the attributeValues that should be loaded
	 * @return
	 */
	public EvElement getElement(UserIdentifier userIdentifier, EntryClassIdentifier classIdentifier, EntryShortIdentifier valueIdentifier);
	
	/**
	 * adds the given attributeValue to the database
	 * ensures that entry does exist and that it specifies an element and not a class of entries
	 * ensures that attribute belongs to entry (and that attribute does exist)
	 * executes also a upVote for the given user for the added attributeValue
	 * @param attributeValue the attributeValue which is to add
	 * @param userIdentifier the user who adds the attributeValue
	 * @param votes the initial-value for votes
	 * @return the added attributeValue
	 * @throws EvRequestException if entry or attribute where attributeValue should be added does not exist (or attributeValue does already exist)
	 */
	public EvAttributeValue<Ev, AttributeValueIdentifier> addAttributeValue(EvAttributeValue<EvVoid, AttributeValueIdentifier> attributeValue, UserIdentifier userIdentifier, int votes) throws EvRequestException;
	
	/**
	 * executes a vote for an attributeValue
	 * ensures that specified attributeValue does exists
	 * manages if specified user has already voted for the specified attributeValue
	 * @param userIdent specifies the user who votes
	 * @param attributeValueIdent specifies the attributeValue to vote for
	 * @param vote true for upVote, false for downVote
	 * @param votes the number of votes the specified user has
	 * @throws EvRequestException if the attributeValue to vote for does not exist
	 */
	public void voteAttributeValue(UserIdentifier userIdent, AttributeValueIdentifier attributeValueIdent, boolean vote, int votes) throws EvRequestException;
	
	/**
	 * loads the values of the given attribute
	 * @param userIdentifier identifies the votes that should be loaded
	 * @param attributeIdent identifies the attributeValues that should be loaded
	 * @return container that contains attributeValues of the specified attribute
	 */
	public EvAttributeValueContainer getAttributeValueContainer(UserIdentifier userIdent, AttributeIdentifier attributeIdent);
	
	/**
	 * checks if the topValue of the specified attribute has changed
	 * and updates the topValue in attributeTopValueDataSchema
	 * attribute should specify an element and not a class of entries
	 * @param attributeIdent specifies the attribute
	 */
	public void updateAttributeTopValue(AttributeIdentifier attributeIdent);
	
	/**
	 * adds the given medium to the database
	 * ensures that entry where medium should be added does exist
	 * executes also a upVote for the given user for the added medium
	 * @param medium the medium which is to add
	 * @param userIdentifier the user who adds the medium
	 * @param votes the initial-value for votes
	 * @return the added medium
	 * @throws EvRequestException if entry where medium should be added does not exist
	 */
	public EvMedium<Ev, MediumIdentifier> addMedium(EvMedium<EvVoid, EntryClassIdentifier> medium, UserIdentifier userIdentifier, int votes) throws EvRequestException;
	
	/**
	 * executes a vote for a medium
	 * ensures that specified medium does exists
	 * manages if specified user has already voted for the specified medium
	 * @param userIdent specifies the user who votes
	 * @param mediumIdent specifies the medium to vote for
	 * @param vote true for upVote, false for downVote
	 * @param votes the number of votes the specified user has
	 * @throws EvRequestException if the medium to vote for does not exist
	 */
	public void voteMedium(UserIdentifier userIdent, MediumIdentifier mediumIdent, boolean vote, int votes) throws EvRequestException;
	
	/**
	 * adds the given criterion to the database
	 * ensures that entryClass does exist
	 * ensures that entryClass describes a directory and not an element
	 * executes also a upVote for the given user for the added criterion
	 * @param criterion the criterion which is to add
	 * @param userIdentifier the user who adds the criterion
	 * @param votes the initial-value for votes
	 * @return the added criterion
	 * @throws EvRequestException if entryClass where criterion should be added does not exist or describes an element and not a directory
	 */
	public EvCriterion<Ev, CriterionIdentifier> addCriterion(final EvCriterion<EvVoid, EntryClassIdentifier> criterion, final UserIdentifier userIdentifier, final int votes) throws EvRequestException;
	
	/**
	 * adds an existing criterion to the database under another name and description
	 * ensures that destinationCriterion matches to sourceCriterion (equal shortIdentifiers)
	 * ensures that entryClass that is specified by criterion.getIdentifier().getEntryClassId() exists
	 * ensures that entryClass describes a directory and not an element
	 * ensures that criterion that is specified by criterion.getIdentifier().getAttributeId() exists
	 * ensures that entryClass does not already have the criterion
	 * ensures that worst and best are equal to the original criterion
	 * executes also a upVote for the given user for the added criterion
	 * @param sourceCriterion the original criterion (that should be copied)
	 * @param destinationCriterion the criterion which is to add, must have same criterionShortIdentifier as sourceCriterion
	 * @param userIdentifier the user who adds the criterion
	 * @param votes the initial-value for votes
	 * @return the added criterion
	 * @throws EvRequestException if one of the "ensures" is not valid
	 */
	public EvCriterion<Ev, CriterionIdentifier> addExistingCriterion(
			final CriterionIdentifier sourceCriterion,
			final EvCriterion<EvVoid, CriterionIdentifier> destinationCriterion, 
			final UserIdentifier userIdentifier, final int votes) throws EvRequestException;
	
	/**
	 * executes a vote for an criterion
	 * ensures that specified criterion does exist
	 * manages if specified user has already voted for the specified criterion
	 * @param userIdent specifies the user who votes
	 * @param criterionIdent specifies the criterion to vote for
	 * @param vote true for upVote, false for downVote
	 * @param votes the number of votes the specified user has
	 * @throws EvRequestException if the criterion to vote for does not exist
	 */
	public void voteCriterion(final UserIdentifier userIdent, final CriterionIdentifier criterionIdent, final boolean vote, final int votes) throws EvRequestException;
	
	/**
	 * hands a vote in for the specified CriterionValue
	 * @param userIdent specifies the user who votes
	 * @param criterionValueIdent specifies the criterionValue to vote for
	 * @param vote the vote
	 * @param votes the number of votes the specified user has
	 * @throws EvRequestException if identifier is not valid, IllegalArgumentException if vote is out of bounds
	 */
	public void voteCriterionValue(final UserIdentifier userIdent, final CriterionIdentifier criterionValueIdent, final int vote, final int votes) throws EvRequestException;
	
	/**
	 * adds the given comment to the database
	 * ensures that entry where comment should be added does exist
	 * executes also a upVote for the given user for the added comment
	 * @param comment the comment which is to add
	 * @param userIdentifier the user who adds the comment
	 * @param votes the initial-value for votes
	 * @return the added comment
	 * @throws EvRequestException if entry where comment should be added does not exist
	 */
	public EvComment<Ev, CommentIdentifier> addComment(EvComment<EvVoid, EntryClassIdentifier> comment, UserIdentifier userIdentifier, int votes) throws EvRequestException;
	
	/**
	 * executes a vote for a comment
	 * ensures that specified comment does exists
	 * manages if specified user has already voted for the specified comment
	 * @param userIdent specifies the user who votes
	 * @param commentIdent specifies the comment to vote for
	 * @param vote true for upVote, false for downVote
	 * @param votes the number of votes the specified user has
	 * @throws EvRequestException if the comment to vote for does not exist
	 */
	public void voteComment(UserIdentifier userIdent, CommentIdentifier commentIdent, boolean vote, int votes) throws EvRequestException;
	
}
