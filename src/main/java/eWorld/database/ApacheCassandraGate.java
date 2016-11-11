package eWorld.database;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import eWorld.Main;
import eWorld.database.impl.DB;
import eWorld.database.impl.UserSchema;
import eWorld.database.impl.attributeValues.AttributeTopValueDataSchema;
import eWorld.database.impl.attributeValues.AttributeValueCounterSchema;
import eWorld.database.impl.attributeValues.AttributeValueDataSchema;
import eWorld.database.impl.attributeValues.AttributeValueSchemaGate;
import eWorld.database.impl.attributeValues.AttributeValueVoteSchema;
import eWorld.database.impl.attributes.AttributeCounterSchema;
import eWorld.database.impl.attributes.AttributeDataSchema;
import eWorld.database.impl.attributes.AttributeSchemaGate;
import eWorld.database.impl.attributes.AttributeVoteSchema;
import eWorld.database.impl.comments.CommentCounterSchema;
import eWorld.database.impl.comments.CommentDataSchema;
import eWorld.database.impl.comments.CommentSchemaGate;
import eWorld.database.impl.comments.CommentVoteSchema;
import eWorld.database.impl.criteria.CriterionCounterSchema;
import eWorld.database.impl.criteria.CriterionDataSchema;
import eWorld.database.impl.criteria.CriterionSchemaGate;
import eWorld.database.impl.criteria.CriterionVoteSchema;
import eWorld.database.impl.criterionValues.CriterionValueCounterSchema;
import eWorld.database.impl.criterionValues.CriterionValueDataSchema;
import eWorld.database.impl.criterionValues.CriterionValueSchemaGate;
import eWorld.database.impl.criterionValues.CriterionValueVoteSchema;
import eWorld.database.impl.entries.EntryCounterSchema;
import eWorld.database.impl.entries.EntryDataSchema;
import eWorld.database.impl.entries.EntryRegisterSchema;
import eWorld.database.impl.entries.EntrySchemaGate;
import eWorld.database.impl.entries.EntryVoteSchema;
import eWorld.database.impl.media.MediumCounterSchema;
import eWorld.database.impl.media.MediumDataSchema;
import eWorld.database.impl.media.MediumSchemaGate;
import eWorld.database.impl.media.MediumVoteSchema;
import eWorld.datatypes.EvPath;
import eWorld.datatypes.containers.EvAttributeContainer;
import eWorld.datatypes.containers.EvAttributeValueContainer;
import eWorld.datatypes.containers.EvCommentContainer;
import eWorld.datatypes.containers.EvCriterionContainer;
import eWorld.datatypes.containers.EvEntryContainer;
import eWorld.datatypes.containers.EvMediumContainer;
import eWorld.datatypes.data.EvAttribute;
import eWorld.datatypes.data.EvAttributeValue;
import eWorld.datatypes.data.EvComment;
import eWorld.datatypes.data.EvCriterion;
import eWorld.datatypes.data.EvEntry;
import eWorld.datatypes.data.EvMedium;
import eWorld.datatypes.data.IdAttributeTopValue;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.evs.EvVoid;
import eWorld.datatypes.exceptions.EvRequestException;
import eWorld.datatypes.identifiers.AttributeIdentifier;
import eWorld.datatypes.identifiers.AttributeShortIdentifier;
import eWorld.datatypes.identifiers.AttributeValueIdentifier;
import eWorld.datatypes.identifiers.CommentIdentifier;
import eWorld.datatypes.identifiers.CommentShortIdentifier;
import eWorld.datatypes.identifiers.CriterionIdentifier;
import eWorld.datatypes.identifiers.CriterionShortIdentifier;
import eWorld.datatypes.identifiers.EntryClassIdentifier;
import eWorld.datatypes.identifiers.EntryIdentifier;
import eWorld.datatypes.identifiers.EntryShortIdentifier;
import eWorld.datatypes.identifiers.MediumIdentifier;
import eWorld.datatypes.identifiers.MediumShortIdentifier;
import eWorld.datatypes.identifiers.UserIdentifier;
import eWorld.datatypes.identifiers.VoteIdentifier;
import eWorld.datatypes.packages.EvElement;
import eWorld.datatypes.packages.EvEntryPackage;
import eWorld.datatypes.user.AdvancedUser;
import eWorld.datatypes.user.RegisterUser;
import eWorld.datatypes.user.SecretUser;
import eWorld.datatypes.user.SignInUser;

public class ApacheCassandraGate implements Gate {

	/** the number of votes a user has by default after registration */
	private static final int USER_DEFAULT_REGISTER_VOTES = 10;
	
	private final DB db;
	
	private final UserSchema userSchema ;
	
	private final EntryRegisterSchema entryRegisterSchema;
	private final EntryDataSchema entryDataSchema;
	private final EntryCounterSchema entryCounterSchema;
	private final EntryVoteSchema entryVoteSchema;
	private final EntrySchemaGate entrySchemaGate;
	
	private final AttributeDataSchema attributeDataSchema;
	private final AttributeCounterSchema attributeCounterSchema;
	private final AttributeVoteSchema attributeVoteSchema;
	private final AttributeSchemaGate attributeSchemaGate;
	
	private final AttributeTopValueDataSchema attributeTopValueDataSchema;
	
	private final AttributeValueDataSchema attributeValueDataSchema;
	private final AttributeValueCounterSchema attributeValueCounterSchema;
	private final AttributeValueVoteSchema attributeValueVoteSchema;
	private final AttributeValueSchemaGate attributeValueSchemaGate;
	
	private final MediumDataSchema mediumDataSchema;
	private final MediumCounterSchema mediumCounterSchema;
	private final MediumVoteSchema mediumVoteSchema;
	private final MediumSchemaGate mediumSchemaGate;
	
