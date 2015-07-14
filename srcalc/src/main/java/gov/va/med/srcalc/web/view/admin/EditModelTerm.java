package gov.va.med.srcalc.web.view.admin;

import gov.va.med.srcalc.domain.model.*;
import gov.va.med.srcalc.service.InvalidIdentifierException;
import gov.va.med.srcalc.service.ModelInspectionService;
import gov.va.med.srcalc.util.ValidationCodes;
import gov.va.med.srcalc.util.ValidationUtils2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;

/**
 * A form-backing object for creating a {@link ModelTerm}.
 */
public class EditModelTerm
{
    private static final Logger fLogger = LoggerFactory.getLogger(EditModelTerm.class);
    
    /**
     * The overarching types of ModelTerms.
     */
    public enum TermType
    {
        /**
         * Represents a {@link ConstantTerm}.
         */
        CONSTANT,
        
        /**
         * Represents a {@link SingleVariableTerm}, depending on the Variable type.
         */
        VARIABLE,

        /**
         * Represents a {@link DerivedTerm}.
         */
        RULE;
    }
    
    private final ImmutableList<TypeState> fTypeStates = ImmutableList.of(
            new ConstantTypeState(), new VariableTypeState(), new RuleTypeState());
    
    private TypeState fTypeState;
    private String fKey;
    private String fOptionValue;
    private float fCoefficient;
    
    /**
     * Constructs an instance with unspecified default values.
     */
    public EditModelTerm()
    {
        // Just pick an arbitrary TypeState.
        fTypeState = fTypeStates.get(0);
        fKey = "";
        fOptionValue = "";
        fCoefficient = 0.0f;
    }
    
    /**
     * Constructs an instance for a Constant-type term.
     * @param coefficient the term coefficient
     * @return a new instance
     */
    public static EditModelTerm forConstant(final float coefficient)
    {
        return new EditModelTerm()
            .setTermType(TermType.CONSTANT)
            .setCoefficient(coefficient);
    }
    
    /**
     * Constructs an instance for a Variable-type term. Initializes the option value to
     * an empty string.
     * @param variableKey the target variable's key
     * @param coefficient the term coefficient
     * @return a new instance
     */
    public static EditModelTerm forVariable(
            final String variableKey, final float coefficient)
    {
        return new EditModelTerm()
            .setTermType(TermType.VARIABLE)
            .setKey(variableKey)
            .setOptionValue("")
            .setCoefficient(coefficient);
    }
    
    /**
     * Constructs an instance for a Rule-type term.
     * @param ruleName the target rule's display name
     * @param coefficient the term coefficient
     * @return a new instance
     */
    public static EditModelTerm forRule(
            final String ruleName, final float coefficient)
    {
        return new EditModelTerm()
            .setTermType(TermType.RULE)
            .setKey(ruleName)
            .setCoefficient(coefficient);
    }
    
    /**
     * Constructs an instance form a prototype ModelTerm. The properties are copied out
     * of the provided term, but a reference to it is not retained. Calling {@link
     * #build(ModelInspectionService)} should construct an identical term to the
     * prototype.
     * @param existingTerm the prototype term
     * @return a new instance
     */
    public static EditModelTerm fromExistingTerm(final ModelTerm existingTerm)
    {
        class ConstructionVisitor implements ModelTermVisitor
        {
            private EditModelTerm fConstructedEditTerm;
            
            private void visitVariableTerm(final SingleVariableTerm term)
            {
                fConstructedEditTerm = forVariable(
                        term.getVariable().getKey(), term.getCoefficient());
            }
            
            @Override
            public void visitProcedureTerm(final ProcedureTerm term)
            {
                visitVariableTerm(term);
            }
            
            @Override
            public void visitNumericalTerm(final NumericalTerm term)
            {
                visitVariableTerm(term);
            }
            
            @Override
            public void visitDiscreteTerm(final DiscreteTerm term)
            {
                visitVariableTerm(term);
                fConstructedEditTerm.setOptionValue(term.getOption().getValue());
            }
            
            @Override
            public void visitBooleanTerm(final BooleanTerm term)
            {
                visitVariableTerm(term);
            }
            
            @Override
            public void visitDerivedTerm(final DerivedTerm term)
            {
                fConstructedEditTerm = forRule(
                        term.getRule().getDisplayName(), term.getCoefficient());
            }
            
            @Override
            public void visitConstantTerm(final ConstantTerm term)
            {
                fConstructedEditTerm = forConstant(existingTerm.getCoefficient());
            }
            
            public EditModelTerm makeEditTerm(final ModelTerm term)
            {
                term.accept(this);
                return fConstructedEditTerm;
            }
        };
        
        return new ConstructionVisitor().makeEditTerm(existingTerm);
    }
    
