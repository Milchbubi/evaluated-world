package eWorld;

import eWorld.database.ApacheCassandraGate;
import eWorld.database.Gate;
import eWorld.datatypes.data.EvAttribute;
import eWorld.datatypes.data.EvAttributeValue;
import eWorld.datatypes.data.EvCriterion;
import eWorld.datatypes.data.EvEntry;
import eWorld.datatypes.data.EvMedium;
import eWorld.datatypes.elementars.WoString;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.evs.EvVoid;
import eWorld.datatypes.exceptions.EvRequestException;
import eWorld.datatypes.identifiers.AttributeIdentifier;
import eWorld.datatypes.identifiers.AttributeValueIdentifier;
import eWorld.datatypes.identifiers.AttributeValueShortIdentifier;
import eWorld.datatypes.identifiers.CriterionIdentifier;
import eWorld.datatypes.identifiers.EntryClassIdentifier;
import eWorld.datatypes.identifiers.EntryIdentifier;
import eWorld.datatypes.identifiers.UserIdentifier;

/**
 * 
 * @author michael
 * TODO? outsource this class in another project?
 */
public class Main {

	// finals
	
//	private static final String DATABASE_NODE = "127.0.0.1";
	
	
	// attributes
	
//	private static final Gate DB_GATE = new ApacheCassandraGate(DATABASE_NODE);
	
	
	// methods
	
	public static void main(String[] args) {
		
		// open shell
//		Shell shell = new Shell(DB_GATE);
//		shell.doCommands();
		
		// fill database
//		try {
//			fillDB();
//		} catch (EvRequestException e) {
//			e.printStackTrace();
//		}
//		
//		DB_GATE.closeDatabase();
	}

