package gov.va.med.srcalc.domain.calculation;

import gov.va.med.srcalc.domain.Patient;
import gov.va.med.srcalc.domain.model.BooleanVariable;
import gov.va.med.srcalc.domain.model.DiscreteNumericalVariable;
import gov.va.med.srcalc.domain.model.MultiSelectVariable;
import gov.va.med.srcalc.domain.model.NumericalVariable;
import gov.va.med.srcalc.domain.model.ProcedureVariable;
import gov.va.med.srcalc.domain.model.Variable;
import gov.va.med.srcalc.domain.model.VariableGroup;
import gov.va.med.srcalc.vista.HealthFactor;
import gov.va.med.srcalc.web.view.BooleanVariableView;
import gov.va.med.srcalc.web.view.DiscreteNumericalVariableView;
import gov.va.med.srcalc.web.view.DisplayItem;
import gov.va.med.srcalc.web.view.MultiSelectVariableView;
import gov.va.med.srcalc.web.view.NumericalVariableView;
import gov.va.med.srcalc.web.view.ProcedureVariableView;
import gov.va.med.srcalc.web.view.ReferenceItem;
import gov.va.med.srcalc.web.view.VariableView;

import java.text.SimpleDateFormat;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger LOGGER = LoggerFactory.getLogger(PopulatedDisplayGroup.class);
    
    private final List<DisplayItem> fDisplayItems;
    
    private final List<Variable> fVariables;
    
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
        fVariables = new ArrayList<>(variables);
        fDisplayItems = new ArrayList<>();
        for(final Variable var: variables)
        {
            // TODO: If the variable has specified keys that have reference information,
            // create a String that holds that reference information and use it in the view constructors.
            VariableView varView;
            if(var instanceof BooleanVariable)
            {
                varView = new BooleanVariableView((BooleanVariable) var, "");
            }
            else if(var instanceof DiscreteNumericalVariable)
            {
                varView = new DiscreteNumericalVariableView((DiscreteNumericalVariable) var, "");
            }
            else if(var instanceof NumericalVariable)
            {
                varView = new NumericalVariableView((NumericalVariable) var, "");
            }
            else if(var instanceof MultiSelectVariable)
            {
                varView = new MultiSelectVariableView((MultiSelectVariable) var, "");
            }
            else if(var instanceof ProcedureVariable)
            {
                varView = new ProcedureVariableView((ProcedureVariable) var, "");
            }
            else
            {
                LOGGER.debug("Unexpected Variable type while creating a PopulatedDisplayGroup. Key: {}",
                        var.getKey());
                // Unexpected variable type
                continue;
            }
            fDisplayItems.add(varView);
        }
        
        // These group names are all well-known and are hard-coded as such.
        if(group.getName().equalsIgnoreCase("Clinical Conditions or Diseases - Recent"))
        {
            final List<String> factorList = new ArrayList<String>();
            // Output the date without the time of day.
            final SimpleDateFormat format = new SimpleDateFormat("MM/dd/yy");
            for(final HealthFactor factor: patient.getHealthFactors())
            {
                factorList.add(String.format("%s %s%n", format.format(factor.getDate()), factor.getFactor()));
            }
            final DisplayItem refInfo = new ReferenceItem("Health Factors", variables.get(0).getGroup(), factorList);
            fDisplayItems.add(0, refInfo);
        }
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
}
