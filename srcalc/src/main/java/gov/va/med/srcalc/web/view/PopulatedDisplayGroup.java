package gov.va.med.srcalc.web.view;

import gov.va.med.srcalc.domain.HealthFactor;
import gov.va.med.srcalc.domain.Patient;
import gov.va.med.srcalc.domain.model.BooleanVariable;
import gov.va.med.srcalc.domain.model.DiscreteNumericalVariable;
import gov.va.med.srcalc.domain.model.ExceptionlessVariableVisitor;
import gov.va.med.srcalc.domain.model.MultiSelectVariable;
import gov.va.med.srcalc.domain.model.NumericalVariable;
import gov.va.med.srcalc.domain.model.ProcedureVariable;
import gov.va.med.srcalc.domain.model.Variable;
import gov.va.med.srcalc.domain.model.VariableGroup;

import java.util.*;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.google.common.collect.ImmutableList;

/**
 * <p>Contains a list of {@link Variable}s in the same {@link VariableGroup}.
 * This is a separate class from VariableGroup because the latter is a
 * persistent entity.</p>
 * 
 * <p>Per Effective Java Item 17, this class is marked final because it was not
 * designed for inheritance.</p>
 */
public final class PopulatedDisplayGroup implements Comparable<PopulatedDisplayGroup>
{
    private static final String CLINICAL_GROUP = "Clinical Conditions or Diseases - Recent";
    private static final String MEDICATIONS_GROUP = "Medications";
    
    private final List<DisplayItem> fDisplayItems;
    
    private final ImmutableList<Variable> fVariables;
    
    /**
     * Constructs an instance from the given List of variables. Also populates the groups with
     * any patient notes that are automatically retrieved.
     * @param variables must have at least one member
     * @throws IllegalArgumentException if the given list is empty or if it
     * contains Variables with different groups
     */
    public PopulatedDisplayGroup(final List<? extends Variable> variables, final Patient patient)
    {
        // Check precondition: variable list is not empty
        if (variables.isEmpty())
        {
            throw new IllegalArgumentException("variable list must not be empty");
        }
        
        // Check precondition: variables all have the same group
        final VariableGroup group = variables.get(0).getGroup();
        for (final Variable var : variables)
        {
            if (!var.getGroup().equals(group))
            {
                throw new IllegalArgumentException(
                        "variables do not all have the same group");
            }
        }
        
        // Preconditions satisfied: build views of all of the necessary variables
        // so that they can be represented as DisplayItem instances.
        // Store variables for reference later.
        fVariables = ImmutableList.copyOf(variables);
        fDisplayItems = new ArrayList<>();
        for(final Variable var: variables)
        {
            // TODO: If the variable has specified keys that have reference information,
            // create a String that holds that reference information and use it in the view constructors.
            final Visitor visitor = new Visitor();
            fDisplayItems.add(visitor.getView(var, ""));
        }
        
        addReferenceInfo(group, patient);
    }
    
    /**
     * Constructs an instance and populates the groups with any patient notes that are
     * automatically retrieved. This constructor should only be used to place reference information
     * into the group after all PopulatedDisplayGroups have been built for variables.
     * @param group the VariableGroup to which the reference information belongs
     * @param patient the current patient in the calculation
     */
    public PopulatedDisplayGroup(final VariableGroup group, final Patient patient)
    {
        fVariables = ImmutableList.of();
        fDisplayItems = new ArrayList<>();
        addReferenceInfo(group, patient);
    }

    /**
     * <p>Returns the List of Variables, always containing at least 1 Variable. All
     * contained Variables will have equal {@link VariableGroup}s. The
     * collection itself is immutable, though the variables themselves are not.</p>
     * 
     * <p>Order is preserved from construction.</p>
     */
    public List<Variable> getVariables()
    {
        return Collections.unmodifiableList(fVariables);
    }
    
