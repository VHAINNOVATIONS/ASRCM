package gov.va.med.srcalc.web.view.admin;

public class ModelTermElement  implements Comparable<ModelTermElement>
{
//	private final ModelTerm origModelTerm;
	
	private String displayName;
	private float coefficient;
	private int sortPriority=999;

	protected ModelTermElement( String name, float coeff, int sort ) 
	{
		displayName = name;
		coefficient = coeff;
		sortPriority = sort;
	}
 	
	public String getDisplayName()  
	{
		return displayName;
	}
	
	public float getCoefficient()
	{
		return coefficient;
	}

	@Override
	public int compareTo(ModelTermElement o) {

		// create ordering for the Admin Edit Model page. 
		// The Constant Term, Procdure Term...
		if( sortPriority == o.sortPriority ) 
		{
			return displayName.compareTo( o.getDisplayName() );
		}
		else {
			return sortPriority - o.sortPriority;
		}
	}    	
}