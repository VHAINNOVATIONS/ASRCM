package gov.va.med.srcalc.web.view.admin;

import gov.va.med.srcalc.domain.model.ModelTerm;
import gov.va.med.srcalc.domain.model.RiskModel;
import gov.va.med.srcalc.domain.model.Specialty;
import gov.va.med.srcalc.service.InvalidIdentifierException;
import gov.va.med.srcalc.service.ModelInspectionService;
import gov.va.med.srcalc.util.DisplayNameConditions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;

/**
 * A form backing object for editing a target (link @RiskModel) object.
 */
public final class EditRiskModel implements Comparable<EditRiskModel>
{
    private static final Logger fLogger = LoggerFactory.getLogger(EditRiskModel.class);
    
    private String fModelName;
    
    private List<Specialty> fApplicableSpecialties;
    
    private final ArrayList<EditModelTerm> fTerms;
    
    /**
     * Constructs an instance with default values: an empty model name and no terms.
     */
    public EditRiskModel()
    {
        fModelName = "";
        fTerms = new ArrayList<>();
    }
        
    private EditRiskModel(final RiskModel rm, final List<Specialty> applSpecialty)
    {
        fModelName = rm.getDisplayName();
        
        // Create the EditModelTerms list, in TermComparator order per getTerms().
        final ImmutableList<ModelTerm> sortedTerms =
                Ordering.from(new TermComparator()).immutableSortedCopy(rm.getTerms());
        fTerms = new ArrayList<>(sortedTerms.size());
        for (final ModelTerm term : sortedTerms)
        {
            fTerms.add(EditModelTerm.fromExistingTerm(term));
        }

        fApplicableSpecialties = ImmutableList.copyOf(applSpecialty);
    }

    /**
     * Returns an {@link EditRiskModel} instance for editing the given RiskModel.
     * @param riskModel the target RiskModel
     */
    public static EditRiskModel fromRiskModel( final RiskModel riskModel,
            final ModelInspectionService modelService )
    {
        List<Specialty> applSpecialties = new ArrayList<Specialty>();
        fLogger.debug("creating RiskModel {}", riskModel.toString() );
        
        for( Specialty spec : modelService.getAllSpecialties() ) 
        {
            if( spec.getRiskModels().contains( riskModel ) ) 
            {
                applSpecialties.add( spec );
            }
        }
        
        return new EditRiskModel( riskModel, applSpecialties );
    }
    
    /**
     * Return the modelName
     */
    public String getModelName()
    {
        return fModelName;
    }
    
    /**
     * Set the modelName
     */
    public void setModelName(String mn)
    {
        fModelName = mn;
    }
    
    /**
     * Returns the List of EditModelTerm objects allowing modification of the model's
     * terms. Upon construction, this list will be in {@link TermComparator} order. Since
     * the list is mutable, order cannot be guaranteed thereafter.
     * @return a mutable list
     */
    public List<EditModelTerm> getTerms()
    {
        return fTerms;
    }
    
    /**
     * Return a list of Specialties.
     */
    public List<Specialty> getSpecialties()
    {
        return fApplicableSpecialties;
    }
    
    /**
     * the maximum length for a displayName
     * 
     * @return DisplayNameConditions.DISPLAY_NAME_MAX
     */
    public int getMaxDisplayNameLength()
    {
        return DisplayNameConditions.DISPLAY_NAME_MAX;
    }
    
    /**
     * Return a list of {@link ModelTermSummary} objects for the terms.
     * @return a ImmutableList in the same order as {@link #getTerms()}
     */
    public ImmutableList<ModelTermSummary> makeTermSummaries(
            final ModelInspectionService modelService)
    {
        final ArrayList<ModelTermSummary> termSummaries = new ArrayList<>(fTerms.size());
        
        for (final EditModelTerm modTerm : fTerms)
        {
            termSummaries.add(modTerm.makeSummary(modelService));
        }
        
        return ImmutableList.copyOf(termSummaries);
    }

    /**
     * Update the target RiskModel with the current edits.
     * @throws InvalidIdentifierException if any of the terms references a non-existent
     * Variable or Rule
     * @throws IllegalStateException if any of the built terms are duplicates. (They must
     * be put into a Set, so duplicates are not supported.)
     */
    public void applyChanges(
            final RiskModel targetModel,
            final ModelInspectionService modelService)
            throws InvalidIdentifierException
    {
        // can't change the model ID
        
        targetModel.setDisplayName( fModelName );
        
        // Build the new terms.
        final HashSet<ModelTerm> newTerms = new HashSet<>(fTerms.size());
        for (final EditModelTerm term : fTerms)
        {
            final ModelTerm newTerm = term.build(modelService);
            if (!newTerms.add(newTerm))
            {
                throw new IllegalStateException("Duplicate new term " + newTerm);
            }
        }
        targetModel.replaceAllTerms(newTerms);
    }

    @Override
    public int compareTo(final EditRiskModel other)
    {
        // Order alphabetically by modelName.
        //
        return this.fModelName.compareTo(other.fModelName);
    }
    
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("modelName", fModelName)
                .add("terms", fTerms)
                .toString();
    }
}