    private TypeState getTypeState(final TermType termType)
    {
        // Since there are only 3 TypeStates, just do a brute-force search. A HashMap
        // would be less efficient.
        for (final TypeState state : fTypeStates)
        {
            if (state.getAssociatedType() == termType)
            {
                return state;
            }
        }
        
        throw new RuntimeException("Programming error: unsupported term type.");
    }

    /**
     * Returns the overarching type of {@link ModelTerm} that this object will create.
     */
    public TermType getTermType()
    {
        return fTypeState.getAssociatedType();
    }

    /**
     * Sets the overarching type of {@link ModelTerm} that this object will create.
     * @return this object for convenience
     */
    public EditModelTerm setTermType(final TermType termType)
    {
        fTypeState = getTypeState(termType);
        return this;
    }

    /**
     * Returns the associated variable or rule key. For constant terms, the returned value
     * is meaningless and unspecified.
     * @return not null
     */
    public String getKey()
    {
        return fKey;
    }

    /**
     * Sets the associated variable or rule key. If the term type is {@link
     * TermType#CONSTANT}, this value is ignored.
     * @return this object for convenience
     * @see #getKey()
     */
    public EditModelTerm setKey(final String key)
    {
        fKey = key;
        return this;
    }

    /**
     * If the term type is {@link TermType#VARIABLE} and the identified Variable is a
     * {@link DiscreteVariable}, then this method returns the term's associated option
     * value. Otherwise, the return value is meaningless and unspecified.
     * @return not null
     */
    public String getOptionValue()
    {
        return fOptionValue;
    }

    /**
     * Sets the option value for a variable. Unless the term type is {@link
     * TermType#VARIABLE}, this value is ignored.
     * @return this object for convenience
     * @see #getOptionValue()
     */
    public EditModelTerm setOptionValue(final String optionValue)
    {
        fOptionValue = optionValue;
        return this;
    }

    /**
     * Returns the coefficient for the term to construct.
     */
    public float getCoefficient()
    {
        return fCoefficient;
    }

    /**
     * Sets the coefficient for the term.
     * @return this object for convenience
     */
    public EditModelTerm setCoefficient(final float coefficient)
    {
        fCoefficient = coefficient;
        return this;
    }
    
    /**
     * Returns a Validator suitable for validating the current term type.
     * @param modelService a ModelInspectionService for retrieving Variables and Rules
     * as necessary for validation.
     */
    public Validator getValidator(final ModelInspectionService modelService)
    {
        return fTypeState.getValidator(modelService);
    }
    
    /**
     * Creates a ModelTermSummary for this term. The returned summary actually contains
     * more information than this class (EditModelTerm), such as a Variable display name.
     * @param modelService for looking up Variable and Rule information
     */
    public ModelTermSummary makeSummary(final ModelInspectionService modelService)
    {
        return fTypeState.makeSummary(modelService);
    }
    
