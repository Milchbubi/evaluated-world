package eWorld.datatypes.packages;

import java.io.Serializable;
import java.util.ArrayList;

import eWorld.datatypes.containers.EvAttributeContainer;
import eWorld.datatypes.containers.EvCommentContainer;
import eWorld.datatypes.containers.EvCriterionContainer;
import eWorld.datatypes.containers.EvMediumContainer;
import eWorld.datatypes.containers.IdAttributeTopValueContainer;
import eWorld.datatypes.containers.IdCriterionValueContainer;
import eWorld.datatypes.data.EvEntry;
import eWorld.datatypes.elementars.WoString;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.identifiers.EntryClassIdentifier;
import eWorld.datatypes.identifiers.EntryIdentifier;

@SuppressWarnings("serial")
public class EvElement extends EvPackage implements Serializable {

	// attributes
	
	private EvAttributeContainer eAttributeContainer;
	
	private ArrayList<IdAttributeTopValueContainer> attributeValueContainers = new ArrayList<IdAttributeTopValueContainer>();
	
	private EvMediumContainer eMediumContainer;
	
	private EvCriterionContainer eCriterionContainer;
	
	private ArrayList<IdCriterionValueContainer> criterionValueContainers = new ArrayList<IdCriterionValueContainer>();
	
	private EvCommentContainer eCommentContainer;
	
	
	// constructors

	/** default constructor for remote procedure call (RPC) */
	public EvElement() {
	}
	
	public EvElement(
			EvEntry<Ev, EntryIdentifier> header,
			EvAttributeContainer eAttributeContainer,
			EvMediumContainer eMediumContainer,
			EvCriterionContainer eCriterionContainer,
			EvCommentContainer eCommentContainer) {
		super(header);
		
		assert null != eAttributeContainer;
		assert null != eMediumContainer;
		assert null != eCriterionContainer;
		assert null != eCommentContainer;
		
		this.eAttributeContainer = eAttributeContainer;
		this.eMediumContainer = eMediumContainer;
		this.eCriterionContainer = eCriterionContainer;
		this.eCommentContainer = eCommentContainer;
	}
	
	
	// methods
	
	public EvAttributeContainer getAttributeContainer() {
		return eAttributeContainer;
	}
	
	public ArrayList<IdAttributeTopValueContainer> getAttributeValueContainers() {
		return attributeValueContainers;
	}
	
	/**
	 * adds the given AttributeValueContainer to the list of attributeValueContainers
	 * @param container should fit (more or less) to eAttributeContainer
	 */
	public void addAttributeValueContainer(IdAttributeTopValueContainer container) {
		assert null != container;
		
		attributeValueContainers.add(container);
	}
	
	public EvMediumContainer getMediumContainer() {
		return eMediumContainer;
	}
	
	public EvCriterionContainer getCriterionContainer() {
		return eCriterionContainer;
	}
	
	public ArrayList<IdCriterionValueContainer> getCriterionValueContainers() {
		return criterionValueContainers;
	}
	
	/**
	 * adds the given CriterionValueContainer to the list of criterionValueContainers
	 * @param container should fit (more or less) to eCriterionContainer
	 */
	public void addCriterionValueContainer(IdCriterionValueContainer container) {
		assert null != container;
		
		criterionValueContainers.add(container);
	}
	
	public EvCommentContainer getCommentContainer() {
		return eCommentContainer;
	}
	
}