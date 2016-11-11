package eWorld.database.impl.criterionValues;

import java.util.ArrayList;

import eWorld.database.impl.EvSchemaGate;
import eWorld.database.impl.criteria.CriterionDataSchema;
import eWorld.database.impl.entries.EntryRegisterSchema;
import eWorld.datatypes.containers.IdCriterionValueContainer;
import eWorld.datatypes.data.EvCriterion;
import eWorld.datatypes.data.IdCriterionValue;
import eWorld.datatypes.elementars.IntegerVote;
import eWorld.datatypes.elementars.IntegerVotes;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.exceptions.EvRequestException;
import eWorld.datatypes.identifiers.CriterionIdentifier;
import eWorld.datatypes.identifiers.CriterionShortIdentifier;
import eWorld.datatypes.identifiers.EntryClassIdentifier;
import eWorld.datatypes.identifiers.EntryShortIdentifier;
import eWorld.datatypes.identifiers.VoteIdentifier;

public class CriterionValueSchemaGate extends EvSchemaGate<
		CriterionIdentifier,
		EntryClassIdentifier,
		CriterionShortIdentifier,
		Long,
		IdCriterionValue<CriterionIdentifier>,
		IdCriterionValue<EntryClassIdentifier>,
		IdCriterionValue<CriterionShortIdentifier>,
		IdCriterionValueContainer,
		Integer
	> {
	
	// attributes
	
	private final CriterionValueDataSchema dataSchema;
	private final CriterionValueCounterSchema counterSchema;
	private final CriterionValueVoteSchema voteSchema;
	
	private final EntryRegisterSchema entryRegisterSchema;
	private final CriterionDataSchema criterionDataSchema;
	
	
	// constructors
	
	public CriterionValueSchemaGate(
			CriterionValueDataSchema dataSchema,
			CriterionValueCounterSchema counterSchema,
			CriterionValueVoteSchema voteSchema,
			EntryRegisterSchema entryRegisterSchema,
			CriterionDataSchema criterionDataSchema) {
		super(dataSchema, counterSchema, voteSchema);
		
		this.dataSchema = dataSchema;
		this.counterSchema = counterSchema;
		this.voteSchema = voteSchema;
		
		this.entryRegisterSchema = entryRegisterSchema;
		this.criterionDataSchema = criterionDataSchema;
	}
	

	// methods
	
	/**
	 * hands a vote in for the specified CriterionValue
	 * ensures that identifier.getEvIdent() is valid
	 * ensures that vote is in bounds
	 * manages if specified user has already voted for the specified criterionValue
	 * @param identifier specifies the CriterionValue
	 * @param vote the vote
	 * @param votes the number of votes the user has
	 * @throws EvRequestException if identifier is not valid, IllegalArgumentException if vote is out of bounds
	 * TODO? throw always EvRequestException instead of IllegalArgumentException? (Illegal Argument Exception will not be displayed on website)
	 */
	public void vote(VoteIdentifier<CriterionIdentifier> identifier, int vote, int votes) throws EvRequestException {
		assert null != identifier;

		CriterionIdentifier eIdent = identifier.getEvIdent();
		
		// get class of entry
		EntryClassIdentifier classIdentifier = entryRegisterSchema.getClassIdentifier(new EntryShortIdentifier(eIdent.getEntryClassId()));
		
		// get criterion
		EvCriterion<Ev, CriterionIdentifier> criterion = criterionDataSchema.selectOne(new CriterionIdentifier(classIdentifier, eIdent.getShortIdentifier()));
		
		// get previous criterionValue and ev
		CriterionValueDataSchema.CriterionValueAndEv criterionValueAndEv = dataSchema.selectOneCriterionValueAndEv(eIdent);
		
		// if criterionValue in criterionValueDataSchema does not exist, ensure that identifier is valid
		if (null == criterionValueAndEv) {
			
			// ensure that entry exists in entryDataSchema|entryRegisterSchema
			if (!entryRegisterSchema.exists(new EntryShortIdentifier(eIdent.getEntryClassId()))) {
				throw new EvRequestException("entry does not exist");
			}
			
			// ensure that entryClass exists in entryDataSchema|entryRegisterSchema, should always exist when entry does exist
			if (null == classIdentifier) {
				// should never happen
				throw new EvRequestException("something is wrong with the database, class of entry does not exist");
			}
			
			// ensure that criterion exists in criterionDataSchema
			if (null == criterion) {
				throw new EvRequestException("criterion does not exist");
			}
			
			// add criterionValue to (criterionValueCounterSchema,) CriterionValueDataSchema?
			
		}
		
		// ensure that vote is in bounds
		int max = criterion.getBest();
		int min = criterion.getWorst();
		if (min > max) {
			max = criterion.getWorst();
			min = criterion.getBest();
		}
		if (max < vote) {
			throw new IllegalArgumentException("vote should be <= " + max + " but is " + vote);
		}
		if (min > vote) {
			throw new IllegalArgumentException("vote should be >= " + min + " but is " + vote);
		}
		
		// adapt vote and votes if user has already voted
		Integer formerVote = voteSchema.getVote(identifier);
		int voteValue;
		if (null == formerVote) {
			// user has not yet voted
			voteValue = vote * votes;
		} else {
			// user has already voted
			voteValue = (vote - formerVote) * votes;
			votes = 0;
		}
		
		// execute vote
		voteSchema.vote(identifier, vote);
		counterSchema.update(eIdent, voteValue, votes);
		
		IntegerVotes integerVotes = counterSchema.selectOne(eIdent);
		float average = (float)integerVotes.getSum() / (float)integerVotes.getVotes();
		long summarizedVotes = integerVotes.getVotes();
		
		if (null != criterionValueAndEv) {
			// user has already voted, override row in CriterionValueDataSchema
			// TODO do not use insert(..) for this, write update(..) method in CriterionValueDataSchema instead
			dataSchema.insert(new IdCriterionValue<EntryClassIdentifier>(new EntryClassIdentifier(eIdent.getEntryClassId()), average, summarizedVotes), 
					eIdent.getShortIdentifier(), 
					criterionValueAndEv.e.getRating(), 
					criterionValueAndEv.e.getRank());
		} else {
			// user has not voted yet, add row to CriterionValueDataSchema
			dataSchema.insert(
					new IdCriterionValue<EntryClassIdentifier>(new EntryClassIdentifier(eIdent.getEntryClassId()), average, summarizedVotes), 
					eIdent.getShortIdentifier(), 
					criterion.getEv().getRating(), 
					criterion.getEv().getRank());
		}
	}
	
	/**
	 * updates the rating of a row specified by the given identifier
	 * @param identifier
	 */
	public void updateRating(CriterionIdentifier identifier) {
		assert null != identifier;
		
		// TODO
		throw new UnsupportedOperationException("not yet implemented");
		
		// get the new rating from criterionDataSchema(|criterionCounterSchema)
		
		// get the old rating from criterionValueDataSchema
		
		// update if the rating has changed
		
			// insert new row
			
			// delete old row
		
			// check if inserted row exists now
			
	}
	
	
	// overridden methods
	
	@Override
	protected void setVoteToItem(
			IdCriterionValue<CriterionShortIdentifier> item, Integer vote) {
		item.setIndividualVote(new IntegerVote(vote));
	}
	
	@Override
	protected IdCriterionValueContainer constructEvDataTypeContainer(
			EntryClassIdentifier superIdentifier,
			ArrayList<IdCriterionValue<CriterionShortIdentifier>> data) {
		
		return new IdCriterionValueContainer(
				superIdentifier,
				data);
	}
}
