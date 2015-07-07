package gov.va.med.srcalc.web.view.admin;

import gov.va.med.srcalc.domain.model.BooleanTerm;
import gov.va.med.srcalc.domain.model.ConstantTerm;
import gov.va.med.srcalc.domain.model.DerivedTerm;
import gov.va.med.srcalc.domain.model.DiscreteNumericalVariable;
import gov.va.med.srcalc.domain.model.DiscreteTerm;
import gov.va.med.srcalc.domain.model.DiscreteVariable;
import gov.va.med.srcalc.domain.model.ModelTerm;
import gov.va.med.srcalc.domain.model.MultiSelectOption;
import gov.va.med.srcalc.domain.model.MultiSelectVariable;
import gov.va.med.srcalc.domain.model.NumericalTerm;
import gov.va.med.srcalc.domain.model.ProcedureTerm;
import gov.va.med.srcalc.domain.model.RiskModel;
import gov.va.med.srcalc.domain.model.Specialty;
import gov.va.med.srcalc.service.AdminService;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;

import gov.va.med.srcalc.util.DisplayNameConditions;

/**
 * A form backing object for editing a target (link @RiskModel) object.
 */
public class EditRiskModel implements Comparable<EditRiskModel>
{
    private final RiskModel fRiskModel;
    
    private String fModelName;
    
    private List<Specialty> fApplicableSpecialties;
    
    // Store the Edit Changes (an 'Import') in a new RiskModel object.
    // If this were a normal editing situation where the user could change individual values through
    // the gui, this would be separate copy, but since this is an import process, we will just
    // init this to null until when/if the user imports a new model.
    // The name may be edited separately and so the edit copy is stored separately.
    // the getter methods will first check will return the fImportModel values if it has been set and
    // the target model
    // if not.
    private RiskModel fImportedModel = null;

    private static final Logger fLogger = LoggerFactory.getLogger(EditRiskModel.class);

    /**
     * Class to hold the label, type and coefficient for a (link @ModelTerm) 
     * and its coefficient on the Admin's Edit Model page. 
     * 
     * @author owner
     *
     */
    public static class ModelTermSummary  implements Comparable<ModelTermSummary>
    {
        private final ModelTerm targetTerm;
        
        private String displayName;   // the name presented on the Edit Model page
        private String termType;      // the variable type
        
        private ModelTermSummary( ModelTerm mt, String name, String type ) 
        {
            displayName = name;
            targetTerm = mt;
            termType = type;
        }
         
        /**
         * The name of the ModelTerm to be shown to the user.
         * @return
         */
        public String getDisplayName()  
        {
            return displayName;
        }

        /**
         *  The term's type is the type of the term's Variable or, for term's w/o a Variable, it is the 
         *  type of the term such as "Constant" or "Rule".
         */
        public String getTermType() 
        {
            return termType;
        }
        
        /**
         * The target term's coefficient
         * @return
         */
        public float getCoefficient()
        {
            return targetTerm.getCoefficient();
        }

        /*
         * Create and instance of a ModelTermSummary for the given ModelTerm. Determine and set the
         * displayName and the termType.
         */
        public static ModelTermSummary createTermSummary( ModelTerm modelTerm ) 
        {
            String dispName="";
            String type="";
            
            if( modelTerm instanceof ConstantTerm ) 
            {
                dispName = "Constant"; 
                type = "Constant";
            }
            else if( modelTerm instanceof ProcedureTerm )
            {
                type = "Multiplier";
                dispName = "Procedure (RVU Multiplier)"; 
            }
            else if( modelTerm instanceof BooleanTerm )
            {
                type = "Boolean";
                BooleanTerm boolTerm = (BooleanTerm)modelTerm;                
                dispName = boolTerm.getVariable().getDisplayName();
            }            
            else if( modelTerm instanceof NumericalTerm )
            {
                type = "Numerical";
                NumericalTerm numTerm = (NumericalTerm)modelTerm;
                dispName = numTerm.getVariable().getDisplayName();                                     
            }
            else if( modelTerm instanceof DiscreteTerm )
            {
                DiscreteTerm discreteTerm = (DiscreteTerm)modelTerm;                                
                MultiSelectOption opt = discreteTerm.getOption();
                DiscreteVariable var = discreteTerm.getVariable();
                
                if( var instanceof DiscreteNumericalVariable ) 
                {
                    type = "Discrete Numeric";
                    dispName = var.getDisplayName()+ " = " + opt.getValue();
                }
                else if( var instanceof MultiSelectVariable ) 
                {
                    type = "Multi-Select";
                    dispName = var.getDisplayName()+ " = " + opt.getValue() ;
                }
            }            
            else if( modelTerm instanceof DerivedTerm )
            {
                type = "Rule";
                dispName = "Rule: "+((DerivedTerm)modelTerm).getRule().getId();
            }
            else
            {
                dispName = "Unrecognized Term: "+modelTerm.getClass().getName();    
                type = "";
            }
            
            return new ModelTermSummary( modelTerm, dispName, type );
        }