    /**
     * Builds a new ModelTerm of the proper type.
     * @param modelService for looking up variables and rules
     * @return a new ModelTerm instance
     * @throws InvalidIdentifierException if there is no variable with the set key
     */
    public ModelTerm build(final ModelInspectionService modelService)
        throws InvalidIdentifierException
    {
        return fTypeState.buildTerm(modelService);
    }
    
    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("typeState", fTypeState)
                .add("key", fKey)
                .add("optionValue", fOptionValue)
                .add("coefficient", fCoefficient)
                .toString();
    }
    
    /**
     * Gang-of-Four State Pattern. This interface defines the State type.
     */
    private interface TypeState
    {
        /**
         * Returns the {@link TermType} which this object supports.
         */
        public TermType getAssociatedType();
        
        public BaseValidator getValidator(final ModelInspectionService modelService);
        
        public ModelTermSummary makeSummary(final ModelInspectionService modelService);

        /**
         * Builds a new ModelTerm of the correct type.
         * @param modelService for looking up variables
         * @throws InvalidIdentifierException if there is no variable with the set key
         */
        public ModelTerm buildTerm(final ModelInspectionService modelService)
            throws InvalidIdentifierException;
    }
    
    private class ConstantTypeState implements TypeState
    {
        @Override
        public TermType getAssociatedType()
        {
            return TermType.CONSTANT;
        }
        
        @Override
        public ModelTermSummary makeSummary(final ModelInspectionService modelService)
        {
            return new ModelTermSummary("Constant", "", fCoefficient);
        }
        
        @Override
        public BaseValidator getValidator(final ModelInspectionService modelService)
        {
            return new ConstantTermValidator();
        }

        @Override
        public ConstantTerm buildTerm(final ModelInspectionService modelService)
        {
            return new ConstantTerm(fCoefficient);
        }
    }
    
    private class VariableTypeState implements TypeState
    {
        private class TermBuilder extends ExceptionlessVariableVisitor
        {
            private SingleVariableTerm fBuiltTerm;
            
            public SingleVariableTerm getBuiltTerm()
            {
                return fBuiltTerm;
            }

            @Override
            public void visitProcedure(final ProcedureVariable variable)
            {
                fBuiltTerm = new ProcedureTerm(variable, fCoefficient);
            }
            
            @Override
            public void visitNumerical(final NumericalVariable variable)
            {
                fBuiltTerm = new NumericalTerm(variable, fCoefficient);
            }
            
            private DiscreteTerm buildDiscreteTerm(final DiscreteVariable variable)
            {
                final int index = variable.getOptions().indexOf(
                        // Just construct an equivalent option to find the index.
                        new MultiSelectOption(fOptionValue));

                if (index != -1)
                {
                    return new DiscreteTerm(variable, index, fCoefficient);
                }
                else
                {
                    throw new IllegalStateException(
                            "no such option value in variable " + variable);
                }
            }
            
            @Override
            public void visitMultiSelect(final MultiSelectVariable variable)
            {
                fBuiltTerm = buildDiscreteTerm(variable);
            }
            
            @Override
            public void visitDiscreteNumerical(final DiscreteNumericalVariable variable)
            {
                fBuiltTerm = buildDiscreteTerm(variable);
            }
            
            @Override
            public void visitBoolean(final BooleanVariable variable)
            {
                fBuiltTerm = new BooleanTerm(variable, fCoefficient);
            }
        };
        
        private class TermTypeTranslator extends ExceptionlessVariableVisitor
        {
            private String fTypeString;
            
            @Override
            public void visitNumerical(final NumericalVariable variable)
            {
                fTypeString = "Numerical";
            }

            @Override
            public void visitBoolean(final BooleanVariable variable)
            {
                fTypeString = "Boolean";
            }

            @Override
            public void visitMultiSelect(final MultiSelectVariable variable)
            {
                fTypeString = "Multi-Select";
            }

            @Override
            public void visitProcedure(final ProcedureVariable variable)
            {
                fTypeString = "Procedure (RVU Multiplier)";
            }

            @Override
            public void visitDiscreteNumerical(final DiscreteNumericalVariable variable)
            {
                fTypeString = "Discrete Numerical";
            }

            public String getTypeString(final Variable var)
            {
                visit(var);
                return fTypeString;
            }
        }

        @Override
        public TermType getAssociatedType()
        {
            return TermType.VARIABLE;
        }
        
        @Override
        public ModelTermSummary makeSummary(final ModelInspectionService modelService)
        {
            String displayName;
            String termType;
            
            try
            {
                final Variable var = modelService.getVariable(fKey);
                displayName = var.getDisplayName();
                termType = new TermTypeTranslator().getTypeString(var);
            }
            catch (final InvalidIdentifierException ex)
            {
                fLogger.debug(
                        "Using fallback values due to InvalidIdentifierException.", ex);
                
                displayName = fKey;
                termType = "";
            }

            final StringBuilder idBuilder = new StringBuilder(displayName);
            // Append the option value if specified.
            if (!Strings.isNullOrEmpty(fOptionValue))
            {
                idBuilder.append(" = ");
                idBuilder.append(fOptionValue);
            }

            return new ModelTermSummary(idBuilder.toString(), termType, fCoefficient);
        }
        
        @Override
        public VariableTermValidator getValidator(
                final ModelInspectionService modelService)
        {
            return new VariableTermValidator(modelService);
        }
        
        @Override
        public SingleVariableTerm buildTerm(final ModelInspectionService modelService)
                throws InvalidIdentifierException
        {
            final TermBuilder termBuilder = new TermBuilder();
            termBuilder.visit(modelService.getVariable(fKey));
            return termBuilder.getBuiltTerm();
        }
    }
    
    private class RuleTypeState implements TypeState
    {
        @Override
        public TermType getAssociatedType()
        {
            return TermType.RULE;
        }
        
        @Override
        public ModelTermSummary makeSummary(final ModelInspectionService modelService)
        {
            return new ModelTermSummary(fKey, "Rule", fCoefficient);
        }

        @Override
        public BaseValidator getValidator(final ModelInspectionService modelService)
        {
            return new RuleTermValidator(modelService);
        }

        @Override
        public DerivedTerm buildTerm(final ModelInspectionService modelService)
                throws InvalidIdentifierException
        {
            final Rule rule = modelService.getRule(fKey);
            return new DerivedTerm(fCoefficient, rule);
        }
    }
    
    /*
     * Validators
     * 
     * These could be inner classes of the State implementations above, but there are two
     * advantages of having them here:
     * 
     * - Less nesting (therefore easier to read and understand).
     * - Out here they can be static.
     */
    
    private abstract static class BaseValidator implements Validator
    {
        /**
         * Returns true if (and only if) the given class is {@link EditModelTerm} or a
         * subclass.
         */
        @Override
        public boolean supports(final Class<?> clazz)
        {
            return clazz.isAssignableFrom(EditModelTerm.class);
        }
        
        /**
         * Adds a validation error if the given EditModelTerm's optionValue is non-empty.
         * (We treat this as a validation error instead of silently ignoring.)
         * @param errors to capture the error
         */
        protected final void ensureNoOptionValue(
                final EditModelTerm editTerm, final Errors errors)
        {
            if (!editTerm.getOptionValue().isEmpty())
            {
                errors.rejectValue(
                        "optionValue",
                        ValidationCodes.NOT_APPLICABLE,
                        "not applicable");
            }
        }
    }
        
    /**
     * Validator for a Constant-type term.
     */
    private static class ConstantTermValidator extends BaseValidator
    {
        @Override
        public void validate(final Object target, final Errors errors)
        {
            final EditModelTerm editTerm = (EditModelTerm)target;
            
            if (!editTerm.getKey().isEmpty())
            {
                errors.rejectValue(
                        "key", ValidationCodes.NOT_APPLICABLE, "not applicable");
            }
            
            ensureNoOptionValue(editTerm, errors);
        }
    }
    
    /**
     * Validator for a Variable-type term.
     */
    private static class VariableTermValidator extends BaseValidator
    {
        private final ModelInspectionService fModelService;
        
        public VariableTermValidator(final ModelInspectionService modelService)
        {
            fModelService = modelService;
        }

        @Override
        public void validate(final Object target, final Errors errors)
        {
            final EditModelTerm editTerm = (EditModelTerm)target;
            
            if (ValidationUtils2.rejectIfEmpty(errors, "key"))
            {
                // Nothing more to validate: just stop.
                return;
            }

            try
            {
                final AbstractVariable var = fModelService.getVariable(editTerm.getKey());
                
                // If it's a DiscreteVariable, also ensure the given option is valid.
                if (var instanceof DiscreteVariable)
                {
                    if (ValidationUtils2.rejectIfEmpty(errors, "optionValue"))
                    {
                        // Again, nothing more to validate.
                        return;
                    }

                    final DiscreteVariable dv = (DiscreteVariable)var;
                    // Create an equivalent MultiSelectOption for contains() below.
                    final MultiSelectOption option =
                            new MultiSelectOption(editTerm.getOptionValue());
                    if (!dv.getOptions().contains(option))
                    {
                        errors.rejectValue(
                                "optionValue",
                                ValidationCodes.INVALID_OPTION,
                                "non-existent option");
                    }
                }
                else
                {
                    ensureNoOptionValue(editTerm, errors);
                }
            }
            catch (final InvalidIdentifierException ex)
            {
                errors.rejectValue(
                        "key",
                        ValidationCodes.INVALID_OPTION,
                        "non-existent key");
            }
        }
    }
    
    /**
     * Validator for a Rule-type term.
     */
    private static class RuleTermValidator extends BaseValidator
    {
        private final ModelInspectionService fModelService;
        
        public RuleTermValidator(final ModelInspectionService modelService)
        {
            fModelService = modelService;
        }
        
        @Override
        public void validate(final Object target, final Errors errors)
        {
            final EditModelTerm editTerm = (EditModelTerm)target;

            // Validate that they key/display name is given and exists.
            if (!ValidationUtils2.rejectIfEmpty(errors, "key"))
            {
                try
                {
                    fModelService.getRule(editTerm.getKey());
                }
                catch (final InvalidIdentifierException ex)
                {
                    errors.rejectValue(
                            "key", ValidationCodes.INVALID_OPTION, "non-existent key");
                }
            }

            ensureNoOptionValue(editTerm, errors);
        }
    }
}
