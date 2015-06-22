package gov.va.med.srcalc.web.view.admin;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Validator;

import gov.va.med.srcalc.domain.model.RiskModel;
import gov.va.med.srcalc.domain.model.Specialty;

public class EditRiskModel implements Comparable<EditRiskModel>{

	private final RiskModel riskModel;
    private static final Logger fLogger = LoggerFactory.getLogger(EditRiskModel.class);

    private String modelName;
    // can't change the id so just reference the id from the RiskModel
    //private final int id;
    
//    private RiskModel importedModel;
    
	protected EditRiskModel( final RiskModel rm ) 
	{
		riskModel = rm;
		modelName = rm.getDisplayName();
		//id = rm.getId();
	}
	
	public static EditRiskModel fromRiskModel( final RiskModel rm ) 
	{
		return new EditRiskModel( rm );
	}
	
	public RiskModel getRiskModel() 
	{
		return riskModel;
	}
	
	public String getModelName( ) 
	{
		return modelName;
	}

	public void setModelName( String mn ) 
	{
		modelName = mn;
	}

	public String getId( ) 
	{
		return Integer.toString( riskModel.getId() );
	}
	
	public List<Specialty> getSpecialties() 
	{
		return new ArrayList<Specialty>( );
	}
	
	public int getMaxDisplayNameLength() 
	{
		return RiskModel.DISPLAY_NAME_MAX;
	}
	
	public RiskModel applyChanges() 
	{
		// can't change the model ID

		riskModel.setDisplayName( modelName );
		
//		riskModel = importedModel;
		
		return riskModel;
	}
	
    @Override
    public int compareTo(final EditRiskModel other )
    {
    	// Order according to id instead of display name ???
    	// 
//        return this.riskModel.compareTo(other.riskModel);
    	return Integer.compare( this.riskModel.getId(), other.riskModel.getId() );
    }

    /**
     * <p>Returns an appropriate Validator instance for validating this object.
     * </p>
     * 
     * <p>Implementations do not perform their own validation so that the web
     * code can directly bind user input to these beans without fear of
     * Exceptions. We instead provide a Spring Validator via this method to
     * safely ensure that user input meets  class's requirements.</p>
     */
    public /*abstract*/ Validator getValidator()
    {
    	return new EditRiskModelValidator();
    }

	public String toString() 
	{
		return riskModel.toString();
	}	
}
