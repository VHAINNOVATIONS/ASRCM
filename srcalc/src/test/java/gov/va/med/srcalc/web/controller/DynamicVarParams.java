package gov.va.med.srcalc.web.controller;

import gov.va.med.srcalc.web.view.VariableEntry;

import java.util.*;

import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

/**
 * For testing purposes. Represents a set of dynamic variable value parameters
 * to add to an HTTP request.
 */
public class DynamicVarParams
{
    private final HashMap<String, String> fValues = new HashMap<>();
    
    public void add(final String varName, final String value)
    {
        fValues.put(varName, value);
    }
    
    public int getNumVariables()
    {
        return fValues.size();
    }
    
    /**
     * Returns the variable names and values as a Map from name to value. This
     * is a live instance of the internal data: modifications will affect the
     * stored values.
     */
    public Map<String, String> getPairs()
    {
        return fValues;
    }

    public MockHttpServletRequestBuilder addTo(
            final MockHttpServletRequestBuilder request)
    {
        for (final Map.Entry<String, String> pair : fValues.entrySet())
        {
            request.param(
                    VariableEntry.makeDynamicValuePath(pair.getKey()),
                    pair.getValue());
        }
        return request;
    }
}
