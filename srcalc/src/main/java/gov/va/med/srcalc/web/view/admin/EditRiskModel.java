package gov.va.med.srcalc.web.view.admin;

import java.util.ArrayList;
//import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;

import gov.va.med.srcalc.domain.model.BooleanTerm;
import gov.va.med.srcalc.domain.model.ConstantTerm;
import gov.va.med.srcalc.domain.model.DerivedTerm;
import gov.va.med.srcalc.domain.model.DiscreteTerm;
import gov.va.med.srcalc.domain.model.ModelTerm;
import gov.va.med.srcalc.domain.model.NumericalTerm;
import gov.va.med.srcalc.domain.model.ProcedureTerm;
import gov.va.med.srcalc.domain.model.RiskModel;
import gov.va.med.srcalc.domain.model.SingleVariableTerm;
import gov.va.med.srcalc.domain.model.Specialty;
import gov.va.med.srcalc.service.AdminService;

/**
 * A form backing object for editing a target (link @RiskModel) object.
 */
public class EditRiskModel implements Comparable<EditRiskModel>{

	private final RiskModel fRiskModel;

    private String modelName;
    
    private List<Specialty> applicableSpecialties;
    
    private static final Logger fLogger = LoggerFactory.getLogger(EditRiskModel.class);

    
    // Store the Edit Changes (an 'Import') in a new RiskModel object.
	// If this were a normal editing situation where the user could change individual values through 
	// the gui, this would be separate copy, but since this is an import process, we will just 
	// init this to null until when/if the user imports a new model. 
	// The name may be edited separately and so the edit copy is stored separately.
	// the getter methods will first check will return the importModel values if it has been set and the target model 
	// if not.
    private RiskModel importedModel=null;
    
	protected EditRiskModel( final RiskModel rm, final List<Specialty> applSpecialty ) 
	{
		fRiskModel = rm;
		modelName = rm.getDisplayName();
		
		applicableSpecialties = ImmutableList.copyOf( applSpecialty );
		importedModel = null;				
	}
	
    /**
     * Returns an {@link EditRiskModel} instance for editing the given RiskModel.
     * @param riskModel the target RiskModel
     */
	public static EditRiskModel fromRiskModel( final RiskModel riskModel,
			final AdminService fAdminService )
	{
		List<Specialty> applSpecialties = new ArrayList<Specialty>();
		fLogger.debug("creating RM {}", riskModel.getDisplayName() );
		
		for( Specialty spec : fAdminService.getAllSpecialties() ) 
		{
			if( spec.getRiskModels().contains( riskModel ) ) 
			{
				fLogger.debug(" adding {} to specialties list", spec.getName() );
				applSpecialties.add( spec );
			}
		}
		
		return new EditRiskModel( riskModel, applSpecialties );
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
		return applicableSpecialties;
	}
	
	public ConstantTerm getConstantTerm() 
	{
		return ( importedModel == null ? fRiskModel.getConstantTerm() : importedModel.getConstantTerm() );
	}

	public int getMaxDisplayNameLength() 
	{
		return RiskModel.DISPLAY_NAME_MAX;
	}
	
//	public float getConstantTermCoefficient() 
//	{
//		return ( importedModel == null ? fRiskModel.getConstantTerm().getCoefficient() : importedModel.getConstantTerm().getCoefficient() );
//	}
//
//	public float getProcedureTermCoefficient() 
//	{
//		Set<ProcedureTerm> procTerms = ( importedModel != null ? 
//										 importedModel.getProcedureTerms() : fRiskModel.getProcedureTerms() );
//		//
//		// Note: The code is assuming that there is at most one ProcedureTerm.
//		//
//		return ( procTerms.isEmpty() ? 0.0f : procTerms.iterator().next().getCoefficient() );
//	}

//	public List<EditTerm> getNumericalTerms() 
//	{
//		Set<NumericalTerm> numTerms = ( importedModel != null ? 
//				 						importedModel.getNumericalTerms() : fRiskModel.getNumericalTerms() );
//
//		List<EditTerm> editTermsList = new ArrayList<EditTerm>();
//
//		for( NumericalTerm nTerm : numTerms )
//		{
//			editTermsList.add( new EditTerm( nTerm.getVariable().getDisplayName(), nTerm.getCoefficient() ) );
//		}
//		
//		return editTermsList;
//	}
	
	public List<ModelTermElement> getSortedTerms() 
	{
		Set<ModelTerm> numTerms = ( importedModel != null ? 
				 					importedModel.getTerms(): fRiskModel.getTerms() );

		ModelTermElementList termsElemsList = new ModelTermElementList();

		for( ModelTerm modTerm : numTerms )
		{
			// change this
			termsElemsList.addTermElements( modTerm );
		}
		
		termsElemsList.sort( );
		
		return termsElemsList;
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