    /**
     * <p>Returns the List of DisplayItems, always containing at least 1 VariableView. All
     * contained VariableViews will have equal {@link VariableGroup}s. The
     * collection itself is immutable, as well as the VariableViews</p>
     * 
     * <p>Order is preserved from construction.</p>
     */
    public List<DisplayItem> getDisplayItems()
    {
        return Collections.unmodifiableList(fDisplayItems);
    }
    
    public VariableGroup getGroup()
    {
        // Since all Variables have the same group, just use the first's group.
        return fDisplayItems.get(0).getDisplayGroup();
    }
    
    /**
     * Returns the VariableGroup's name.
     */
    public String getName()
    {
        return getGroup().getName();
    }
    
    private void addReferenceInfo(final VariableGroup group, final Patient patient)
    {
     // These group names are all well-known and are hard-coded as such.
        if(group.getName().equalsIgnoreCase(CLINICAL_GROUP))
        {
            final List<String> factorList = new ArrayList<String>();
            // Output the date without the time of day.
            final DateTimeFormatter format = DateTimeFormat.forPattern("MM/dd/yy");
            for(final HealthFactor factor: patient.getHealthFactors())
            {
                factorList.add(String.format("%s %s%n", format.print(factor.getDate()), factor.getName()));
            }
            final DisplayItem refInfo = new ReferenceItem("Health Factors", group, factorList);
            fDisplayItems.add(0, refInfo);
        }
        else if(group.getName().equalsIgnoreCase(MEDICATIONS_GROUP))
        {
            fDisplayItems.add(0, new ReferenceItem("Active Medications", group, patient.getActiveMedications()));
        }
    }
    
    /**
     * This equals() method depends solely on the variables due to the fact that
     * it is possible for {@link DisplayItem}s to have equal attributes such as
     * display names and display groups, but be different variables.
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof PopulatedDisplayGroup)
        {
            final PopulatedDisplayGroup other = (PopulatedDisplayGroup)obj;
            return this.getVariables().equals(other.getVariables());
        }
        else
        {
            return false;
        }
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(getVariables());
    }
    
    /**
     * <p>Compares this {@link PopulatedDisplayGroup} with another. Comparison
     * is based only on each object's associated {@link VariableGroup}.</p>
     * 
     * <p>Not consistent with equals: two {@link PopulatedDisplayGroup}s with
     * different sets of variables from the same group are non-equal but will
     * compare as equal.</p>
     */
    @Override
    public int compareTo(PopulatedDisplayGroup o)
    {
        return this.getGroup().compareTo(o.getGroup());
    }
    
    @Override
    public String toString()
    {
        return String.format(
                "Display Group '%s' with display items %s",
                getGroup(),
                getDisplayItems());
    }
    
    private static class Visitor extends ExceptionlessVariableVisitor
    {
        private VariableView fView;
        private String fReferenceInfo;
        
        public Visitor()
        {
        }
        
        @Override
        public void visitNumerical(final NumericalVariable variable)
        {
            fView = new NumericalVariableView(variable, ImmutableList.of(fReferenceInfo));
        }
        
        @Override
        public void visitBoolean(final BooleanVariable variable)
        {
            fView = new BooleanVariableView(variable, ImmutableList.of(fReferenceInfo));
        }
        
        @Override
        public void visitMultiSelect(final MultiSelectVariable variable)
        {
            fView = new MultiSelectVariableView(variable, ImmutableList.of(fReferenceInfo));
        }
        
        @Override
        public void visitProcedure(final ProcedureVariable variable)
        {
            fView = new ProcedureVariableView(variable, ImmutableList.of(fReferenceInfo));
        }
        
        @Override
        public void visitDiscreteNumerical(final DiscreteNumericalVariable variable)
        {
            fView = new DiscreteNumericalVariableView(variable, ImmutableList.of(fReferenceInfo));
        }
        
        /**
         * Visits the given variable and returns the constructed view.
         */
        public VariableView getView(final Variable variable, final String referenceInfo)
        {
            fReferenceInfo = referenceInfo;
            visit(variable);
            return fView;
        }

    }
}
