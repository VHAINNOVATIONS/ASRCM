package gov.va.med.srcalc.web.view;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
// Using StringUtils instead of commons-lang to ease the dependencies.
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

import gov.va.med.srcalc.domain.Procedure;
import gov.va.med.srcalc.domain.variable.*;
import gov.va.med.srcalc.domain.variable.DiscreteNumericalVariable.Category;

/**
 * <p>A {@link VariableVisitor} that produces a {@link Value} for each Variable
 * based on a {@link VariableEntry}.</p>
 * 
 * <p>Tightly coupled with enterVariables.jsp.</p>
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
        return fVariableEntry.getDynamicValues().get(variable.getKey());
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
            rejectDynamicValue(variable.getKey(), "noSelection", "no selection");
            return;
        }
        // Find the selected option.
        final Map<String, MultiSelectOption> optionMap = buildOptionMap(variable.getOptions());
        final MultiSelectOption selectedOption = optionMap.get(value);
        if (selectedOption == null)
        {
            rejectDynamicValue(variable.getKey(), "invalid", "not a valid selection");
        }
        else
        {
            fValues.add(variable.makeValue(selectedOption));
        }
    }
    
    @Override
    public void visitBoolean(final BooleanVariable variable)
    {
        fLogger.debug("Parsing BooleanVariable {}", variable);
        
        final String stringValue = getVariableValue(variable);
        final boolean booleanValue = Objects.equals(stringValue, "true");
        fValues.add(variable.makeValue(booleanValue));
    }
    
    @Override
    public void visitNumerical(final NumericalVariable variable)
    {
        fLogger.debug("Parsing NumericalVariable {}", variable);

        final String stringValue = getVariableValue(variable);
        if (StringUtils.isEmpty(stringValue))
        {
            rejectDynamicValue(variable.getKey(), "noInput.float", "no input");
            return;
        }
        try
        {
            final float floatValue = Float.parseFloat(stringValue);
            fValues.add(variable.makeValue(floatValue));
        }
        // Translate any Exceptions into validation errors.
        catch (final NumberFormatException ex)
        {
            rejectDynamicValue(variable.getKey(), "typeMismatch.float", ex.getMessage());
        }
        catch (final ValueTooLowException ex)
        {
            rejectDynamicValue(
                    variable.getKey(),
                    ex.getErrorCode(),
                    new Object[]{ variable.getMinValue() },
                    ex.getMessage());
        }
        catch (final ValueTooHighException ex)
        {
            rejectDynamicValue(
                    variable.getKey(),
                    ex.getErrorCode(),
                    new Object[]{ variable.getMaxValue() },
                    ex.getMessage());
        }
    }
    
    /**
     * Builds a Map to each Category from the option's value.
     */
    private Map<String, Category> buildCategoryMap(final Iterable<Category> categories)
    {
        final HashMap<String, Category> map = new HashMap<>();
        for (final Category category : categories)
        {
            map.put(category.getOption().getValue(), category);
        }
        return map;
    }
    
    @Override
    public void visitDiscreteNumerical(final DiscreteNumericalVariable variable)
    {
        fLogger.debug("Parsing DiscreteNumericalVariable {}", variable);

        final String categoryName = getVariableValue(variable);
        if (StringUtils.isEmpty(categoryName))
        {
            rejectDynamicValue(variable.getKey(), "noSelection", "no selection");
            return;
        }
        // Special case: numerical
        if (categoryName.equals(VariableEntry.SPECIAL_NUMERICAL))
        {
            final String numericalName = VariableEntry.getNumericalInputName(variable);
            final String stringValue = fVariableEntry.getDynamicValues().get(
                    numericalName);
            fLogger.debug("User specified a numerical value: {}", stringValue);
            if (StringUtils.isEmpty(stringValue))
            {
                rejectDynamicValue(numericalName, "noInput.float", "no input");
                return;
            }
            try
            {
                final float floatValue = Float.parseFloat(stringValue);
                fValues.add(variable.makeValue(floatValue));
            }
            // Translate any Exceptions into validation errors.
            catch (final NumberFormatException ex)
            {
                rejectDynamicValue(
                        numericalName, "typeMismatch.float", ex.getMessage());
            }
            catch (final ValueTooLowException ex)
            {
                rejectDynamicValue(
                        numericalName,
                        ex.getErrorCode(),
                        new Object[]{ variable.getMinValue() },
                        ex.getMessage());
            }
            catch (final ValueTooHighException ex)
            {
                rejectDynamicValue(
                        numericalName,
                        ex.getErrorCode(),
                        new Object[]{ variable.getMaxValue() },
                        ex.getMessage());
            }
        }
        else
        {
            final Map<String, Category> categoryMap =
                    buildCategoryMap(variable.getCategories());
            final Category selectedCategory = categoryMap.get(categoryName);
            if (selectedCategory == null)
            {
                rejectDynamicValue(variable.getKey(), "invalid", "not a valid selection");
            }
            else
            {
                fLogger.debug("User selected Category {}", selectedCategory);
                fValues.add(variable.makeValue(selectedCategory));
            }
        }
    }
    
    @Override
    public void visitProcedure(final ProcedureVariable variable)
    {
        fLogger.debug("Parsing ProcedureVariable {}", variable);

        final String selectedCpt = getVariableValue(variable);
        if (StringUtils.isEmpty(selectedCpt))
        {
            rejectDynamicValue(variable.getKey(), "noSelection", "no selection");
            return;
        }
        final Procedure selectedProcedure =
                variable.getProcedureMap().get(selectedCpt);
        if (selectedProcedure == null)
        {
            rejectDynamicValue(variable.getKey(), "invalid", "not a valid procedure");
        }
        else
        {
            fValues.add(variable.makeValue(selectedProcedure));
        }
    }
    
    public List<Value> getValues()
    {
        return fValues;
    }
}
