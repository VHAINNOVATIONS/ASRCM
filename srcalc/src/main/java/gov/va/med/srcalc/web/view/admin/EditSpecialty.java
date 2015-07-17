package gov.va.med.srcalc.web.view.admin;

import gov.va.med.srcalc.domain.model.RiskModel;
import gov.va.med.srcalc.domain.model.Specialty;
import gov.va.med.srcalc.service.InvalidIdentifierException;
import gov.va.med.srcalc.service.ModelInspectionService;
import gov.va.med.srcalc.util.DisplayNameConditions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;

public class EditSpecialty implements Comparable<EditSpecialty>
{
    private static final Logger fLogger = LoggerFactory.getLogger(EditRiskModel.class);
    
    private Specialty fTargetSpecialty;
    
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
        
        public SelectableModel( )
        {
            fIncluded = false;
            fModel = null;
        }

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
        
        public RiskModel getModel()
        {
            return fModel;
        }
    }
    /**
     * Constructs an instance with default values: an empty model name and no terms.
     */
    public EditSpecialty()
    {
        fLogger.debug("Calling Default constructor for EditSpecialty");
        fTargetSpecialty = null;
        fName = "";
        fSelectableModels = new ArrayList<SelectableModel>();
    }

    private EditSpecialty(final Specialty sp, List<SelectableModel> selModels )
    {
        fTargetSpecialty = sp;
        fLogger.debug("Calling constructor for EditSpecialty. target is {}  ",
                (fTargetSpecialty == null ? "NULL" : fTargetSpecialty.toString() ) );

        fName = fTargetSpecialty.getName();
        
        fSelectableModels = ImmutableList.copyOf(selModels);
    }

    /**
     * Returns an {@link EditSpecialty} instance for editing the given target Specialty.
     * @param spec the target Specialty
     */
    public static EditSpecialty fromSpecialty( Specialty spec,
            final ModelInspectionService modelService )
    {
        fLogger.debug("creating Specialty {}", spec.toString() );

        List<SelectableModel> selectableModels = new ArrayList<SelectableModel>();
        Set<RiskModel> includedModels = spec.getRiskModels();
        
        List<RiskModel> allRiskModels = new ArrayList<RiskModel>( modelService.getAllRiskModels() );
        Collections.sort( allRiskModels );
                
        for( RiskModel rm : allRiskModels ) 
        {            
            selectableModels.add( 
                    new SelectableModel( includedModels.contains( rm ), rm ) );

            fLogger.debug( "Including Risk Model {} in Specialty.", rm.toString() );
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
     * Return a list of selectable models.
     */
    public List<SelectableModel> getSelectableModels()
    {
        return fSelectableModels;
    }

    /**
     * Sets the list of selectable models.
     */
    public void getSelectableModels( List<SelectableModel> selModelsList )
    {
        fSelectableModels = selModelsList;
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
     * Return the target Specialty
     * 
     */
    public Specialty getTargetSpecialty()
    {
        return fTargetSpecialty;
    }
    /**
     * Update the target Specialty with the current edits.
     */
    public Specialty applyChanges( )
    {        
        fLogger.debug("ApplyChanges to name {} : Target {}",
                (fName == null ? "NULL" : fName ), 
                (fTargetSpecialty == null ? "NULL" : fTargetSpecialty.toString() ));
        
        fTargetSpecialty.setName( fName );
        Set<RiskModel> includedRiskModels = new HashSet<RiskModel>();
        
        for( SelectableModel selMod : fSelectableModels )
        {
            if( selMod.getIncluded() ) 
            {
                includedRiskModels.add( selMod.getModel() );
            }
        }
        fTargetSpecialty.setRiskModels( includedRiskModels );
        
        return fTargetSpecialty;
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
