package gov.va.med.srcalc.web;

import gov.va.med.srcalc.domain.calculation.*;
import gov.va.med.srcalc.web.view.VariableEntry;

/**
 * A {@link ValueVisitor} that adds a key value pair to the dynamic values used
 * on the enter variables page to fill values for the input fields.
 * 
 */
public class DynamicValueVisitor extends ExceptionlessValueVisitor
{
    private VariableEntry fVariableEntry;
    
    public DynamicValueVisitor(final VariableEntry variableEntry)
    {
        fVariableEntry = variableEntry;
    }
    
    @Override
    public void visitNumerical(final NumericalValue value)
    {
        fVariableEntry.getDynamicValues().put(value.getVariable().getKey(), value.getValue().toString());
    }

    @Override
    public void visitBoolean(final BooleanValue value)
    {
        fVariableEntry.getDynamicValues().put(value.getVariable().getKey(), value.getValue().toString());
    }

    @Override
    public void visitMultiSelect(final MultiSelectValue value)
    {
        fVariableEntry.getDynamicValues().put(value.getVariable().getKey(), value.getValue().toString());
    }

    @Override
    public void visitProcedure(final ProcedureValue value)
    {
        fVariableEntry.getDynamicValues().put(value.getVariable().getKey(), value.getValue().getCptCode());
    }

    @Override
    public void visitDiscreteNumerical(final DiscreteNumericalValue value)
    {
        String key = value.getVariable().getKey();
        final String valueString;
        if(Float.isNaN(value.getNumericalValue()))
        {
            valueString = value.getValue().getOption().getValue();
        }
        else
        {
            key = VariableEntry.makeNumericalInputName(key);
            valueString = String.valueOf(value.getNumericalValue());
        }
        fVariableEntry.getDynamicValues().put(key, valueString);
    }
    
    public VariableEntry getValues()
    {
        return fVariableEntry;
    }
}
