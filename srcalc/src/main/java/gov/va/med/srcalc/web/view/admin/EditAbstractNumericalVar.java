package gov.va.med.srcalc.web.view.admin;

import com.google.common.collect.*;

import gov.va.med.srcalc.domain.calculation.ValueRetriever;
import gov.va.med.srcalc.domain.model.*;
import gov.va.med.srcalc.service.ModelInspectionService;

/**
 * Provides common functionality for editing an {@link AbstractNumericalVariable}.
 */
public abstract class EditAbstractNumericalVar extends EditBaseVar
{   
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
    
    /**
     * Constructs an instance with the properties initialized to the given prototype
     * variable.
     * @param prototype the existing variable containing the initial properties.
     * The properties are copied but this object is not stored.
     * @param modelService to provide reference data (e.g., available
     * VariableGroups) to the user
     */
    public EditAbstractNumericalVar(
            final AbstractNumericalVariable prototype,
            final ModelInspectionService modelService)
    {
        super(prototype, modelService);
        fUnits = prototype.getUnits();
        fValidRange = NumericalRangeBuilder.fromPrototype(prototype.getValidRange());
    }
    
    @Override
    public ImmutableSortedSet<ValueRetriever> getAllRetrievers()
    {
        return ImmutableSortedSet.copyOf(ValueRetriever.NUMERICAL_SET);
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
    
    /**
     * <p>Applies the base numerical properties that we store here to an existing
     * variable.</p>
     * 
     * <ul>
     * <li>Units</li>
     * <li>Valid Range</li>
     * <li>All other properties specified in {@link #applyBaseProperties(AbstractVariable)}</li>
     * </ul>
     * 
     * @param var the existing variable to modify
     * @throws IllegalStateException if the key to set doesn't already match
     * the variable's key or if the set retriever is not valid
     */
    protected final void applyNumericalProperties(final AbstractNumericalVariable var)
    {
        applyBaseProperties(var);
        var.setUnits(getUnits());
        var.setValidRange(getValidRange().build());
    }
    
}
