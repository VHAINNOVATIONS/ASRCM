package gov.va.med.srcalc.web.view;

import gov.va.med.srcalc.domain.Patient;
import gov.va.med.srcalc.domain.model.*;
import gov.va.med.srcalc.vista.RpcVistaPatientDao;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final String SEPARATOR = "$";
    
    /**
     * A way to delineate the information regarding a retrieved value.
     */
    private static final String RETRIEVAL_STRING = "retrievalDate";
    
    private static final Logger fLogger = LoggerFactory.getLogger(VariableEntry.class);

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
            if (v.getRetriever() == null)
            {
                continue;
            }
            String key = v.getKey();
            if (v instanceof DiscreteNumericalVariable)
            {
                key += "$numerical";
            }
            
            fLogger.debug("Executing retriever {} for key {}", v.getRetriever(), key);
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
     * Returns the name of the numerical input for the given key
     */
    public static String makeNumericalInputName(final String key)
    {
        return key + SEPARATOR + SPECIAL_NUMERICAL;
    }
    
    /**
     * Returns the name of the key concatenated with the retrieval tag.
     */
    public static String makeRetrievalString(final String key)
    {
        return key + SEPARATOR + RETRIEVAL_STRING;
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
     * Returns a string indicating the retrieved date, value, and units
     * of an {@link AbstractVariable}
     * @param key the name of the field to automatically fill
     */
    public String getMeasureDate(final String key)
    {
        final String retrievedString = fDynamicValues.get(key + VariableEntry.SEPARATOR + VariableEntry.RETRIEVAL_STRING);
        if(retrievedString == null)
        {
            return "";
        }
        return retrievedString;
    }
    
    /**
     * This method will place a key-value pair into the dynamic values by using a fully-qualified 
     * variable key and a string notifying the user of information about the retrieved value.
     * @param key the fully qualified variable key to add to the dynamic values
     * @param retrievalString the string to display to the user
     */
    public void setMeasureDate(final String key, final String retrievalString)
    {
        fDynamicValues.put(key + VariableEntry.SEPARATOR + VariableEntry.RETRIEVAL_STRING, retrievalString);
    }
    
    /**
     * Returns a string indicating the retrieved date, value, and units
     * of a {@link DiscreteNumericalVariable}.
     * @param key the name of the {@link DiscreteNumericalVariable} to automatically fill
     */
    public String getNumericalMeasureDate(final String key)
    {
        return getMeasureDate(key + VariableEntry.SEPARATOR + VariableEntry.SPECIAL_NUMERICAL);
    }
    
    /**
     * Make a string to tell the user information about the automatically retrieved value.
     * @param value the retrieved value to display
     * @param measureDate the date on which the value was measured
     * @param units the units in which the value was measured, can be empty but not null
     * @return the string to display to the user
     */
    public static String makeRetrievalString(final double value, final Date measureDate, final String units)
    {
        final SimpleDateFormat originalFormat = new SimpleDateFormat(RpcVistaPatientDao.VISTA_DATE_OUTPUT_FORMAT);
        final String dateString = " on " + originalFormat.format(measureDate);
        String unitString = "";
        if(units.length() > 0)
        {
            unitString = " " + units;
        }
        return String.format("(Retrieved: %.2f%s%s)", value, unitString, dateString);
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
