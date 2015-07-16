package gov.va.med.srcalc.web.view.admin;

import gov.va.med.srcalc.domain.model.RiskModel;
import gov.va.med.srcalc.domain.model.Specialty;
import gov.va.med.srcalc.service.InvalidIdentifierException;
import gov.va.med.srcalc.service.ModelInspectionService;
import gov.va.med.srcalc.util.DisplayNameConditions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;

public class EditSpecialty implements Comparable<EditSpecialty>
{
    private static final Logger fLogger = LoggerFactory.getLogger(EditRiskModel.class);
    
    private String fName;
    
    private List<SelectableModel> fSelectableModels;
    
    /**
     * A class to store and display a RiskModel that is available to be selected as 
     * an included model for this Specialty.
     */
    public static class SelectableModel
    {
        private Boolean fIncluded = false;
        
        private RiskModel fModel;
        
        public SelectableModel( Boolean inc, RiskModel model )
        {
            fIncluded = inc;
            fModel = model;
        }
        
        public Boolean getIncluded() 
        {
            return fIncluded;
        }
        
        public void setIncluded( Boolean inc ) 
        {
            fIncluded = inc;
        }
        
        public String getModelName() 
        {
            return fModel.getDisplayName();
        }        
    }
    
    /**
     * Constructs an instance with default values: an empty model name and no terms.
     */
    public EditSpecialty()
    {
        fName = "";
    }
        
    private EditSpecialty(final Specialty sp, List<SelectableModel> selModels )
    {
        fName = sp.getName();
        
        fSelectableModels = ImmutableList.copyOf(selModels);
    }

    /**
     * Returns an {@link EditSpecialty} instance for editing the given Specialty.
     * @param spec the target Specialty
     */
    public static EditSpecialty fromSpecialty( final Specialty spec,
            final ModelInspectionService modelService )
    {
        fLogger.debug("creating Specialty {}", spec.toString() );

        List<SelectableModel> selectableModels = new ArrayList<SelectableModel>();
        Set<RiskModel> includedModels = spec.getRiskModels();
        
        List<RiskModel> allRiskModels = new ArrayList<RiskModel>( modelService.getAllRiskModels() );
        Collections.sort( allRiskModels );
        
        fLogger.debug( "There are {} Risk Models in the DB.", allRiskModels.size());
        
        // 
        for( RiskModel rm : allRiskModels ) 
        {            
            selectableModels.add( 
                    new SelectableModel( includedModels.contains( rm ), rm ) );
        }
        
        return new EditSpecialty( spec, selectableModels );
    }
    
    /**
     * Return the specialty name
     */
    public String getName()
    {
        return fName;
    }
    
    /**
     * Set the name
     */
    public void setName(String mn)
    {
        fName = mn;
    }
        
    /**
     * Return a list of includedModels.
     */
    public List<SelectableModel> getSelectableModels()
    {
        return fSelectableModels;
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
     * Update the target Specialty with the current edits.
     */
    public void applyChanges(
            final Specialty targetSpec,
            final ModelInspectionService modelService)
            throws InvalidIdentifierException
    {        
        targetSpec.setName( fName );
        
        // Build the new terms.
//        final HashSet<ModelTerm> newTerms = new HashSet<>(fTerms.size());
//        for (final EditModelTerm term : fTerms)
//        {
//            final ModelTerm newTerm = term.build(modelService);
//            if (!newTerms.add(newTerm))
//            {
//                throw new IllegalStateException("Duplicate new term " + newTerm);
//            }
//        }
//        targetSpec.replaceAllTerms(newTerms);
    }

    @Override
    public int compareTo(final EditSpecialty other)
    {
        // Order alphabetically by Name.
        //
        return this.fName.compareTo(other.fName);
    }
    
    public String toString()
    {
        return String.format("EditSpecialty: %s", fName );
    }

}