	public static void fillDB(Gate DB_GATE) throws EvRequestException {
		UserIdentifier userIdent = new UserIdentifier(42);
		
		EvEntry<Ev, EntryIdentifier> rootEntry = DB_GATE.getRootEntry();
		EntryClassIdentifier rootIdent = new EntryClassIdentifier(rootEntry.getIdentifier().getEntryId());
		
		// add some criteria to root
		EvCriterion<Ev, CriterionIdentifier> recommendation = 
				DB_GATE.addCriterion(
						new EvCriterion<EvVoid, EntryClassIdentifier>(
								EvVoid.INST, rootIdent, new WoString("Recommendation"), 
								new WoString("How much do you recommend the article?"), 1, 10, userIdent), 
						userIdent, 150);
		
		// add some entries to root
		EvEntry<Ev,EntryIdentifier> films = 
				DB_GATE.addEntry(new EvEntry<EvVoid, EntryClassIdentifier>(EvVoid.INST, rootIdent, new WoString("Films"), null, false, userIdent), userIdent, 150);
		EvEntry<Ev,EntryIdentifier> computers = 
				DB_GATE.addEntry(new EvEntry<EvVoid, EntryClassIdentifier>(EvVoid.INST, rootIdent, new WoString("Computers"), null, false, userIdent), userIdent, 130);
		EvEntry<Ev,EntryIdentifier> software = 
				DB_GATE.addEntry(new EvEntry<EvVoid, EntryClassIdentifier>(EvVoid.INST, rootIdent, new WoString("Software"), null, false, userIdent), userIdent, 120);
		EvEntry<Ev,EntryIdentifier> games = 
				DB_GATE.addEntry(new EvEntry<EvVoid, EntryClassIdentifier>(EvVoid.INST, rootIdent, new WoString("Games"), null, false, userIdent), userIdent, 100);
		EvEntry<Ev,EntryIdentifier> books = 
				DB_GATE.addEntry(new EvEntry<EvVoid, EntryClassIdentifier>(EvVoid.INST, rootIdent, new WoString("Books"), null, false, userIdent), userIdent, 70);
		EvEntry<Ev,EntryIdentifier> music = 
				DB_GATE.addEntry(new EvEntry<EvVoid, EntryClassIdentifier>(EvVoid.INST, rootIdent, new WoString("Music"), null, false, userIdent), userIdent, 60);
		EvEntry<Ev,EntryIdentifier> television = 
				DB_GATE.addEntry(new EvEntry<EvVoid, EntryClassIdentifier>(EvVoid.INST, rootIdent, new WoString("Television"), null, false, userIdent), userIdent, 50);
		EvEntry<Ev,EntryIdentifier> food = 
				DB_GATE.addEntry(new EvEntry<EvVoid, EntryClassIdentifier>(EvVoid.INST, rootIdent, new WoString("Food"), null, false, userIdent), userIdent, 40);
		
		EvAttribute<Ev, AttributeIdentifier> attrProdCompany;
		EvAttribute<Ev, AttributeIdentifier> attrAuthor;
		
		EvCriterion<Ev, CriterionIdentifier> critThrilling;
		EvCriterion<Ev, CriterionIdentifier> critIntellectualLevel;
		EvCriterion<Ev, CriterionIdentifier> critHumor;
		EvCriterion<Ev, CriterionIdentifier> critEmotionalIntensity;
		
		{	// films
			EntryClassIdentifier filmsIdent = new EntryClassIdentifier(films.getIdentifier().getEntryId());
			
			DB_GATE.addMedium(
					new EvMedium<EvVoid, EntryClassIdentifier>(
							EvVoid.INST, filmsIdent, 
							new String("http://upload.wikimedia.org/wikipedia/en/e/e7/Video-x-generic.svg"), 
							null, userIdent), 
					userIdent, 20);
			
			// add some attributes to films
			EvAttribute<Ev, AttributeIdentifier> attrDirected = DB_GATE.addAttribute(
					new EvAttribute<EvVoid, EntryClassIdentifier>(
							EvVoid.INST, filmsIdent, new WoString("Directed by"), new WoString("The person who directed the film."), userIdent), userIdent, 100);
			AttributeIdentifier attrDirectedIdent = attrDirected.getIdentifier();
			
			EvAttribute<Ev, AttributeIdentifier> attrProduced = DB_GATE.addAttribute(
					new EvAttribute<EvVoid, EntryClassIdentifier>(
							EvVoid.INST, filmsIdent, new WoString("Produced by"), new WoString("The person who produced the film."), userIdent), userIdent, 100);
			AttributeIdentifier attrProducedIdent = attrProduced.getIdentifier();
			
			attrAuthor = DB_GATE.addAttribute(
					new EvAttribute<EvVoid, EntryClassIdentifier>(
							EvVoid.INST, filmsIdent, new WoString("Written by"), new WoString("The person who wrote the screenplay."), userIdent), userIdent, 100);
			AttributeIdentifier attrWrittenIdent = attrAuthor.getIdentifier();
			
			EvAttribute<Ev, AttributeIdentifier> attrMusic= DB_GATE.addAttribute(
					new EvAttribute<EvVoid, EntryClassIdentifier>(EvVoid.INST, filmsIdent, new WoString("Music by"), null, userIdent), userIdent, 90);
			AttributeIdentifier attrMusicIdent = attrMusic.getIdentifier();
			
			EvAttribute<Ev, AttributeIdentifier> attrStarring = DB_GATE.addAttribute(
					new EvAttribute<EvVoid, EntryClassIdentifier>(
							EvVoid.INST, filmsIdent, new WoString("Starring"), new WoString("The main characters of the film."), userIdent), userIdent, 95);
			AttributeIdentifier attrStarringIdent = attrStarring.getIdentifier();
			
			attrProdCompany = DB_GATE.addAttribute(
					new EvAttribute<EvVoid, EntryClassIdentifier>(EvVoid.INST, filmsIdent, new WoString("Production company"), null, userIdent), userIdent, 80);
			AttributeIdentifier attrProdCompanyIdent = attrProdCompany.getIdentifier();
			
			EvAttribute<Ev, AttributeIdentifier> attrDistributed = DB_GATE.addAttribute(
					new EvAttribute<EvVoid, EntryClassIdentifier>(
							EvVoid.INST, filmsIdent, new WoString("Distributed by"), new WoString("The company which distributes the film."), userIdent), userIdent, 80);
			AttributeIdentifier attrDistributedIdent = attrDistributed.getIdentifier();
			
			EvAttribute<Ev, AttributeIdentifier> attrYear = DB_GATE.addAttribute(
					new EvAttribute<EvVoid, EntryClassIdentifier>(EvVoid.INST, filmsIdent, new WoString("Year of publication"), null, userIdent), userIdent, 70);
			AttributeIdentifier attrYearIdent = attrYear.getIdentifier();
			
			EvAttribute<Ev, AttributeIdentifier> attrRunningTime = DB_GATE.addAttribute(
					new EvAttribute<EvVoid, EntryClassIdentifier>(EvVoid.INST, filmsIdent, new WoString("Running time"), null, userIdent), userIdent, 50);
			AttributeIdentifier attrRunningTimeIdent = attrRunningTime.getIdentifier();
			
			EvAttribute<Ev, AttributeIdentifier> attrBudget = DB_GATE.addAttribute(
					new EvAttribute<EvVoid, EntryClassIdentifier>(EvVoid.INST, filmsIdent, new WoString("Budget"), null, userIdent), userIdent, 50);
			AttributeIdentifier attrBudgetIdent = attrBudget.getIdentifier();
			
			EvAttribute<Ev, AttributeIdentifier> attrBoxOffice = DB_GATE.addAttribute(
					new EvAttribute<EvVoid, EntryClassIdentifier>(EvVoid.INST, filmsIdent, new WoString("Box office"), null, userIdent), userIdent, 50);
			AttributeIdentifier attrBoxOfficeIdent = attrBoxOffice.getIdentifier();
			
			// add some criteria to films
			critThrilling = DB_GATE.addCriterion(new EvCriterion<EvVoid, EntryClassIdentifier>(
					EvVoid.INST, filmsIdent, new WoString("Thrilling"), new WoString("How exciting is the film?"), 1, 10, userIdent), userIdent, 90);
			critIntellectualLevel = DB_GATE.addCriterion(new EvCriterion<EvVoid, EntryClassIdentifier>(
							EvVoid.INST, filmsIdent, new WoString("Intellectual level"), null, 1, 10, userIdent), userIdent, 80);
			critHumor = DB_GATE.addCriterion(new EvCriterion<EvVoid, EntryClassIdentifier>(
					EvVoid.INST, filmsIdent, new WoString("Humor"), null, 1, 10, userIdent), userIdent, 70);
			critEmotionalIntensity = DB_GATE.addCriterion(new EvCriterion<EvVoid, EntryClassIdentifier>(
					EvVoid.INST, filmsIdent, new WoString("Emotional intensity"), null, 1, 10, userIdent), userIdent, 70);
			
			// add some films
			{
				EvEntry<Ev,EntryIdentifier> film = 
						DB_GATE.addEntry(new EvEntry<EvVoid, EntryClassIdentifier>(
								EvVoid.INST, filmsIdent, new WoString("The Hitchhiker's Guide to the Galaxy"), 
								new WoString("About the Question of Life, the Universe, and Everything."), true, userIdent), userIdent, 42);
				DB_GATE.addMedium(
						new EvMedium<EvVoid, EntryClassIdentifier>(
								EvVoid.INST, new EntryClassIdentifier(film.getIdentifier().getEntryId()), 
								new String("https://upload.wikimedia.org/wikipedia/en/thumb/7/7a/Hitchhikers_guide_to_the_galaxy.jpg/220px-Hitchhikers_guide_to_the_galaxy.jpg"), 
								null, userIdent), 
						userIdent, 20);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrDirectedIdent.getAttributeId(), "Garth Jennings"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrProducedIdent.getAttributeId(), 
										"Gary Barber, Roger Birnbaum, Jonathan Glickman, Nick Goldsmith, Jay Roach"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrWrittenIdent.getAttributeId(), 
										"Douglas Adams, Karey Kirkpatrick"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrRunningTimeIdent.getAttributeId(), 
										"110 minutes"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrBudgetIdent.getAttributeId(), 
										"$50 million"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrBoxOfficeIdent.getAttributeId(), 
										"$104.5 million"), 
								userIdent), 
						userIdent, 40);
			}
			{
				EvEntry<Ev,EntryIdentifier> film = 
						DB_GATE.addEntry(new EvEntry<EvVoid, EntryClassIdentifier>(
								EvVoid.INST, filmsIdent, new WoString("Star Wars Episode I: The Phantom Menace"), 
								null, true, userIdent), userIdent, 70);
				DB_GATE.addMedium(
						new EvMedium<EvVoid, EntryClassIdentifier>(
								EvVoid.INST, new EntryClassIdentifier(film.getIdentifier().getEntryId()), 
								new String("https://upload.wikimedia.org/wikipedia/en/thumb/4/40/Star_Wars_Phantom_Menace_poster.jpg/220px-Star_Wars_Phantom_Menace_poster.jpg"), 
								null, userIdent), 
						userIdent, 20);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrDirectedIdent.getAttributeId(), 
										"George Lucas"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrWrittenIdent.getAttributeId(), 
										"George Lucas"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrMusicIdent.getAttributeId(), 
										"John Williams"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrYearIdent.getAttributeId(), 
										"1999"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrRunningTimeIdent.getAttributeId(), 
										"133 minutes"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrBudgetIdent.getAttributeId(), 
										"$115 million"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrBoxOfficeIdent.getAttributeId(), 
										"$1.027 billion"), 
								userIdent), 
						userIdent, 40);
			}
			{
				EvEntry<Ev,EntryIdentifier> film = 
						DB_GATE.addEntry(new EvEntry<EvVoid, EntryClassIdentifier>(
								EvVoid.INST, filmsIdent, new WoString("Star Wars Episode III: Revenge of the Sith"), 
								null, true, userIdent), userIdent, 60);
				DB_GATE.addMedium(
						new EvMedium<EvVoid, EntryClassIdentifier>(
								EvVoid.INST, new EntryClassIdentifier(film.getIdentifier().getEntryId()), 
								new String("https://upload.wikimedia.org/wikipedia/en/thumb/9/93/Star_Wars_Episode_III_Revenge_of_the_Sith_poster.jpg/220px-Star_Wars_Episode_III_Revenge_of_the_Sith_poster.jpg"), 
								null, userIdent), 
						userIdent, 20);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrDirectedIdent.getAttributeId(), 
										"George Lucas"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrWrittenIdent.getAttributeId(), 
										"George Lucas"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrMusicIdent.getAttributeId(), 
										"John Williams"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrProdCompanyIdent.getAttributeId(), 
										"Lucasfilm"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrDistributedIdent.getAttributeId(), 
										"20th Century Fox"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrYearIdent.getAttributeId(), 
										"2005"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrRunningTimeIdent.getAttributeId(), 
										"140 minutes"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrBudgetIdent.getAttributeId(), 
										"$113 million"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrBoxOfficeIdent.getAttributeId(), 
										"$850 million"), 
								userIdent), 
						userIdent, 40);
			}
			{
				EvEntry<Ev,EntryIdentifier> film = 
						DB_GATE.addEntry(new EvEntry<EvVoid, EntryClassIdentifier>(
								EvVoid.INST, filmsIdent, new WoString("Star Wars Episode IV: A New Hope"), 
								null, true, userIdent), userIdent, 60);
				DB_GATE.addMedium(
						new EvMedium<EvVoid, EntryClassIdentifier>(
								EvVoid.INST, new EntryClassIdentifier(film.getIdentifier().getEntryId()), 
								new String("https://upload.wikimedia.org/wikipedia/en/thumb/8/87/StarWarsMoviePoster1977.jpg/220px-StarWarsMoviePoster1977.jpg"), 
								null, userIdent), 
						userIdent, 20);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrDirectedIdent.getAttributeId(), 
										"George Lucas"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrWrittenIdent.getAttributeId(), 
										"George Lucas"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrMusicIdent.getAttributeId(), 
										"John Williams"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrProdCompanyIdent.getAttributeId(), 
										"Lucasfilm"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrDistributedIdent.getAttributeId(), 
										"20th Century Fox"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrYearIdent.getAttributeId(), 
										"1977"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrRunningTimeIdent.getAttributeId(), 
										"121 minutes"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrBudgetIdent.getAttributeId(), 
										"$11 million"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrBoxOfficeIdent.getAttributeId(), 
										"$775 million"), 
								userIdent), 
						userIdent, 40);
			}
			{
				EvEntry<Ev,EntryIdentifier> film = 
						DB_GATE.addEntry(new EvEntry<EvVoid, EntryClassIdentifier>(
								EvVoid.INST, filmsIdent, new WoString("Avatar"), 
								null, true, userIdent), userIdent, 70);
				DB_GATE.addMedium(
						new EvMedium<EvVoid, EntryClassIdentifier>(
								EvVoid.INST, new EntryClassIdentifier(film.getIdentifier().getEntryId()), 
								new String("https://upload.wikimedia.org/wikipedia/en/thumb/b/b0/Avatar-Teaser-Poster.jpg/220px-Avatar-Teaser-Poster.jpg"), 
								null, userIdent), 
						userIdent, 20);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrDirectedIdent.getAttributeId(), 
										"James Cameron"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrWrittenIdent.getAttributeId(), 
										"James Cameron"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrMusicIdent.getAttributeId(), 
										"James Horner"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrProdCompanyIdent.getAttributeId(), 
										"Lightstorm Entertainment, Dune Entertainment, Ingenious Film Partners"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrDistributedIdent.getAttributeId(), 
										"20th Century Fox"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrYearIdent.getAttributeId(), 
										"2009"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrRunningTimeIdent.getAttributeId(), 
										"161 minutes, 178 minutes (extended edition"), 
								userIdent), 
						userIdent, 20);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrBudgetIdent.getAttributeId(), 
										"$237 million"), 
								userIdent), 
						userIdent, 30);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrBoxOfficeIdent.getAttributeId(), 
										"$2.788 billion"), 
								userIdent), 
						userIdent, 30);
			}
			{
				EvEntry<Ev,EntryIdentifier> film = 
						DB_GATE.addEntry(new EvEntry<EvVoid, EntryClassIdentifier>(
								EvVoid.INST, filmsIdent, new WoString("The Terminator"), 
								null, true, userIdent), userIdent, 40);
				DB_GATE.addMedium(
						new EvMedium<EvVoid, EntryClassIdentifier>(
								EvVoid.INST, new EntryClassIdentifier(film.getIdentifier().getEntryId()), 
								new String("https://upload.wikimedia.org/wikipedia/en/thumb/7/70/Terminator1984movieposter.jpg/220px-Terminator1984movieposter.jpg"), 
								null, userIdent), 
						userIdent, 20);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrDirectedIdent.getAttributeId(), 
										"James Cameron"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrWrittenIdent.getAttributeId(), 
										"James Cameron, Gale Anne Hurd"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrProdCompanyIdent.getAttributeId(), 
										"Hemdale, Pacific Western"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrDistributedIdent.getAttributeId(), 
										"Orion Pictures"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrYearIdent.getAttributeId(), 
										"1984"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrRunningTimeIdent.getAttributeId(), 
										"107 minutes"), 
								userIdent), 
						userIdent, 20);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrBudgetIdent.getAttributeId(), 
										"$6.4 million"), 
								userIdent), 
						userIdent, 30);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrBoxOfficeIdent.getAttributeId(), 
										"$78.4 million"), 
								userIdent), 
						userIdent, 30);
			}
			{
				EvEntry<Ev,EntryIdentifier> film = 
						DB_GATE.addEntry(new EvEntry<EvVoid, EntryClassIdentifier>(
								EvVoid.INST, filmsIdent, new WoString("Terminator 2: Judgment Day"), 
								null, true, userIdent), userIdent, 60);
				DB_GATE.addMedium(
						new EvMedium<EvVoid, EntryClassIdentifier>(
								EvVoid.INST, new EntryClassIdentifier(film.getIdentifier().getEntryId()), 
								new String("https://upload.wikimedia.org/wikipedia/en/thumb/8/85/Terminator2poster.jpg/220px-Terminator2poster.jpg"), 
								new WoString("Theatrical release poster"), userIdent), 
						userIdent, 20);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrDirectedIdent.getAttributeId(), 
										"James Cameron"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrWrittenIdent.getAttributeId(), 
										"James Cameron, William Wisher"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrStarringIdent.getAttributeId(), 
										"Arnold Schwarzenegger, Linda Hamilton, Robert Patrick"), 
								userIdent), 
						userIdent, 10);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrDistributedIdent.getAttributeId(), 
										"Tri-Star Pictures"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrYearIdent.getAttributeId(), 
										"1991"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrRunningTimeIdent.getAttributeId(), 
										"136 minutes"), 
								userIdent), 
						userIdent, 20);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrBudgetIdent.getAttributeId(), 
										"$100 million"), 
								userIdent), 
						userIdent, 10);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrBoxOfficeIdent.getAttributeId(), 
										"$520 million"), 
								userIdent), 
						userIdent, 20);
			}
			{
				EvEntry<Ev,EntryIdentifier> film = 
						DB_GATE.addEntry(new EvEntry<EvVoid, EntryClassIdentifier>(
								EvVoid.INST, filmsIdent, new WoString("The Lord of the Rings: The Return of the King"), 
								null, true, userIdent), userIdent, 90);
				DB_GATE.addMedium(
						new EvMedium<EvVoid, EntryClassIdentifier>(
								EvVoid.INST, new EntryClassIdentifier(film.getIdentifier().getEntryId()), 
								new String("https://upload.wikimedia.org/wikipedia/en/9/9d/Lord_of_the_Rings_-_The_Return_of_the_King.jpg"), 
								new WoString("Theatrical release poster"), userIdent), 
						userIdent, 20);
				DB_GATE.addMedium(
						new EvMedium<EvVoid, EntryClassIdentifier>(
								EvVoid.INST, new EntryClassIdentifier(film.getIdentifier().getEntryId()), 
								new String("https://upload.wikimedia.org/wikipedia/en/thumb/2/22/ROTK-Minas-Tirith.jpg/250px-ROTK-Minas-Tirith.jpg"), 
								new WoString("The city of Minas Tirith"), userIdent), 
						userIdent, 20);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrDirectedIdent.getAttributeId(), 
										"Peter Jackson"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrDirectedIdent.getAttributeId(), 
										"Howard Shore"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrProdCompanyIdent.getAttributeId(), 
										"WingNut Films, Saul Zaentz"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrDistributedIdent.getAttributeId(), 
										"New Line Cinema"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrYearIdent.getAttributeId(), 
										"2003"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrRunningTimeIdent.getAttributeId(), 
										"201 minutes"), 
								userIdent), 
						userIdent, 20);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrBudgetIdent.getAttributeId(), 
										"$94 million"), 
								userIdent), 
						userIdent, 10);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrBoxOfficeIdent.getAttributeId(), 
										"$1.12 billion"), 
								userIdent), 
						userIdent, 20);
			}
			{
				EvEntry<Ev,EntryIdentifier> film = 
						DB_GATE.addEntry(new EvEntry<EvVoid, EntryClassIdentifier>(
								EvVoid.INST, filmsIdent, new WoString("The Lord of the Rings: The Two Towers"), 
								null, true, userIdent), userIdent, 80);
				DB_GATE.addMedium(
						new EvMedium<EvVoid, EntryClassIdentifier>(
								EvVoid.INST, new EntryClassIdentifier(film.getIdentifier().getEntryId()), 
								new String("https://upload.wikimedia.org/wikipedia/en/a/ad/Lord_of_the_Rings_-_The_Two_Towers.jpg"), 
								new WoString("Theatrical release poster"), userIdent), 
						userIdent, 20);
				DB_GATE.addMedium(
						new EvMedium<EvVoid, EntryClassIdentifier>(
								EvVoid.INST, new EntryClassIdentifier(film.getIdentifier().getEntryId()), 
								new String("https://upload.wikimedia.org/wikipedia/commons/thumb/f/f1/MtSunday.jpg/220px-MtSunday.jpg"), 
								new WoString("The hill known as Mount Sunday, in Canterbury, New Zealand, provided the location for Edoras."), 
								userIdent), 
						userIdent, 10);
				DB_GATE.addMedium(
						new EvMedium<EvVoid, EntryClassIdentifier>(
								EvVoid.INST, new EntryClassIdentifier(film.getIdentifier().getEntryId()), 
								new String("https://upload.wikimedia.org/wikipedia/en/3/30/Two_Towers-Gollum.jpg"), 
								new WoString("Gollum eating a fish"), 
								userIdent), 
						userIdent, 0);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrDirectedIdent.getAttributeId(), 
										"Peter Jackson"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrDirectedIdent.getAttributeId(), 
										"Howard Shore"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrProdCompanyIdent.getAttributeId(), 
										"WingNut Films, Saul Zaentz"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrDistributedIdent.getAttributeId(), 
										"New Line Cinema"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrYearIdent.getAttributeId(), 
										"2002"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrRunningTimeIdent.getAttributeId(), 
										"179 minutes"), 
								userIdent), 
						userIdent, 20);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrBudgetIdent.getAttributeId(), 
										"$94 million"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrBoxOfficeIdent.getAttributeId(), 
										"$926 million"), 
								userIdent), 
						userIdent, 30);
			}
			{
				EvEntry<Ev,EntryIdentifier> film = 
						DB_GATE.addEntry(new EvEntry<EvVoid, EntryClassIdentifier>(
								EvVoid.INST, filmsIdent, new WoString("The Lord of the Rings: The Fellowship of the Ring"), 
								null, true, userIdent), userIdent, 60);
				DB_GATE.addMedium(
						new EvMedium<EvVoid, EntryClassIdentifier>(
								EvVoid.INST, new EntryClassIdentifier(film.getIdentifier().getEntryId()), 
								new String("https://upload.wikimedia.org/wikipedia/en/thumb/0/0c/The_Fellowship_Of_The_Ring.jpg/220px-The_Fellowship_Of_The_Ring.jpg"), 
								new WoString("Theatrical release poster"), userIdent), 
						userIdent, 20);
				DB_GATE.addMedium(
						new EvMedium<EvVoid, EntryClassIdentifier>(
								EvVoid.INST, new EntryClassIdentifier(film.getIdentifier().getEntryId()), 
								new String("https://upload.wikimedia.org/wikipedia/en/4/40/Fellowship.JPG"), 
								new WoString("The eponymous Fellowship from left to right: (Top row) Aragorn, Gandalf, Legolas, Boromir, (bottom row) Sam, Frodo, Merry, Pippin, Gimli."), 
								userIdent), 
						userIdent, 20);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrDirectedIdent.getAttributeId(), 
										"Peter Jackson"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrDirectedIdent.getAttributeId(), 
										"Howard Shore"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrProdCompanyIdent.getAttributeId(), 
										"WingNut Films, Saul Zaentz"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrDistributedIdent.getAttributeId(), 
										"New Line Cinema"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrYearIdent.getAttributeId(), 
										"2001"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrRunningTimeIdent.getAttributeId(), 
										"178 minutes"), 
								userIdent), 
						userIdent, 20);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrBudgetIdent.getAttributeId(), 
										"$93 million"), 
								userIdent), 
						userIdent, 10);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrBoxOfficeIdent.getAttributeId(), 
										"$872 million"), 
								userIdent), 
						userIdent, 20);
			}
			{
				EvEntry<Ev,EntryIdentifier> film = 
						DB_GATE.addEntry(new EvEntry<EvVoid, EntryClassIdentifier>(
								EvVoid.INST, filmsIdent, new WoString("Interstellar"), 
								null, true, userIdent), userIdent, 70);
				DB_GATE.addMedium(
						new EvMedium<EvVoid, EntryClassIdentifier>(
								EvVoid.INST, new EntryClassIdentifier(film.getIdentifier().getEntryId()), 
								new String("https://upload.wikimedia.org/wikipedia/en/thumb/b/bc/Interstellar_film_poster.jpg/220px-Interstellar_film_poster.jpg"), 
								null, userIdent), 
						userIdent, 20);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrDirectedIdent.getAttributeId(), "Christopher Nolan"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrProducedIdent.getAttributeId(), 
										"Emma Thomas, Christopher Nolan, Lynda Obst"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrWrittenIdent.getAttributeId(), 
										"Jonathan Nolan, Christopher Nolan"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrMusicIdent.getAttributeId(), 
										"Hans Zimmer"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrRunningTimeIdent.getAttributeId(), 
										"169 minutes"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrBudgetIdent.getAttributeId(), 
										"$165 million"), 
								userIdent), 
						userIdent, 40);
				DB_GATE.addAttributeValue(
						new EvAttributeValue<EvVoid, AttributeValueIdentifier>(
								EvVoid.INST, 
								new AttributeValueIdentifier(film.getIdentifier().getEntryId(), attrBoxOfficeIdent.getAttributeId(), 
										"$673 million"), 
								userIdent), 
						userIdent, 40);
			}
		}
		
		EvEntry<Ev,EntryIdentifier> videoGames;
		{	// games
			EntryClassIdentifier gamesIdent = new EntryClassIdentifier(games.getIdentifier().getEntryId());
			
			DB_GATE.addMedium(
					new EvMedium<EvVoid, EntryClassIdentifier>(
							EvVoid.INST, gamesIdent, 
							new String("https://upload.wikimedia.org/wikipedia/commons/thumb/1/1a/Vg_icon.svg/150px-Vg_icon.svg.png"), 
							null, userIdent), 
					userIdent, 20);
			
			// add some criteria to games
			DB_GATE.addCriterion(new EvCriterion<EvVoid, EntryClassIdentifier>(
					EvVoid.INST, gamesIdent, new WoString("Fun"), null, 1, 10, userIdent), userIdent, 70);
			DB_GATE.addCriterion(new EvCriterion<EvVoid, EntryClassIdentifier>(
					EvVoid.INST, gamesIdent, new WoString("Simpleness"), new WoString("How easy is the game to learn?"), 1, 10, userIdent), userIdent, 50);
			DB_GATE.addCriterion(new EvCriterion<EvVoid, EntryClassIdentifier>(
					EvVoid.INST, gamesIdent, new WoString("Contents"), new WoString("Is the content big enough for long gaming fun?"), 1, 10, userIdent), userIdent, 40);
			
			// add some entries to games
			videoGames = 
					DB_GATE.addEntry(new EvEntry<EvVoid, EntryClassIdentifier>(
							EvVoid.INST, gamesIdent, new WoString("Video Games"), 
							null, false, userIdent), userIdent, 50);
			DB_GATE.addMedium(
					new EvMedium<EvVoid, EntryClassIdentifier>(
							EvVoid.INST, new EntryClassIdentifier(videoGames.getIdentifier().getEntryId()), 
							new String("https://upload.wikimedia.org/wikipedia/commons/thumb/1/1a/Vg_icon.svg/150px-Vg_icon.svg.png"), 
							null, userIdent), 
					userIdent, 20);
			
			EvEntry<Ev,EntryIdentifier> boardGames = 
					DB_GATE.addEntry(new EvEntry<EvVoid, EntryClassIdentifier>(
							EvVoid.INST, gamesIdent, new WoString("Board Games"), 
							null, false, userIdent), userIdent, 40);
			
			EvEntry<Ev,EntryIdentifier> outdoorGames = 
					DB_GATE.addEntry(new EvEntry<EvVoid, EntryClassIdentifier>(
							EvVoid.INST, gamesIdent, new WoString("Outdoor Games"), 
							new WoString("Games that usually take place outdoors e.g. sports."), false, userIdent), userIdent, 30);
		}
		
		{	// software
			EntryClassIdentifier softwareIdent = new EntryClassIdentifier(software.getIdentifier().getEntryId());
			
			// add some entries to software
			DB_GATE.copyEntry(videoGames.getIdentifier(), softwareIdent, userIdent, 40);
			
			EvEntry<Ev,EntryIdentifier> officeSoftware = 
					DB_GATE.addEntry(new EvEntry<EvVoid, EntryClassIdentifier>(
							EvVoid.INST, softwareIdent, new WoString("Office Software"), 
							null, false, userIdent), userIdent, 50);
			
			EvEntry<Ev,EntryIdentifier> multimediaSoftware = 
					DB_GATE.addEntry(new EvEntry<EvVoid, EntryClassIdentifier>(
							EvVoid.INST, softwareIdent, new WoString("Multimedia Software"), 
							null, false, userIdent), userIdent, 30);
			
			EvEntry<Ev,EntryIdentifier> operatingSystems = 
					DB_GATE.addEntry(new EvEntry<EvVoid, EntryClassIdentifier>(
							EvVoid.INST, softwareIdent, new WoString("Operating Systems"), 
							null, false, userIdent), userIdent, 30);
		}
		
		{	// computers
			EntryClassIdentifier computersIdent = new EntryClassIdentifier(computers.getIdentifier().getEntryId());
			
			// add some attributes to computers
			DB_GATE.addExistingAttribute(new EvAttribute<EvVoid, AttributeIdentifier>(
					EvVoid.INST, 
					new AttributeIdentifier(computersIdent, attrProdCompany.getIdentifier().getShortIdentifier()), 
					new WoString("Production Company"), null, userIdent),
					userIdent, 140);
			DB_GATE.addAttribute(new EvAttribute<EvVoid, EntryClassIdentifier>(
					EvVoid.INST, computersIdent, new WoString("Processor"), null, userIdent), userIdent, 100);
			DB_GATE.addAttribute(new EvAttribute<EvVoid, EntryClassIdentifier>(
					EvVoid.INST, computersIdent, new WoString("RAM"), null, userIdent), userIdent, 90);
			DB_GATE.addAttribute(new EvAttribute<EvVoid, EntryClassIdentifier>(
					EvVoid.INST, computersIdent, new WoString("Non-volatile memory (NVM)"), null, userIdent), userIdent, 80);
			DB_GATE.addAttribute(new EvAttribute<EvVoid, EntryClassIdentifier>(
					EvVoid.INST, computersIdent, new WoString("Operating System"), null, userIdent), userIdent, 70);
			
			// add some criteria to computers
			
			// add some entries to computers
			EvEntry<Ev,EntryIdentifier> desktopPCs = 
					DB_GATE.addEntry(new EvEntry<EvVoid, EntryClassIdentifier>(
							EvVoid.INST, computersIdent, new WoString("Desktop PCs"), 
							null, false, userIdent), userIdent, 100);
			EvEntry<Ev,EntryIdentifier> laptops = 
					DB_GATE.addEntry(new EvEntry<EvVoid, EntryClassIdentifier>(
							EvVoid.INST, computersIdent, new WoString("Laptops"), 
							null, false, userIdent), userIdent, 90);
			EvEntry<Ev,EntryIdentifier> smartPhones = 
					DB_GATE.addEntry(new EvEntry<EvVoid, EntryClassIdentifier>(
							EvVoid.INST, computersIdent, new WoString("Smartphones"), 
							null, false, userIdent), userIdent, 80);
			EvEntry<Ev,EntryIdentifier> tablets = 
					DB_GATE.addEntry(new EvEntry<EvVoid, EntryClassIdentifier>(
							EvVoid.INST, computersIdent, new WoString("Tablets"), 
							null, false, userIdent), userIdent, 70);
			
			// add some attributes to subClasses of computers
			EvAttribute<Ev, AttributeIdentifier> displaySize = DB_GATE.addAttribute(
					new EvAttribute<EvVoid, EntryClassIdentifier>(
							EvVoid.INST, new EntryClassIdentifier(laptops.getIdentifier().getEntryId()), 
							new WoString("Display size"), null, userIdent),
					userIdent, 120);
			DB_GATE.addExistingAttribute(
					new EvAttribute<EvVoid, AttributeIdentifier>(
							EvVoid.INST, new AttributeIdentifier(smartPhones.getIdentifier().getEntryId(), displaySize.getIdentifier().getAttributeId()), 
							new WoString("Display size"), null, userIdent), 
					userIdent, 120);
			DB_GATE.addExistingAttribute(
					new EvAttribute<EvVoid, AttributeIdentifier>(
							EvVoid.INST, new AttributeIdentifier(tablets.getIdentifier().getEntryId(), displaySize.getIdentifier().getAttributeId()), 
							new WoString("Display size"), null, userIdent), 
					userIdent, 120);
			EvAttribute<Ev, AttributeIdentifier> displayResolution = DB_GATE.addAttribute(
					new EvAttribute<EvVoid, EntryClassIdentifier>(
							EvVoid.INST, new EntryClassIdentifier(laptops.getIdentifier().getEntryId()), 
							new WoString("Display resolution"), null, userIdent),
					userIdent, 110);
			DB_GATE.addExistingAttribute(
					new EvAttribute<EvVoid, AttributeIdentifier>(
							EvVoid.INST, new AttributeIdentifier(smartPhones.getIdentifier().getEntryId(), displayResolution.getIdentifier().getAttributeId()), 
							new WoString("Display resolution"), null, userIdent), 
					userIdent, 110);
			DB_GATE.addExistingAttribute(
					new EvAttribute<EvVoid, AttributeIdentifier>(
							EvVoid.INST, new AttributeIdentifier(tablets.getIdentifier().getEntryId(), displayResolution.getIdentifier().getAttributeId()), 
							new WoString("Display resolution"), null, userIdent), 
					userIdent, 110);
			
			
			// add some criteria to subClasses of computers
			
		}
		
		{	// books
			EntryClassIdentifier booksIdent = new EntryClassIdentifier(books.getIdentifier().getEntryId());
			
			// add some attributes to books
			DB_GATE.addExistingAttribute(
					new EvAttribute<EvVoid, AttributeIdentifier>(
							EvVoid.INST, new AttributeIdentifier(booksIdent.getEntryClassId(), attrAuthor.getIdentifier().getAttributeId()), 
							new WoString("Author"), new WoString("The person who wrote the book."), userIdent),
					userIdent, 120);
			
			// add some criteria to books
			DB_GATE.addExistingCriterion(
					critThrilling.getIdentifier(), 
					new EvCriterion<EvVoid, CriterionIdentifier>(
							EvVoid.INST, new CriterionIdentifier(booksIdent, critThrilling.getIdentifier().getShortIdentifier()), 
							new WoString("Thrilling"), new WoString("How exciting is the book?"), 1, 10, userIdent), 
					userIdent, 90);
			DB_GATE.addExistingCriterion(
					critIntellectualLevel.getIdentifier(), 
					new EvCriterion<EvVoid, CriterionIdentifier>(
							EvVoid.INST, new CriterionIdentifier(booksIdent, critIntellectualLevel.getIdentifier().getShortIdentifier()), 
							new WoString("Intellectual level"), null, 1, 10, userIdent), 
					userIdent, 80);
			DB_GATE.addExistingCriterion(
					critHumor.getIdentifier(), 
					new EvCriterion<EvVoid, CriterionIdentifier>(
							EvVoid.INST, new CriterionIdentifier(booksIdent, critHumor.getIdentifier().getShortIdentifier()), 
							new WoString("Humor"), null, 1, 10, userIdent), 
					userIdent, 60);
			DB_GATE.addExistingCriterion(
					critEmotionalIntensity.getIdentifier(), 
					new EvCriterion<EvVoid, CriterionIdentifier>(
							EvVoid.INST, new CriterionIdentifier(booksIdent, critEmotionalIntensity.getIdentifier().getShortIdentifier()), 
							new WoString("Emotional intensity"), null, 1, 10, userIdent), 
					userIdent, 70);
		}
		
		{	// music
			EntryClassIdentifier musicIdent = new EntryClassIdentifier(music.getIdentifier().getEntryId());
			
			// add some attributes to music
			DB_GATE.addExistingAttribute(
					new EvAttribute<EvVoid, AttributeIdentifier>(
							EvVoid.INST, new AttributeIdentifier(musicIdent.getEntryClassId(), attrAuthor.getIdentifier().getAttributeId()), 
							new WoString("Composer"), null, userIdent),
					userIdent, 120);
			
			// add some criteria to music
			DB_GATE.addCriterion(new EvCriterion<EvVoid, EntryClassIdentifier>(
					EvVoid.INST, musicIdent, new WoString("Harmony"), null, 1, 10, userIdent), userIdent, 70);
			DB_GATE.addExistingCriterion(
					critEmotionalIntensity.getIdentifier(), 
					new EvCriterion<EvVoid, CriterionIdentifier>(
							EvVoid.INST, new CriterionIdentifier(musicIdent, critEmotionalIntensity.getIdentifier().getShortIdentifier()), 
							new WoString("Emotional intensity"), null, 1, 10, userIdent), 
					userIdent, 60);
			DB_GATE.addExistingCriterion(
					critIntellectualLevel.getIdentifier(), 
					new EvCriterion<EvVoid, CriterionIdentifier>(
							EvVoid.INST, new CriterionIdentifier(musicIdent, critIntellectualLevel.getIdentifier().getShortIdentifier()), 
							new WoString("Intellectual level"), null, 1, 10, userIdent), 
					userIdent, 40);
			
		}
		
		{	// television
			EntryClassIdentifier televisionIdent = new EntryClassIdentifier(television.getIdentifier().getEntryId());
			
			// add some attributes to television
			
			// add some criteria to television
			DB_GATE.addCriterion(new EvCriterion<EvVoid, EntryClassIdentifier>(
					EvVoid.INST, televisionIdent, new WoString("Entertaining"), null, 1, 10, userIdent), userIdent, 80);
			DB_GATE.addExistingCriterion(
					critIntellectualLevel.getIdentifier(), 
					new EvCriterion<EvVoid, CriterionIdentifier>(
							EvVoid.INST, new CriterionIdentifier(televisionIdent, critIntellectualLevel.getIdentifier().getShortIdentifier()), 
							new WoString("Intellectual level"), null, 1, 10, userIdent), 
					userIdent, 70);
			
		}
		
		{	// food
			EntryClassIdentifier foodIdent = new EntryClassIdentifier(food.getIdentifier().getEntryId());
			
			// add some attributes to food
			
			// add some criteria to food
			DB_GATE.addCriterion(new EvCriterion<EvVoid, EntryClassIdentifier>(
					EvVoid.INST, foodIdent, new WoString("Taste"), null, 1, 10, userIdent), userIdent, 100);
		}
	}
	
}
