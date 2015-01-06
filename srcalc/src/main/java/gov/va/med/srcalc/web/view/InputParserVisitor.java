package gov.va.med.srcalc.web.view;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
// Using StringUtils instead of commons-lang to ease the dependencies.
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

import gov.va.med.srcalc.domain.Procedure;
import gov.va.med.srcalc.domain.variable.*;

/**
 * <p>A {@link VariableVisitor} that produces a {@link Value} for each Variable
 * based on a {@link VariableEntry}.</p>
 * 
 * <p>Tightly coupled with {@link InputGeneratorVisitor}.</p>
 */
public class InputParserVisitor extends ExceptionlessVariableVisitor
{
    private static final Logger fLogger = LoggerFactory.getLogger(InputParserVisitor.class);

    private final VariableEntry fVariableEntry;
    private final Errors fErrors;
    private final ArrayList<Value> fValues;
    
    public InputParserVisitor(final VariableEntry variableEntry, final Errors errors)
    {
        fLogger.debug(
                "Creating InputParserVisitor for the given request values: {}",
                variableEntry);

        fVariableEntry = variableEntry;
        fValues = new ArrayList<>();
        fErrors = errors;
    }

    /**
     * Returns the given variable's value from the {@link VariableEntry}, or
     * null if there is no value.
     */
    protected String getVariableValue(final Variable variable)
    {
        return fVariableEntry.getDynamicValues().get(variable.getDisplayName());
    }
    
    /**
     * Calls {@link Errors#rejectValue(String, String, String)} specifying the
     * proper nested field name.
     * @param variableName the key in the dynamicValues map
     * @param errorCode
     * @param defaultMessage
     */
    protected void rejectDynamicValue(
            final String variableName, final String errorCode, final String defaultMessage)
    {
        fErrors.rejectValue(
                VariableEntry.makeDynamicValuePath(variableName),
                errorCode,
                defaultMessage);
    }
    
    /**
     * Calls {@link Errors#rejectValue(String, String, Object[], String)} specifying the
     * proper nested field name.
     * @param variableName the key in the dynamicValues map
     * @param errorCode
     * @param errorArgs
     * @param defaultMessage
     */
    protected void rejectDynamicValue(
            final String variableName,
            final String errorCode,
            final Object[] errorArgs,
            final String defaultMessage)
    {
        fErrors.rejectValue(
                VariableEntry.makeDynamicValuePath(variableName),
                errorCode,
                errorArgs,
                defaultMessage);
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
        fLogger.debug("Parsing MultiSelectVariable {}", variable);

        final String value = getVariableValue(variable);
        if (StringUtils.isEmpty(value))
        {
            rejectDynamicValue(variable.getDisplayName(), "noSelection", "no selection");
            return;
        }
        // Find the selected option.
        final Map<String, MultiSelectOption> optionMap = buildOptionMap(variable.getOptions());
        final MultiSelectOption selectedOption = optionMap.get(value);
        if (selectedOption == null)
        {
            rejectDynamicValue(variable.getDisplayName(), "invalid", "not a valid selection");
        }
        else
        {
            fValues.add(new MultiSelectValue(variable, selectedOption));
        }
    }
    
    @Override
    public void visitBoolean(final BooleanVariable variable)
    {
        fLogger.debug("Parsing BooleanVariable {}", variable);
        
        final String stringValue = getVariableValue(variable);
        final boolean booleanValue = Objects.equals(stringValue, "true");
        fValues.add(new BooleanValue(variable, booleanValue));
    }
    
    @Override
    public void visitNumerical(final NumericalVariable variable)
    {
        fLogger.debug("Parsing NumericalVariable {}", variable);

        final String stringValue = getVariableValue(variable);
        if (StringUtils.isEmpty(stringValue))
        {
            rejectDynamicValue(variable.getDisplayName(), "noInput.float", "no input");
            return;
        }
        try
        {
            final float floatValue = Float.parseFloat(stringValue);
            fValues.add(new NumericalValue(variable, floatValue));
        }
        // Translate any Exceptions into validation errors.
        catch (final NumberFormatException ex)
        {
            rejectDynamicValue(variable.getDisplayName(), "typeMismatch.float", ex.getMessage());
        }
        catch (final ValueTooLowException ex)
        {
            rejectDynamicValue(
                    variable.getDisplayName(),
                    ex.getErrorCode(),
                    new Object[]{ variable.getMinValue() },
                    ex.getMessage());
        }
        catch (final ValueTooHighException ex)
        {
            rejectDynamicValue(
                    variable.getDisplayName(),
                    ex.getErrorCode(),
                    new Object[]{ variable.getMaxValue() },
                    ex.getMessage());
        }
    }
    
    @Override
    public void visitLab(final LabVariable variable)
    {
        // TODO
    }
    
    @Override
    public void visitProcedure(final ProcedureVariable variable)
    {
        fLogger.debug("Parsing ProcedureVariable {}", variable);

        final String selectedCpt = getVariableValue(variable);
        if (StringUtils.isEmpty(selectedCpt))
        {
            rejectDynamicValue(variable.getDisplayName(), "noSelection", "no selection");
            return;
        }
        final Procedure selectedProcedure =
                variable.getProcedureMap().get(selectedCpt);
        if (selectedProcedure == null)
        {
            rejectDynamicValue(variable.getDisplayName(), "invalid", "not a valid procedure");
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
