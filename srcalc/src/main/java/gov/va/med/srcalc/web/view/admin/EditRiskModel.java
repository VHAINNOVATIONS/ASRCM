package gov.va.med.srcalc.web.view.admin;

import java.util.ArrayList;
import java.util.List;

import gov.va.med.srcalc.domain.model.RiskModel;
import gov.va.med.srcalc.domain.model.Specialty;

/**
 * A form backing object for editing a target (link @RiskModel) object.
 */
public class EditRiskModel implements Comparable<EditRiskModel>{

	private final RiskModel fRiskModel;

    private String modelName;
    
//    private RiskModel importedModel; TBD
    
	protected EditRiskModel( final RiskModel rm ) 
	{
		fRiskModel = rm;
		modelName = rm.getDisplayName();
		//id = rm.getId();
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
	
	public List<Specialty> getSpecialties() 
	{
		return new ArrayList<Specialty>( );
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
