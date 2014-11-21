package gov.va.med.srcalc.controller;

import java.util.*;

import javax.servlet.ServletRequest;

// Using StringUtils instead of commons-lang to ease the dependencies.
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

import gov.va.med.srcalc.domain.Procedure;
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
        final String value = fRequest.getGender();
        if (value == null)
        {
            fErrors.rejectValue(variable.getDisplayName(), "noSelection", "no selection");
            return;
        }
        // Find the selected option.
        final Map<String, MultiSelectOption> optionMap = buildOptionMap(variable.getOptions());
        final MultiSelectOption selectedOption = optionMap.get(value);
        if (selectedOption == null)
        {
            fErrors.rejectValue(variable.getDisplayName(), "invalid", "not a valid selection");
        }
        else
        {
            fValues.add(new MultiSelectValue(variable, selectedOption));
        }
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
    
    @Override
    public void visitProcedure(ProcedureVariable variable) throws Exception
    {
        final String selectedCpt = fRequest.getProcedure();
        if (StringUtils.isEmpty(selectedCpt))
        {
            fErrors.rejectValue(variable.getDisplayName(), "noSelection", "no selection");
            return;
        }
        final Procedure selectedProcedure =
                variable.getProcedureMap().get(selectedCpt);
        if (selectedProcedure == null)
        {
            fErrors.rejectValue(variable.getDisplayName(), "invalid", "not a valid procedure");
        }
        else
        {
            fValues.add(new ProcedureValue(variable, selectedProcedure));
        }
    }
    
    public List<Value> getValues()
    {
        return fValues;
    }
}