	private final CriterionDataSchema criterionDataSchema;
	private final CriterionCounterSchema criterionCounterSchema;
	private final CriterionVoteSchema criterionVoteSchema;
	private final CriterionSchemaGate criterionSchemaGate;
	
	private final CriterionValueDataSchema criterionValueDataSchema;
	private final CriterionValueCounterSchema criterionValueCounterSchema;
	private final CriterionValueVoteSchema criterionValueVoteSchema;
	private final CriterionValueSchemaGate criterionValueSchemaGate;
	
	private final CommentDataSchema commentDataSchema;
	private final CommentCounterSchema commentCounterSchema;
	private final CommentVoteSchema commentVoteSchema;
	private final CommentSchemaGate commentSchemaGate;
	
	/**
	 * initializes the database, checks if all tables are correct, creates them if they don't exist
	 * @param nodeAddress IP-address of the node to connect to
	 */
	public ApacheCassandraGate(final String nodeAddress) {
		assert null != nodeAddress;
		
		db = new DB(nodeAddress);
		
		userSchema = new UserSchema(db.getUserKeyspaceSession(), DB.USER_SCHEMA);
		
		entryRegisterSchema = new EntryRegisterSchema(db.getGeneralKeyspaceSession(), DB.ENTRY_REGISTER_SCHEMA);
		entryDataSchema = new EntryDataSchema(db.getGeneralKeyspaceSession(), DB.ENTRY_DATA_SCHEMA);
		entryCounterSchema = new EntryCounterSchema(db.getGeneralKeyspaceSession(), DB.ENTRY_COUNTER_SCHEMA);
		entryVoteSchema = new EntryVoteSchema(db.getGeneralKeyspaceSession(), DB.ENTRY_VOTE_SCHEMA);
		entrySchemaGate = new EntrySchemaGate(entryRegisterSchema, entryDataSchema, entryCounterSchema, entryVoteSchema);
		
		attributeDataSchema = new AttributeDataSchema(db.getGeneralKeyspaceSession(), DB.ATTRIBUTE_DATA_SCHEMA);
		attributeCounterSchema = new AttributeCounterSchema(db.getGeneralKeyspaceSession(), DB.ATTRIBUTE_COUNTER_SCHEMA);
		attributeVoteSchema = new AttributeVoteSchema(db.getGeneralKeyspaceSession(), DB.ATTRIBUTE_VOTE_SCHEMA);
		attributeSchemaGate = new AttributeSchemaGate(attributeDataSchema, attributeCounterSchema, attributeVoteSchema);
		
		attributeTopValueDataSchema = new AttributeTopValueDataSchema(db.getGeneralKeyspaceSession(), DB.ATTRIBUTE_TOP_VALUE_SCHEMA);
		
		attributeValueDataSchema = new AttributeValueDataSchema(db.getGeneralKeyspaceSession(), DB.ATTRIBUTE_VALUE_DATA_SCHEMA);
		attributeValueCounterSchema = new AttributeValueCounterSchema(db.getGeneralKeyspaceSession(), DB.ATTRIBUTE_VALUE_COUNTER_SCHEMA);
		attributeValueVoteSchema = new AttributeValueVoteSchema(db.getGeneralKeyspaceSession(), DB.ATTRIBUTE_VALUE_VOTE_SCHEMA);
		attributeValueSchemaGate = new AttributeValueSchemaGate(attributeValueDataSchema, attributeValueCounterSchema, attributeValueVoteSchema);
		
		mediumDataSchema = new MediumDataSchema(db.getGeneralKeyspaceSession(), DB.MEDIUM_DATA_SCHEMA);
		mediumCounterSchema = new MediumCounterSchema(db.getGeneralKeyspaceSession(), DB.MEDIUM_COUNTER_SCHEMA);
		mediumVoteSchema = new MediumVoteSchema(db.getGeneralKeyspaceSession(), DB.MEDIUM_VOTE_SCHEMA);
		mediumSchemaGate = new MediumSchemaGate(mediumDataSchema, mediumCounterSchema, mediumVoteSchema);
		
		criterionDataSchema = new CriterionDataSchema(db.getGeneralKeyspaceSession(), DB.CRITERION_DATA_SCHEMA);
		criterionCounterSchema = new CriterionCounterSchema(db.getGeneralKeyspaceSession(), DB.CRITERION_COUNTER_SCHEMA);
		criterionVoteSchema = new CriterionVoteSchema(db.getGeneralKeyspaceSession(), DB.CRITERION_VOTE_SCHEMA);
		criterionSchemaGate = new CriterionSchemaGate(criterionDataSchema, criterionCounterSchema, criterionVoteSchema);
		
		criterionValueDataSchema = new CriterionValueDataSchema(db.getGeneralKeyspaceSession(), DB.CRITERION_VALUE_DATA_SCHEMA);
		criterionValueCounterSchema = new CriterionValueCounterSchema(db.getGeneralKeyspaceSession(), DB.CRITERION_VALUE_COUNTER_SCHEMA);
		criterionValueVoteSchema = new CriterionValueVoteSchema(db.getGeneralKeyspaceSession(), DB.CRITERION_VALUE_VOTE_SCHEMA);
		criterionValueSchemaGate = new CriterionValueSchemaGate(criterionValueDataSchema, criterionValueCounterSchema, criterionValueVoteSchema, entryRegisterSchema, criterionDataSchema);
		
		commentDataSchema = new CommentDataSchema(db.getGeneralKeyspaceSession(), DB.COMMENT_DATA_SCHEMA);
		commentCounterSchema = new CommentCounterSchema(db.getGeneralKeyspaceSession(), DB.COMMENT_COUNTER_SCHEMA);
		commentVoteSchema = new CommentVoteSchema(db.getGeneralKeyspaceSession(), DB.COMMENT_VOTE_SCHEMA);
		commentSchemaGate = new CommentSchemaGate(commentDataSchema, commentCounterSchema, commentVoteSchema);
		
//		// fill db with some stuff, comment this out if db is not empty TODO delete this
//		try {
//			Main.fillDB(this);
//		} catch (EvRequestException e) {
//			e.printStackTrace();
//		}
	}
	
