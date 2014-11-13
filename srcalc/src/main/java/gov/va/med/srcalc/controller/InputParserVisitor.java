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
    
    public InputParserVisitor(final SubmittedValues request, final Errors errors)
    {
        fRequest = request;
        fValues = new ArrayList<>();
        fErrors = errors;
    }
    
    /**
     * Returns a Map of value to {@link MultiSelectOption} for all of the options.
     */
    public Map<String, MultiSelectOption> buildOptionMap(final Iterable<MultiSelectOption> options)
    {
        final HashMap<String, MultiSelectOption> map = new HashMap<>();
        for (final MultiSelectOption option : options)
        {
            map.put(option.getValue(), option);
        }
        return map;
    }
    
    @Override
    public void visitMultiSelect(final MultiSelectVariable variable)
    {
        // FIXME: assumed to be gender
        // Find the selected option.
        final Map<String, MultiSelectOption> optionMap = buildOptionMap(variable.getOptions());
        final MultiSelectOption selectedOption = optionMap.get(fRequest.getGender());
        if (selectedOption == null)
        {
            fErrors.rejectValue(variable.getDisplayName(), "invalid", "not a valid selection");
        }
        fValues.add(new MultiSelectValue(variable, selectedOption));
    }
    
    @Override
    public void visitNumerical(final NumericalVariable variable)
    {
        // FIXME: assumed to be age
        final int value = fRequest.getAge();
        if (value < variable.getMinValue())
        {
            fErrors.rejectValue(
                    variable.getDisplayName(),
                    "tooLow",
                    new Object[]{ variable.getMinValue() },
                    "must be greater than {0}");
        }
        else if (value > variable.getMaxValue())
        {
            fErrors.rejectValue(
                    variable.getDisplayName(),
                    "tooHigh",
                    new Object[]{ variable.getMaxValue() },
                    "must be less than {0}");
        }
        else
        {
            fValues.add(new NumericalValue(variable, value));
        }
    }
    
    public List<Value> getValues()
    {
        return fValues;
    }
}
