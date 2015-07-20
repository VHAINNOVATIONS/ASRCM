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
    private static final Logger fLogger = LoggerFactory.getLogger(EditSpecialty.class);
    
//    private final Specialty fTargetSpecialty;
    
    private String fName;
    
    private List<SelectableModel> fSelectableModels;
    
    /**
     * A class to store and display a RiskModel that is available to be selected as 
     * an included model for this Specialty.
     */
    public static class SelectableModel
    {
        private Boolean fIncluded = false;
        
        private String fModelName;
        
        private Integer fModelId;
        
        public SelectableModel( )
        {
            fIncluded = false;
            fModelName = "";
            fModelId = 0;
        }

        public SelectableModel( Boolean inc, RiskModel model )
        {
            fIncluded = inc;
            fModelId = model.getId();
            fModelName = model.getDisplayName();
        }
        
        public Boolean getIncluded() 
        {
            return fIncluded;
        }
        
        public void setIncluded( Boolean inc ) 
        {
            fIncluded = inc;
        }
        
        public Integer getModelId() 
        {
            return fModelId;
        }
        
        public void setModelId( Integer modId ) 
        {
            fModelId = modId;
        }
        
        public String getModelName() 
        {
            return fModelName;
        }        

        public void setModelName( String modName )
        {
            fModelName = modName;
        }
    }
    
    /**
     * Constructs an instance with default values..
     */
    public EditSpecialty()
    {
        fName = "";
        fSelectableModels = new ArrayList<SelectableModel>();
    }

    private EditSpecialty(final Specialty sp, List<SelectableModel> selModels )
    {
        fName = sp.getName();
        
        fSelectableModels = ImmutableList.copyOf(selModels);
    }

    /**
     * Returns an {@link EditSpecialty} instance for editing the given target Specialty.
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
                
        for( RiskModel rm : allRiskModels ) 
        {            
            selectableModels.add( 
                    new SelectableModel( includedModels.contains( rm ), rm ) );
            if (includedModels.contains( rm ) )
            {           
                fLogger.debug( "Including Risk Model {} in Specialty.", rm.toString() );
            }
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
     * Update the target Specialty with the current edits.
     */
    public Specialty applyChanges( Specialty existingSpec,
            final ModelInspectionService modelService)
            throws InvalidIdentifierException
    {        
        fLogger.debug("ApplyChanges to name {} : Target {}",
                (fName == null ? "NULL" : fName ), 
                (existingSpec == null ? "NULL" : existingSpec.toString() ));
        
        existingSpec.setName( fName );
        Set<RiskModel> includedRiskModels = new HashSet<RiskModel>();
      
        fLogger.debug("ApplyChanges: There are {} selectableModels", fSelectableModels.size() );

        for( SelectableModel selMod : fSelectableModels )
        {
            fLogger.debug("Model {} is {} included", selMod.getModelName(), (selMod.getIncluded() ? " " : "NOT" ) );

            if( selMod.getIncluded() ) 
            {
                RiskModel rm = modelService.getRiskModelForId( selMod.getModelId() );
                
                if( rm == null ) 
                {
                    throw new InvalidIdentifierException( "Unable to find risk model id = "+selMod.getModelId()+" from the DB");
                }
                
                fLogger.debug("Risk Model {} found and included", rm.toString() );

                includedRiskModels.add( rm );
            }
        }
        existingSpec.setRiskModels( includedRiskModels );
        
        return existingSpec;
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