	@Override
	public void closeDatabase() {
		db.close();
	}
	
	/**
	 * 
	 * @param salt
	 * @param password
	 * @return the hash of password with salt as prefix
	 * @throws EvRequestException when calculating of hash failed
	 */
	private String calculateHash(String salt, String password) throws EvRequestException {
		assert null != salt;
		assert null != password;
		
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
			
			String saltedPassword = salt + password;
			
			return new String(messageDigest.digest(saltedPassword.getBytes("UTF-8")), "UTF-8");
			
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			e.printStackTrace();	// TODO log
			throw new EvRequestException("epic fail");
		}
	}

	@Override
	public void registerUser(RegisterUser user) throws EvRequestException {
		assert null != user;
		
		// check pseudonym TODO? check this in SericeImpl instead of here?
		if (null != SignInUser.checkPseudonym(user.getPseudonym())) {
			throw new EvRequestException(SignInUser.checkPseudonym(user.getPseudonym()));
		}
		
		// check password TODO? check this in SericeImpl instead of here?
		if (null != SignInUser.checkPassword(user.getPassword())) {
			throw new EvRequestException(SignInUser.checkPassword(user.getPassword()));
		}
		
		// check if a user with this pseudonym already exists
		long count = userSchema.countPseudonyms(user.getPseudonym());
		if (1 <= count) {
			if (1 < count) {
				System.out.println("there are " + count + " users with pseudonym '" + user.getPseudonym() + "'");	// TODO log
			}
			throw new EvRequestException("User with this pseudonym already exists.");
		}
		
		// insert new user
		String passwordSalt = String.valueOf(System.currentTimeMillis());	// FIXME? is this a good salt? I think yes
		UserIdentifier identifier = userSchema.insert(user, passwordSalt, calculateHash(passwordSalt, user.getPassword()), USER_DEFAULT_REGISTER_VOTES);
		
		// check if other user with same pseudonym was added at same time
		if (2 <= userSchema.countPseudonyms(user.getPseudonym())) {
			userSchema.delete(identifier);
			throw new EvRequestException("User with this pseudonym already exists.");
		}
	}

	@Override
	public AdvancedUser signInUser(SignInUser user) throws EvRequestException {
		assert null != user;
		
		// TODO? call check-methods of SignInUser, check this in SericeImpl instead of here?
		
		SecretUser secretUser = userSchema.selectSecret(user.getPseudonym());
		
		if (null == secretUser) {
			throw new EvRequestException("User with this pseudonym does not exist");
		}
		
		// check if password is correct
		if (!calculateHash(secretUser.getPasswordSalt(), user.getPassword()).equals(secretUser.getPasswordHash())) {
			throw new EvRequestException("password is not correct");
		}
		
		return secretUser.getUser();
	}
	
	@Override
	public EvEntry<Ev, EntryIdentifier> getRootEntry() {
		return entryDataSchema.selectOne(new EntryIdentifier(EntryRegisterSchema.SUPER_ROOT_ID, EntryRegisterSchema.ROOT_ID));
	}
	
	@Override
	public EvEntry<Ev, EntryIdentifier> getTopRatedEntry(
			EntryClassIdentifier directory) {
		return entryDataSchema.getTopRatedItem(directory);
	}
	
	@Override
	public EvEntry<Ev, EntryIdentifier> getEntry(EntryShortIdentifier identifier) throws EvRequestException {
		assert null != identifier;
		
		// get entryClass
		EntryClassIdentifier entryClass = entryRegisterSchema.getClassIdentifier(identifier);
		if (null == entryClass) {
			throw new EvRequestException("entry does not exist");
		}
		
		// get entry
		EvEntry<Ev, EntryIdentifier> entry = entryDataSchema.selectOne(new EntryIdentifier(entryClass, identifier));
		if (null == entry) {
			throw new EvRequestException("epic fail, entry does not exist but should");
		}
		
		return entry;
	}

	@Override
	public EvEntry<Ev,EntryIdentifier> addEntry(EvEntry<EvVoid, EntryClassIdentifier> entry,
			UserIdentifier userIdentifier, int votes) throws EvRequestException {
		assert null != entry;
		assert null != userIdentifier;
		
		// ensure that class in which entry is to add does exist
		EntryClassIdentifier entryClass = entryRegisterSchema.getClassIdentifier(new EntryShortIdentifier(entry.getIdentifier().getEntryClassId()));
		if (null == entryClass) {
			throw new EvRequestException("entryClass does not exist");
		}
		
		// ensure that entry(Class) where entry should be added is not an element
		EvEntry<Ev, EntryIdentifier> superEntry = entryDataSchema.selectOne(new EntryIdentifier(entryClass.getEntryClassId(), entry.getIdentifier().getEntryClassId()));
		if (superEntry.isElement()) {
			throw new EvRequestException("entryClass specifies an element");
		}
		
		// add entry
		EntryShortIdentifier entryShortIdent = entryRegisterSchema.insert(entry.getIdentifier());
		EntryIdentifier entryIdent = new EntryIdentifier(entry.getIdentifier(), entryShortIdent);
		VoteIdentifier<EntryIdentifier> voteIdent = new VoteIdentifier<EntryIdentifier>(userIdentifier, entryIdent);
		entryDataSchema.insert(entry, entryShortIdent, (float)votes, null);
		entryVoteSchema.vote(voteIdent, true);
		entryCounterSchema.upVote(entryIdent, votes);
		
		EntryClassIdentifier classIdentifier = new EntryClassIdentifier(entryShortIdent.getEntryId());
		
		if (!entry.isElement()) {
			// inherit attributes TODO move this functionality to EvSchemaGate (void copyAll(CONTAINER_IDENT from, CONTAINER_IDENT to))
			EvAttributeContainer eAttributeContainer = attributeSchemaGate.listItems(userIdentifier, entry.getIdentifier());
			ArrayList<EvAttribute<Ev, AttributeShortIdentifier>> attributes = eAttributeContainer.getData();
			for (EvAttribute<Ev, AttributeShortIdentifier> attribute : attributes) {
				attributeDataSchema.insert(
						attributeDataSchema.toClassIdentifiedAttribute(attribute, classIdentifier), 
						attribute.getIdentifier(), 
						attribute.getEv().getRating(), 
						attribute.getEv().getRank());
//				attributeCounterSchema.upVote(new AttributeIdentifier(classIdentifier, attribute.getIdentifier()), votes);
				attributeCounterSchema.upVote(new AttributeIdentifier(classIdentifier, attribute.getIdentifier()), (long)attribute.getEv().getRating());
			}
			
			// inherit criteria TODO move this functionality to EvSchemaGate (void copyAll(CONTAINER_IDENT from, CONTAINER_IDENT to))
			EvCriterionContainer eCriterionContainer = criterionSchemaGate.listItems(userIdentifier, entry.getIdentifier());
			ArrayList<EvCriterion<Ev, CriterionShortIdentifier>> criteria = eCriterionContainer.getData();
			for (EvCriterion<Ev, CriterionShortIdentifier> criterion : criteria) {
				criterionDataSchema.insert(
						criterionDataSchema.toClassIdentifiedCriterion(criterion, classIdentifier), 
						criterion.getIdentifier(), 
						criterion.getEv().getRating(), 
						criterion.getEv().getRank());
//				criterionCounterSchema.upVote(new CriterionIdentifier(classIdentifier, criterion.getIdentifier()), votes);
				criterionCounterSchema.upVote(new CriterionIdentifier(classIdentifier, criterion.getIdentifier()), (long)criterion.getEv().getRating());
			}
		}
		
		// return added entry
		EvEntry<Ev, EntryIdentifier> re = entryDataSchema.selectOne(entryIdent);
		re.getEv().setVote(entryVoteSchema.getBooleanVote(voteIdent));
		return re;
	}
	
	public EvEntry<Ev, EntryIdentifier> copyEntry(
			final EntryIdentifier source, final EntryClassIdentifier destination, final UserIdentifier userIdentifier, final int votes)
					throws EvRequestException {
		assert null != source;
		assert null != destination;
		assert null != userIdentifier;
		
		// ensure that class in which entry is to copy does exist
		EntryClassIdentifier destEntryClass = entryRegisterSchema.getClassIdentifier(new EntryShortIdentifier(destination.getEntryClassId()));
		if (null == destEntryClass) {
			throw new EvRequestException("destination entryClass does not exist");
		}
		
		// ensure that entry(Class) where entry should be copied to is not an element
		EvEntry<Ev, EntryIdentifier> destSuperEntry = entryDataSchema.selectOne(new EntryIdentifier(destEntryClass.getEntryClassId(), destination.getEntryClassId()));
		if (destSuperEntry.isElement()) {
			throw new EvRequestException("destination entryClass specifies an element");
		}
		
		// ensure that entry(Class) where entry should be copied to does not already contain the entry
		if (null != entryDataSchema.selectOne(new EntryIdentifier(destination, source.getShortIdentifier()))) {
			throw new EvRequestException("destination entryClass already contains the specified entry");
		}
		
		// get entry that is specified by source
		EvEntry<Ev, EntryIdentifier> sourceEntry = entryDataSchema.selectOne(source);
		if (null == sourceEntry) {
			throw new EvRequestException("source entry does not exist");
		}
		
		// construct destinationEntry
		EvEntry<Ev, EntryClassIdentifier> destEntry = new EvEntry<Ev, EntryClassIdentifier>(
				sourceEntry.getEv(),
				destination,
				sourceEntry.getName(),
				sourceEntry.getDescription(),
				sourceEntry.isElement(),
				sourceEntry.getAuthor()
				);
		
		// add|copy entry
		EntryIdentifier entryIdent = new EntryIdentifier(destination, source.getShortIdentifier());
		VoteIdentifier<EntryIdentifier> voteIdent = new VoteIdentifier<EntryIdentifier>(userIdentifier, entryIdent);
		entryDataSchema.insert(destEntry, source.getShortIdentifier(), (float)votes, null);
		entryVoteSchema.vote(voteIdent, true);
		entryCounterSchema.upVote(entryIdent, votes);
		
		// return added|copied entry
		EvEntry<Ev, EntryIdentifier> re = entryDataSchema.selectOne(entryIdent);
		re.getEv().setVote(entryVoteSchema.getBooleanVote(voteIdent));
		return re;
	}

	@Override
	public void voteEntry(UserIdentifier userIdent, EntryIdentifier entryIdent, boolean vote, int votes) throws EvRequestException {
		assert null != userIdent;
		assert null != entryIdent;
		
		entrySchemaGate.vote(new VoteIdentifier<EntryIdentifier>(userIdent, entryIdent), vote, votes);
		
		// update rating of the entry
		entrySchemaGate.updateRating(entryIdent, entryIdent.getShortIdentifier());	// TODO don't do this all the time, delete this line from here
	}

	@Override
	public EvPath getPath(EntryShortIdentifier identifier) {
		assert null != identifier;
		
		return entrySchemaGate.getPath(identifier);
	}

	@Override
	@Deprecated
	public EvEntryContainer listEntriesRanked(UserIdentifier userIdentifier,
			EntryClassIdentifier superIdentifier) {
		assert null != superIdentifier;
		
		return entrySchemaGate.listItems(userIdentifier, superIdentifier);
	}

	@Override
	public EvAttribute<Ev, AttributeIdentifier> addAttribute(
			EvAttribute<EvVoid, EntryClassIdentifier> attribute,
			UserIdentifier userIdentifier, int votes) throws EvRequestException {
		assert null != attribute;
		assert null != userIdentifier;
		
		// ensure that class in which attribute is to add does exist
		EntryClassIdentifier entryClass = entryRegisterSchema.getClassIdentifier(new EntryShortIdentifier(attribute.getIdentifier().getEntryClassId()));
		if (null == entryClass) {
			throw new EvRequestException("entryClass does not exist");
		}
		
		// ensure that entryClass describes a directory and not an element
		EvEntry<Ev, EntryIdentifier> entry = entryDataSchema.selectOne(new EntryIdentifier(entryClass.getEntryClassId(), attribute.getIdentifier().getEntryClassId()));
		if (entry.isElement()) {
			throw new EvRequestException("entryClass specifies an element and not a directory");
		}
		
		AttributeShortIdentifier attributeShortIdent = attributeDataSchema.generateNewId();
		AttributeIdentifier attributeIdent = new AttributeIdentifier(attribute.getIdentifier(), attributeShortIdent);
		VoteIdentifier<AttributeIdentifier> voteIdent = new VoteIdentifier<AttributeIdentifier>(userIdentifier, attributeIdent);
		
		attributeDataSchema.insert(attribute, attributeShortIdent, (float)votes, null);
		attributeVoteSchema.vote(voteIdent, true);
		attributeCounterSchema.upVote(attributeIdent, votes);
		
		// return added attribute
		EvAttribute<Ev, AttributeIdentifier> re = attributeDataSchema.selectOne(attributeIdent);
		re.getEv().setVote(attributeVoteSchema.getBooleanVote(voteIdent));
		return re;
	}
	
	@Override
	public EvAttribute<Ev, AttributeIdentifier> addExistingAttribute(
			EvAttribute<EvVoid, AttributeIdentifier> attribute,
			UserIdentifier userIdentifier, int votes) throws EvRequestException {
		assert null != attribute;
		assert null != userIdentifier;
		
		AttributeIdentifier attributeIdent = attribute.getIdentifier();
		
		// ensure that class in which attribute is to add does exist
		EntryClassIdentifier entryClass = entryRegisterSchema.getClassIdentifier(new EntryShortIdentifier(attributeIdent.getEntryClassId()));
		if (null == entryClass) {
			throw new EvRequestException("entryClass does not exist");
		}
		
		// ensure that entryClass describes a directory and not an element
		EvEntry<Ev, EntryIdentifier> entry = entryDataSchema.selectOne(new EntryIdentifier(entryClass.getEntryClassId(), attributeIdent.getEntryClassId()));
		if (entry.isElement()) {
			throw new EvRequestException("entryClass specifies an element and not a directory");
		}
		
		// ensure that attribute exists (in arbitrary (other) directory|entryClass)
		if (!attributeDataSchema.exists(attributeIdent.getShortIdentifier())) {
			throw new EvRequestException("attribute with given attributeIdentifier does not exist");
		}
		
		// ensure that entryClass does not already have the attribute
		if (attributeDataSchema.exists(attributeIdent)) {
			throw new EvRequestException("specified entryClass|directory already has the attribute");
		}
		
		VoteIdentifier<AttributeIdentifier> voteIdent = new VoteIdentifier<AttributeIdentifier>(userIdentifier, attributeIdent);
		
		attributeDataSchema.insert(EvAttribute.containerCast(attribute), attributeIdent.getShortIdentifier(), (float)votes, null);
		attributeVoteSchema.vote(voteIdent, true);
		attributeCounterSchema.upVote(attributeIdent, votes);
		
		// return added attribute
		EvAttribute<Ev, AttributeIdentifier> re = attributeDataSchema.selectOne(attributeIdent);
		re.getEv().setVote(attributeVoteSchema.getBooleanVote(voteIdent));
		return re;
	}

	@Override
	public void voteAttribute(UserIdentifier userIdent,
			AttributeIdentifier attributeIdent, boolean vote, int votes)
			throws EvRequestException {
		assert null != userIdent;
		assert null != attributeIdent;
		
		attributeSchemaGate.vote(new VoteIdentifier<AttributeIdentifier>(userIdent, attributeIdent), vote, votes);
		
		// update rating of the attribute
		attributeSchemaGate.updateRating(attributeIdent, attributeIdent.getShortIdentifier());	// TODO don't do this all the time, delete this line from here
		
	}

	@Override
	@Deprecated
	public EvAttributeContainer listAttributesRanked(
			UserIdentifier userIdentifier, EntryClassIdentifier superIdentifier) {
		assert null != superIdentifier;
		
		return attributeSchemaGate.listItems(userIdentifier, superIdentifier);
	}

	@Override
	public EvEntryPackage getStartEntryPackage(UserIdentifier userIdentifier) {
		return getEntryPackage(userIdentifier, new EntryClassIdentifier(EntryRegisterSchema.ROOT_ID));
	}
	
	@Override
	public EvEntryPackage getEntryPackage(UserIdentifier userIdentifier,
			EntryClassIdentifier superIdentifier) {
		assert null != superIdentifier;
		
		EntryClassIdentifier superClassIdentifier = entryRegisterSchema.getClassIdentifier(new EntryShortIdentifier(superIdentifier.getEntryClassId()));
		
		EvEntry<Ev, EntryIdentifier> entry = entrySchemaGate.getFullEntry(
				userIdentifier, 
				new EntryIdentifier(superClassIdentifier.getEntryClassId(), superIdentifier.getEntryClassId())
				);
		
		EvEntryContainer eEntryContainer = entrySchemaGate.listItems(userIdentifier, superIdentifier);
		
		return new EvEntryPackage(entry, eEntryContainer);
	}
	
	@Override
	public EvElement getElement(UserIdentifier userIdentifier, EntryClassIdentifier classIdentifier, EntryShortIdentifier valueIdentifier) {
		assert null != classIdentifier;
		assert null != valueIdentifier;
		
		EntryClassIdentifier entryClassIdentifier = entryRegisterSchema.getClassIdentifier(valueIdentifier);
		
		EvEntry<Ev, EntryIdentifier> entry = entrySchemaGate.getFullEntry(
				userIdentifier, 
				new EntryIdentifier(entryClassIdentifier, valueIdentifier)
				);
		
		// attributes of entryClass
		EvAttributeContainer eAttributeContainer = attributeSchemaGate.listItems(userIdentifier, classIdentifier);
		
		// criteria of entryClass
		EvCriterionContainer eCriterionContainer = criterionSchemaGate.listItems(userIdentifier, classIdentifier);
		
		// media i.a. images of entry
		EvMediumContainer eMediumContainer = mediumSchemaGate.listItems(userIdentifier, new EntryClassIdentifier(valueIdentifier.getEntryId()));
		
		// comments of entry
		EvCommentContainer eCommentContainer = commentSchemaGate.listItems(userIdentifier, new EntryClassIdentifier(valueIdentifier.getEntryId()));
		
		// construct element
		EvElement eElement = new EvElement(entry, eAttributeContainer, eMediumContainer, eCriterionContainer, eCommentContainer);
		
		// attributeValues of entry
		eElement.addAttributeValueContainer(attributeTopValueDataSchema.listValuesOfEntryRated(new EntryClassIdentifier(valueIdentifier.getEntryId())));
		
		// criterionValues of entry
		eElement.addCriterionValueContainer(criterionValueSchemaGate.listItems(userIdentifier, new EntryClassIdentifier(valueIdentifier.getEntryId())));
		
		return eElement;
	}

	@Override
	public EvAttributeValue<Ev, AttributeValueIdentifier> addAttributeValue(
			EvAttributeValue<EvVoid, AttributeValueIdentifier> attributeValue,
			UserIdentifier userIdentifier, int votes) throws EvRequestException {
		assert null != attributeValue;
		assert null != userIdentifier;
		
		// get entryClassIdentifier of the entry where attributeValue should be added
		EntryClassIdentifier entryClassIdent = entryRegisterSchema.getClassIdentifier(new EntryShortIdentifier(attributeValue.getIdentifier().getEntryClassId()));
		if (null == entryClassIdent) {
			throw new EvRequestException("entry does not exist");
		}
		
		// check if attributeIdentifier describes an element and not a directory | class of entries
		if (!entryDataSchema.selectOne(new EntryIdentifier(entryClassIdent.getEntryClassId(), attributeValue.getIdentifier().getEntryClassId())).isElement()) {
			throw new EvRequestException("entry does not specify an element");
		}
		
		// construct attributeIdentifier of the entryClass
		AttributeIdentifier attributeClassIdent = new AttributeIdentifier(entryClassIdent, attributeValue.getIdentifier().getAttributeShortIdentifier());
		
		// ensure that entryClass has the attribute
		if (!attributeDataSchema.exists(attributeClassIdent)) {
			throw new EvRequestException("attribute does not fit to entry");
		}
		
		// handle if attributeValue does already exist
		if (attributeValueDataSchema.exists(attributeValue.getIdentifier())) {
			// operate upVote if attributeValue does already exist	// TODO FIXME this is bad style (misuse of Exception)
			attributeValueSchemaGate.vote(
					new VoteIdentifier<AttributeValueIdentifier>(userIdentifier, attributeValue.getIdentifier()), 
					true, 
					votes);	// ?TODO FIXME? maybe not use parameter votes (it's for the initial-value not for common upVote)
			throw new EvRequestException("attributeValue does already exists, upVote operated");
		}
		
		// add attributeValue
		VoteIdentifier<AttributeValueIdentifier> voteIdent = new VoteIdentifier<AttributeValueIdentifier>(userIdentifier, attributeValue.getIdentifier());
		attributeValueDataSchema.insert(
				new EvAttributeValue<EvVoid, AttributeIdentifier>(
						attributeValue.getEv(), 
						attributeValue.getIdentifier().getAttributeIdentifier(), 
						attributeValue.getAuthor()),	// TODO FIXME is there really no way to operate an upcast? 
				attributeValue.getIdentifier().getShortIdentifier(), 
				(float)votes, 
				null);
		attributeValueVoteSchema.vote(voteIdent, true);
		attributeValueCounterSchema.upVote(attributeValue.getIdentifier(), votes);
		
		// check if update of topValue is necessary and if so do so (is important because it may be that this is the first suggestion)
		updateAttributeTopValue(attributeValue.getIdentifier().getAttributeIdentifier());
		
		// return added attributeValue
		EvAttributeValue<Ev, AttributeValueIdentifier> re = attributeValueDataSchema.selectOne(attributeValue.getIdentifier());
		re.getEv().setVote(attributeValueVoteSchema.getBooleanVote(voteIdent));
		return re;
	}

	@Override
	public void voteAttributeValue(UserIdentifier userIdent,
			AttributeValueIdentifier attributeValueIdent, boolean vote,
			int votes) throws EvRequestException {
		assert null != userIdent;
		assert null != attributeValueIdent;
		
		attributeValueSchemaGate.vote(new VoteIdentifier<AttributeValueIdentifier>(userIdent, attributeValueIdent), vote, votes);
		
		// update rating of the attributeValue and topValue
		attributeValueSchemaGate.updateRating(attributeValueIdent, attributeValueIdent.getShortIdentifier());	// TODO don't do this all the time, delete this line from here
		updateAttributeTopValue(attributeValueIdent.getAttributeIdentifier());	// TODO don't do this all the time, delete this line from here
	}

	@Override
	public EvAttributeValueContainer getAttributeValueContainer(
			UserIdentifier userIdent, AttributeIdentifier attributeIdent) {
		assert null != attributeIdent;
		
		return attributeValueSchemaGate.listItems(userIdent, attributeIdent);
	}

	@Override
	public void updateAttributeTopValue(AttributeIdentifier attributeIdent) {
		assert null != attributeIdent;
		
		EvAttributeValue<Ev, AttributeValueIdentifier> topValue = attributeValueDataSchema.getTopRatedItem(attributeIdent);
		
		if (null != topValue) {
		
			// only update when topValue has changed
			IdAttributeTopValue<AttributeIdentifier> topValueBefore = attributeTopValueDataSchema.selectOne(attributeIdent);
			if (null == topValueBefore || topValue.getIdentifier().getValueId() != topValueBefore.getValue()) {
				
				// get entryClassIdentifier of the entry where attributeTopValue should be updated
				EntryClassIdentifier entryClassIdent = entryRegisterSchema.getClassIdentifier(new EntryShortIdentifier(attributeIdent.getEntryClassId()));
				
				// construct attributeIdentifier of the entryClass
				AttributeIdentifier attributeClassIdent = new AttributeIdentifier(entryClassIdent, attributeIdent.getShortIdentifier());
				
				// get rating of attribute in order to can operate an update statement
				EvAttribute<Ev, AttributeIdentifier> attribute = attributeDataSchema.selectOne(attributeClassIdent);
				
				// update the topValue of the attribute
				attributeTopValueDataSchema.update(
						new IdAttributeTopValue<AttributeIdentifier>(topValue.getIdentifier().getAttributeIdentifier(), topValue.getIdentifier().getValueId()),
						attribute.getEv().getRating()					
						);
			}
		}
	}

	@Override
	public EvMedium<Ev, MediumIdentifier> addMedium(EvMedium<EvVoid, EntryClassIdentifier> medium,
			UserIdentifier userIdentifier, int votes) throws EvRequestException {
		assert null != medium;
		assert null != userIdentifier;
		
		// ensure that entry where medium is to add does exist
		if (!entryRegisterSchema.exists(new EntryShortIdentifier(medium.getIdentifier().getEntryClassId()))) {
			throw new EvRequestException("entry where medium should be added does not exist");
		}
		
		MediumShortIdentifier mediumShortIdent = mediumDataSchema.generateNewId();
		MediumIdentifier mediumIdent = new MediumIdentifier(medium.getIdentifier(), mediumShortIdent);
		VoteIdentifier<MediumIdentifier> voteIdent = new VoteIdentifier<MediumIdentifier>(userIdentifier, mediumIdent);
		
		mediumDataSchema.insert(medium, mediumShortIdent, (float)votes, null);
		mediumVoteSchema.vote(voteIdent, true);
		mediumCounterSchema.upVote(mediumIdent, votes);
		
		// return added medium
		EvMedium<Ev, MediumIdentifier> re = mediumDataSchema.selectOne(mediumIdent);
		re.getEv().setVote(mediumVoteSchema.getBooleanVote(voteIdent));
		return re;
	}

	@Override
	public void voteMedium(UserIdentifier userIdent,
			MediumIdentifier mediumIdent, boolean vote, int votes)
			throws EvRequestException {
		assert null != userIdent;
		assert null != mediumIdent;
		
		mediumSchemaGate.vote(new VoteIdentifier<MediumIdentifier>(userIdent, mediumIdent), vote, votes);
		
		// update rating of the medium
		mediumSchemaGate.updateRating(mediumIdent, mediumIdent.getShortIdentifier());	// TODO don't do this all the time, delete this line from here
		
	}

	@Override
	public EvCriterion<Ev, CriterionIdentifier> addCriterion(
			EvCriterion<EvVoid, EntryClassIdentifier> criterion,
			UserIdentifier userIdentifier, int votes) throws EvRequestException {
		assert null != criterion;
		assert null != userIdentifier;
		
		// ensure that class in which criterion is to add does exist
		EntryClassIdentifier entryClass = entryRegisterSchema.getClassIdentifier(new EntryShortIdentifier(criterion.getIdentifier().getEntryClassId()));
		if (null == entryClass) {
			throw new EvRequestException("entryClass does not exist");
		}
		
		// ensure that entryClass describes a directory and not an element
		EvEntry<Ev, EntryIdentifier> entry = entryDataSchema.selectOne(new EntryIdentifier(entryClass.getEntryClassId(), criterion.getIdentifier().getEntryClassId()));
		if (entry.isElement()) {
			throw new EvRequestException("entryClass specifies an element and not a directory");
		}
		
		CriterionShortIdentifier criterionShortIdent = criterionDataSchema.generateNewId();
		CriterionIdentifier criterionIdent = new CriterionIdentifier(criterion.getIdentifier(), criterionShortIdent);
		VoteIdentifier<CriterionIdentifier> voteIdent = new VoteIdentifier<CriterionIdentifier>(userIdentifier, criterionIdent);
		
		criterionDataSchema.insert(criterion, criterionShortIdent, (float)votes, null);
		criterionVoteSchema.vote(voteIdent, true);
		criterionCounterSchema.upVote(criterionIdent, votes);
		
		// return added criterion
		EvCriterion<Ev, CriterionIdentifier> re = criterionDataSchema.selectOne(criterionIdent);
		re.getEv().setVote(criterionVoteSchema.getBooleanVote(voteIdent));
		return re;
	}
	
	@Override
	public EvCriterion<Ev, CriterionIdentifier> addExistingCriterion(
			CriterionIdentifier sourceCriterion,
			EvCriterion<EvVoid, CriterionIdentifier> destinationCriterion,
			UserIdentifier userIdentifier, int votes) throws EvRequestException {
		assert null != sourceCriterion;
		assert null != destinationCriterion;
		assert null != userIdentifier;
		
		CriterionIdentifier criterionIdent = destinationCriterion.getIdentifier();
		
		// ensure that destinationCriterion matches to sourceCriterion
		if (!criterionIdent.getShortIdentifier().getShortId().equals(sourceCriterion.getShortIdentifier().getShortId())) {
			throw new EvRequestException("destinationCriterion does not match to sourceCriterion");
		}
		
		// ensure that class in which criterion is to add does exist
		EntryClassIdentifier entryClass = entryRegisterSchema.getClassIdentifier(new EntryShortIdentifier(criterionIdent.getEntryClassId()));
		if (null == entryClass) {
			throw new EvRequestException("entryClass does not exist");
		}
		
		// ensure that entryClass describes a directory and not an element
		EvEntry<Ev, EntryIdentifier> entry = entryDataSchema.selectOne(new EntryIdentifier(entryClass.getEntryClassId(), criterionIdent.getEntryClassId()));
		if (entry.isElement()) {
			throw new EvRequestException("entryClass specifies an element and not a directory");
		}
		
		// ensure that sourceCriterion exists
		EvCriterion<Ev, CriterionIdentifier> sourceCriterionItem = criterionDataSchema.selectOne(sourceCriterion);
		if (null == sourceCriterionItem) {
			throw new EvRequestException("sourceCriterion does not exist");
		}
		
		// ensure that entryClass does not already have the criterion
		if (criterionDataSchema.exists(criterionIdent)) {
			throw new EvRequestException("specified entryClass|directory already has the criterion");
		}
		
		// ensure that worst and best are equal to the original criterion
		if (sourceCriterionItem.getWorst() != destinationCriterion.getWorst()) {
			throw new EvRequestException("the worst value of the criterion must not differ from the source criterion");
		}
		if (sourceCriterionItem.getBest() != destinationCriterion.getBest()) {
			throw new EvRequestException("the best value of the criterion must not differ from the source criterion");
		}
		
		VoteIdentifier<CriterionIdentifier> voteIdent = new VoteIdentifier<CriterionIdentifier>(userIdentifier, criterionIdent);
		
		criterionDataSchema.insert(EvCriterion.containerCast(destinationCriterion), criterionIdent.getShortIdentifier(), (float)votes, null);
		criterionVoteSchema.vote(voteIdent, true);
		criterionCounterSchema.upVote(criterionIdent, votes);
		
		// return added criterion
		EvCriterion<Ev, CriterionIdentifier> re = criterionDataSchema.selectOne(criterionIdent);
		re.getEv().setVote(criterionVoteSchema.getBooleanVote(voteIdent));
		return re;
	}

	@Override
	public void voteCriterion(UserIdentifier userIdent,
			CriterionIdentifier criterionIdent, boolean vote, int votes)
			throws EvRequestException {
		assert null != userIdent;
		assert null != criterionIdent;
		
		criterionSchemaGate.vote(new VoteIdentifier<CriterionIdentifier>(userIdent, criterionIdent), vote, votes);
		
		// update rating of the criterion
		criterionSchemaGate.updateRating(criterionIdent, criterionIdent.getShortIdentifier());	// TODO don't do this all the time, delete this line from here
		
	}

	@Override
	public void voteCriterionValue(UserIdentifier userIdent,
			CriterionIdentifier criterionValueIdent, int vote, int votes)
			throws EvRequestException {
		assert null != userIdent;
		assert null != criterionValueIdent;
		
		criterionValueSchemaGate.vote(new VoteIdentifier<CriterionIdentifier>(userIdent, criterionValueIdent), vote, votes);
		
	}

	@Override
	public EvComment<Ev, CommentIdentifier> addComment(
			EvComment<EvVoid, EntryClassIdentifier> comment,
			UserIdentifier userIdentifier, int votes) throws EvRequestException {
		assert null != comment;
		assert null != userIdentifier;
		
		// ensure that entry where comment is to add does exist
		if (!entryRegisterSchema.exists(new EntryShortIdentifier(comment.getIdentifier().getEntryClassId()))) {
			throw new EvRequestException("entry where comment should be added does not exist");
		}
		
		CommentShortIdentifier commentShortIdent = commentDataSchema.generateNewId();
		CommentIdentifier commentIdent = new CommentIdentifier(comment.getIdentifier(), commentShortIdent);
		VoteIdentifier<CommentIdentifier> voteIdent = new VoteIdentifier<CommentIdentifier>(userIdentifier, commentIdent);
		
		commentDataSchema.insert(comment, commentShortIdent, (float)votes, null);
		commentVoteSchema.vote(voteIdent, true);
		commentCounterSchema.upVote(commentIdent, votes);
		
		// return added medium
		EvComment<Ev, CommentIdentifier> re = commentDataSchema.selectOne(commentIdent);
		re.getEv().setVote(commentVoteSchema.getBooleanVote(voteIdent));
		return re;
	}

	@Override
	public void voteComment(UserIdentifier userIdent,
			CommentIdentifier commentIdent, boolean vote, int votes)
			throws EvRequestException {
		assert null != userIdent;
		assert null != commentIdent;
		
		commentSchemaGate.vote(new VoteIdentifier<CommentIdentifier>(userIdent, commentIdent), vote, votes);
		
		// update rating of the comment
		commentSchemaGate.updateRating(commentIdent);	// TODO don't do this all the time, delete this line from here
	}
	
}