        /**
         * a string used to make sorting the terms easier. 
         * ConstantTerms first, then Rules and then by display name 
         * with discrete options ordered by index 
         * 
         * @return
         */
        private String getSortString() 
        {
            if( targetTerm instanceof ConstantTerm ) 
            {
                return "AAAA";
            }
            else if( targetTerm instanceof DerivedTerm )
            {
                return "AAAAA"+Float.toString( ((DerivedTerm)targetTerm).getRule().getId()*1f/1000f ); // 7 before 11
            }
            // order DiscreteTerms according to their options
            //
            else if( targetTerm instanceof DiscreteTerm )
            {
                DiscreteTerm dTerm = (DiscreteTerm)targetTerm;
                return dTerm.getVariable().getDisplayName().toUpperCase() + dTerm.getOptionIndex();
            }
            else
            {
                return displayName.toUpperCase();
            }
        }
        
        // sort by the sortString
        @Override
        public int compareTo( ModelTermSummary o ) 
        {
            return getSortString().compareTo( o.getSortString() );
        }        
    }    
        
    protected EditRiskModel(final RiskModel rm, final List<Specialty> applSpecialty)
    {
        fRiskModel = rm;
        fModelName = rm.getDisplayName();
        
        fApplicableSpecialties = ImmutableList.copyOf(applSpecialty);
        fImportedModel = null;
    }

    /**
     * Returns an {@link EditRiskModel} instance for editing the given RiskModel.
     * @param riskModel the target RiskModel
     */
    public static EditRiskModel fromRiskModel( final RiskModel riskModel,
            final AdminService fAdminService )
    {
        List<Specialty> applSpecialties = new ArrayList<Specialty>();
        fLogger.debug("creating RiskModel {}", riskModel.toString() );
        
        for( Specialty spec : fAdminService.getAllSpecialties() ) 
        {
            if( spec.getRiskModels().contains( riskModel ) ) 
            {
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
        public void setImportedModel( RiskModel impModel ) 
        {
            fImportedModel = impModel;
            fImportedModel.setDisplayName( fModelName );
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
     * Return the Id of the target RiskModel.
     */
    public String getId()
    {
        return Integer.toString(fRiskModel.getId());
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
     * Return a list of {@link ModelTermSummary} objects for the terms in the 
     * target RiskModel.
     *  
     * @return
     */
    public List<ModelTermSummary> getTermSummaries() 
    {
        Set<ModelTerm> numTerms = ( fImportedModel != null ? 
                                    fImportedModel.getTerms(): fRiskModel.getTerms() );

        List<ModelTermSummary> termSummariesList = new ArrayList<ModelTermSummary>();

        for( ModelTerm modTerm : numTerms )
        {
            termSummariesList.add( ModelTermSummary.createTermSummary( modTerm ) );
        }
        
        Collections.sort( termSummariesList );
        
        return termSummariesList;
    }

    /**
     * Update the target RiskModel with the current edits.
     */
    public RiskModel applyChanges() 
    {
        // can't change the model ID

        // if the imported model has an associated name then
        // should we override the current one if it had been edited?
        
        fRiskModel.setDisplayName( fModelName );
        
        //  save the imported model to the fRiskModel 
        
        return fRiskModel;
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
        return String.format( "EditRiskModel: name=%s,ID=", fModelName, getId() );
    }
}
