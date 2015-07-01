package gov.va.med.srcalc.web.view.admin;

import java.util.ArrayList;
import java.util.List;

import gov.va.med.srcalc.domain.model.ConstantTerm;
import gov.va.med.srcalc.domain.model.RiskModel;
import gov.va.med.srcalc.domain.model.Specialty;

/**
 * A form backing object for editing a target (link @RiskModel) object.
 */
public class EditRiskModel implements Comparable<EditRiskModel>{

	private final RiskModel fRiskModel;

    private String modelName;
    
    // Store the Edit Changes (an 'Import') in a new RiskModel object.
	// If this were a normal editing situation where the user could change individual values through 
	// the gui, this would be separate copy, but since this is an import process, we will just 
	// init this to null until when/if the user imports a new model. 
	// The name may be edited separately and so the edit copy is stored separately.
	// the getter methods will first check will return the importModel values if it has been set and the target model 
	// if not.
    private RiskModel importedModel=null;
    
	protected EditRiskModel( final RiskModel rm ) 
	{
		fRiskModel = rm;
		modelName = rm.getDisplayName();
		
		importedModel = null;		
	}
	
    /**
     * Returns an {@link EditRiskModel} instance for editing the given RiskModel.
     * @param rm the target RiskModel
     */
	public static EditRiskModel fromRiskModel( final RiskModel rm ) 
	{
		return new EditRiskModel( rm );
	}
	
	/**
	 * Return the target (link @RiskModel)
	 */
	public RiskModel getRiskModel() 
	{
		return fRiskModel;
	}
	
	/**
	 * 
	 * Note: this will take the place of most of the setter methods for individual fields.
	 * @param impModel
	 */
//	public void setImportedModel( RiskModel impModel ) 
//	{
//		/// ??? should this also overwrite the possibly edited display name?		
//		importedModel = impModel;
//		importedModel.setDisplayName( modelName );
//	}
	
	/* 
	 * Return the modelName
	 */
	public String getModelName( ) 
	{
		return modelName;
	}

	/*
	 * Set the modelName
	 */
	public void setModelName( String mn ) 
	{
		modelName = mn;
	}

	/**
	 * Return the Id of the target RiskModel. 
	 */
	public String getId( ) 
	{
		return Integer.toString( fRiskModel.getId() );
	}
	
	/**
	 * Return a list of Specialties. 
	 */
	public List<Specialty> getSpecialties() 
	{
		return new ArrayList<Specialty>( );
	}
	

	
	public ConstantTerm getConstantTerm() 
	{
		return ( importedModel == null ? fRiskModel.getConstantTerm() : importedModel.getConstantTerm() );
	}

	
	public int getMaxDisplayNameLength() 
	{
		return RiskModel.DISPLAY_NAME_MAX;
	}
	
	/**
	 * Update the target RiskModel with the current edits.
	 */
	public RiskModel applyChanges() 
	{
		// can't change the model ID

		fRiskModel.setDisplayName( modelName );
		
//		riskModel = importedModel;
		
		return fRiskModel;
	}
	
    @Override
    public int compareTo(final EditRiskModel other )
    {
    	// Order alphabetically by modelName. Second option would be to order by id if we 
    	// wanted a consistent ordering
    	// 
        return this.modelName.compareTo( other.modelName );
    }

	public String toString() 
	{
		return "EditRiskModel: name="+ modelName+",ID="+ getId();
	}	
}
