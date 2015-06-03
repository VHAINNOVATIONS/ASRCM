package gov.va.med.srcalc.web.view.admin;

import java.util.*;

import com.google.common.collect.*;

import gov.va.med.srcalc.domain.calculation.ValueRetriever;
import gov.va.med.srcalc.domain.model.*;
import gov.va.med.srcalc.service.ModelInspectionService;

/**
 * Provides common functionality for editing an {@link AbstractNumericalVariable}.
 */
public abstract class EditAbstractNumericalVar extends EditBaseVar
{
    private static final ImmutableSortedSet<ValueRetriever> RETRIEVERS;
    static
    {
        // Kludge: all the retrievers happen to be numerical except for gender,
        // so use that fact to build the list of numerical retrievers.
        final HashSet<ValueRetriever> tempRetrievers =
                new HashSet<>(Arrays.asList(ValueRetriever.values()));
        tempRetrievers.remove(ValueRetriever.GENDER);
        RETRIEVERS = ImmutableSortedSet.copyOf(Ordering.usingToString(), tempRetrievers);
    }

    private String fUnits;
    private final NumericalRangeBuilder fValidRange;
    
    /**
     * Constructs an instance with default values for all properties.
     * @param modelService to provide reference data (e.g., available
     * VariableGroups) to the user
     */
    public EditAbstractNumericalVar(final ModelInspectionService modelService)
    {
        super(modelService);
        
        fUnits = "";
        fValidRange = new NumericalRangeBuilder();
    }
    
    @Override
    public ImmutableSortedSet<ValueRetriever> getAllRetrievers()
    {
        return RETRIEVERS;
    }
    
    /**
     * Returns the units string to set on the variable.
     * @see AbstractNumericalVariable#setUnits(String)
     */
    public String getUnits()
    {
        return fUnits;
    }

    /**
     * Sets the units string to set on the variable.
     * @see AbstractNumericalVariable#setUnits(String)
     */
    public void setUnits(final String units)
    {
        fUnits = units;
    }
    
    /**
     * Returns the maximum length of the units string, {@link
     * AbstractNumericalVariable#UNITS_MAX}.
     */
    public int getUnitsMax()
    {
        return AbstractNumericalVariable.UNITS_MAX;
    }

    /**
     * Returns the builder for valid range to set on the variable. Manipulate
     * this builder's properties to change the resultant range.
     * @see AbstractNumericalVariable#setValidRange(NumericalRange)
     */
    public NumericalRangeBuilder getValidRange()
    {
        return fValidRange;
    }
    
}
