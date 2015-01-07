package gov.va.med.srcalc.web.view;

import gov.va.med.srcalc.domain.variable.Variable;

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
    private final HashMap<String, String> fDynamicValues = new HashMap<>();
    
    /**
     * <p>Stores the values entered by the user for dynamic variables (which
     * happens to be all of them). The Map is from {@link Variable#getDisplayName()}
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
        return makeDynamicValuePath(var.getDisplayName());
    }
    
    @Override
    public String toString()
    {
        return "dynamicValues=" + fDynamicValues;
    }
}
