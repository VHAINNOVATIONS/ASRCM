package gov.va.med.srcalc.web.view;

import gov.va.med.srcalc.domain.Patient;
import gov.va.med.srcalc.domain.model.*;
import gov.va.med.srcalc.vista.RpcVistaPatientDao;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

/**
 * <p>The "form backing object" for the Variable Entry Form.</p>
 * 
 * <p>Since all calculation variables are dynamically specified, we cannot
 * declare real properties on this object. All variables are instead stored in
 * a Map from variable name to variable value. One disadvantage of this design
 * is that we lose Spring's automatic type conversion, which must be done in
 * application code instead.</p>
 */
public class VariableEntry
{
    /**
     * A special discrete category value indicating that the user has specified
     * a numerical input.
     */
    public static final String SPECIAL_NUMERICAL = "numerical";
    
    /**
     * A string used to separate a variable's key from the special category
     * that may be used to name it's field.
     * (i.e. The 'bun' variable and 'bun$numerical')
     */
    public static final String SEPARATOR = "$";

    private final HashMap<String, String> fDynamicValues = new HashMap<>();
    
    /**
     * Constructs a {@link VariableEntry} with reasonable defaults for some of
     * the variables.
     */
    public VariableEntry(final Collection<? extends Variable> variables)
    {
        final DefaultValueGenerator dvg = new DefaultValueGenerator();
        for (final Variable v : variables)
        {
            dvg.visit(v);
        }
    }
    
    /**
     * Constructs a {@link VariableEntry} with reasonable defaults for some of
     * the variables and automatically retrieved values for other variables.
     */
    public static VariableEntry withRetrievedValues(final Collection<? extends Variable> variables,
    		final Patient patient)
    {
    	final VariableEntry variableEntry = new VariableEntry(variables);
        for (final Variable v : variables)
        {
                // Skip this if the variable doesn't have an associated retriever.
        	if(v.getRetriever() == null)
        	{
        		continue;
        	}
        	String key = v.getKey();
        	if(v instanceof DiscreteNumericalVariable)
        	{
        		key += "$numerical";
        	}
        	v.getRetriever().execute(patient, variableEntry, v, key);
        }
        return variableEntry;
    }
    
    /**
     * <p>Stores the values entered by the user for dynamic variables (which
     * happens to be all of them). The Map is from {@link Variable#getKey()}
     * to the entered value.</p>
     * 
     * @see #makeDynamicValuePath(String)
     */
    public HashMap<String, String> getDynamicValues()
    {
        return fDynamicValues;
    }
    
    /**
     * <p>Returns the Spring Data Binding nested property name for the given
     * variable name, e.g. <code>"dynamicValues[Gender]"</code>. This is
     * referenced
     * <a href="http://docs.spring.io/spring/docs/4.0.x/spring-framework-reference/html/validation.html#beans-beans-conventions">in
     * the Spring docs</a> but not well-documented.</p>
     * 
     * <p>Note also that name should be alphanumeric to avoid interfering with
     * Spring databinding syntax.</p>
     * 
     * @return the nested property name, e.g. <code>"dynamicValues[Gender]"</code>
     */
    public static String makeDynamicValuePath(final String variableName)
    {
        return "dynamicValues[" + variableName + "]";
    }

    /**
     * Returns the name of the numerical input for the given
     * {@link DiscreteNumericalVariable}.
     * @param variable
     */
    public static String getNumericalInputName(final DiscreteNumericalVariable variable)
    {
        return variable.getKey() + SEPARATOR + SPECIAL_NUMERICAL;
    }
    
    /**
     * <p>Returns the Spring Data Binding nested property name for the given
     * variable. See {@link #makeDynamicValuePath(String)}.</p>
     * 
     * <p>Currently uses the variable's display name. The display names should
     * therefore be alphanumeric to avoid interfering with Spring databinding
     * syntax.</p>
     * 
     * @return the nested property name, e.g. <code>"dynamicValues[Gender]"</code>
     */
    public static String makeVariableValuePath(final Variable var)
    {
        return makeDynamicValuePath(var.getKey());
    }
    
    /**
     * Produce a message that will tell the user when the retrieved value was measured.
     */
    public static String makeRetrievalMessage(final Date retrievalDate)
    {
    	final SimpleDateFormat originalFormat = new SimpleDateFormat(RpcVistaPatientDao.VISTA_DATE_OUTPUT_FORMAT);
    	return "(Measured on: " + originalFormat.format(retrievalDate) + ")";
    }
    
    @Override
    public String toString()
    {
        return "dynamicValues=" + fDynamicValues;
    }
    
    final class DefaultValueGenerator extends ExceptionlessVariableVisitor
    {
        
        @Override
        public void visitNumerical(NumericalVariable variable)
        {
            // No default.
        }
        
        @Override
        public void visitBoolean(BooleanVariable variable)
        {
            // No default (false).
        }
        
        @Override
        public void visitMultiSelect(MultiSelectVariable variable)
        {
            // No default.
        }
        
        @Override
        public void visitProcedure(ProcedureVariable variable)
        {
            // No default.
        }
        
        @Override
        public void visitDiscreteNumerical(DiscreteNumericalVariable variable)
        {
            // Default to numerical input but don't specify the numerical value.
            fDynamicValues.put(variable.getKey(), SPECIAL_NUMERICAL);
        }
        
    }

}
