package gov.va.med.srcalc.web.view.admin;

import gov.va.med.srcalc.domain.model.BooleanTerm;
import gov.va.med.srcalc.domain.model.ConstantTerm;
import gov.va.med.srcalc.domain.model.DerivedTerm;
import gov.va.med.srcalc.domain.model.DiscreteTerm;
import gov.va.med.srcalc.domain.model.ModelTerm;
import gov.va.med.srcalc.domain.model.NumericalTerm;
import gov.va.med.srcalc.domain.model.ProcedureTerm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A class to store the list of Model Term entry on the Admin's Edit Model Page.
 * 
 * @author owner
 *
 */
public class ModelTermElementList extends ArrayList<ModelTermElement> {
	
	/**
	 *  what's this? 
	 */
//	private static final long serialVersionUID = 1L;

	public ModelTermElementList( )
	{

	}
	
	public ModelTermElementList( List<ModelTerm> modelTerms )
	{
		for( ModelTerm mt : modelTerms ) 
		{
			addTermElements( mt );
		}
	}

	public void addTermElements( ModelTerm modelTerm ) 
	{
//		origModelTerm = modelTerm; 		
		if( modelTerm instanceof ConstantTerm ) 
		{
			this.add( 
					new ModelTermElement( "Constant", modelTerm.getCoefficient(), 10 ) ); 
		}
		else if( modelTerm instanceof ProcedureTerm )
		{
			this.add( 
					new ModelTermElement( "Procedure (RVU Multiplier)", modelTerm.getCoefficient(), 20 ) ); 
		}
		if( modelTerm instanceof NumericalTerm )
		{
			NumericalTerm numTerm = (NumericalTerm)modelTerm;
			String name;
			int    sort;
			String varKey = numTerm.getVariable().getKey();
			
			// Special case Age to be towards the top
			// TODO : is there a constant for this anywhere?
			if( varKey.compareToIgnoreCase("age" ) == 0 ) 
			{
				name = numTerm.getVariable().getDisplayName();
				sort = 25; // before others, after Procedure
			}
			else
			{
				name = "Numerical: "+ numTerm.getVariable().getDisplayName();
				sort = 30;
			}

			this.add( new ModelTermElement( name, modelTerm.getCoefficient(), 50 ) );     			
		}
		else if( modelTerm instanceof DiscreteTerm )
		{
			DiscreteTerm discreteTerm = (DiscreteTerm)modelTerm;				
			String name = "Multi-Select: " + discreteTerm.getVariable().getDisplayName();
//			discreteTerm.getVariable().g
			this.add( 
					new ModelTermElement( name, modelTerm.getCoefficient(), 40 ) );
		}			
		else if( modelTerm instanceof BooleanTerm )
		{
			BooleanTerm boolTerm = (BooleanTerm)modelTerm;
			
			String name = "Boolean: " + boolTerm.getVariable().getDisplayName();
			this.add( 
					new ModelTermElement( name, modelTerm.getCoefficient(), 50 ) );     			
		}			
		else if( modelTerm instanceof DerivedTerm )
		{
			String name = "Rule: "+((DerivedTerm)modelTerm).getRule().getId();
			this.add( 
					new ModelTermElement( name, modelTerm.getCoefficient(), 80 ) );     			
		}
		else
		{
			this.add( 
					new ModelTermElement( "Unrecognized Term", modelTerm.getCoefficient(), 99 ) );	
		}
	}
	
	public void sort() 
	{
		Collections.sort( this );
	}
}
