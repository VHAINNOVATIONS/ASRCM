package gov.va.med.srcalc.controller;

import java.util.*;

import javax.servlet.ServletRequest;

import org.springframework.validation.Errors;

import gov.va.med.srcalc.domain.variable.*;

/**
 * <p>A {@link VariableVisitor} that produces a {@link Value} for each Variable
 * based on an {@link ServletRequest}.</p>
 * 
 * <p>Tightly coupled with {@link InputGeneratorVisitor}.</p>
 */
public class InputParserVisitor implements VariableVisitor
{
    private final SubmittedValues fRequest;
    private final Errors fErrors;
    private final ArrayList<Value> fValues;
    
    public InputParserVisitor(SubmittedValues request, Errors errors)
    {
        fRequest = request;
        fValues = new ArrayList<>();
        fErrors = errors;
    }
    
    /**
     * Returns a Map of value to {@link MultiSelectOption} for all of the options.
     */
    public Map<String, MultiSelectOption> buildOptionMap(Iterable<MultiSelectOption> options)
    {
        final HashMap<String, MultiSelectOption> map = new HashMap<>();
        for (MultiSelectOption option : options)
        {
            map.put(option.getValue(), option);
        }
        return map;
    }
    
    @Override
    public void visitMultiSelect(MultiSelectVariable variable)
    {
        // FIXME: assumed to be gender
        // Find the selected option.
        final Map<String, MultiSelectOption> optionMap = buildOptionMap(variable.getOptions());
        MultiSelectOption selectedOption = optionMap.get(fRequest.gender);
        if (selectedOption == null)
        {
            fErrors.rejectValue(variable.getDisplayName(), "invalid", "not a valid selection");
        }
        fValues.add(new MultiSelectValue(variable, selectedOption));
    }
    
    @Override
    public void visitNumerical(NumericalVariable variable)
    {
        // FIXME: assumed to be age
        int value = fRequest.age;
        if (value < variable.getMinValue())
        {
            fErrors.rejectValue(
                    variable.getDisplayName(),
                    "tooLow",
                    new Object[]{ variable.getMinValue() },
                    "must be greater than %d");
        }
        else if (value > variable.getMaxValue())
        {
            fErrors.rejectValue(
                    variable.getDisplayName(),
                    "tooHigh",
                    new Object[]{ variable.getMaxValue() },
                    "must be less than %d");
        }
        else
        {
            fValues.add(new NumericalValue(variable, fRequest.age));
        }
    }
    
    public List<Value> getValues()
    {
        return fValues;
    }
}
