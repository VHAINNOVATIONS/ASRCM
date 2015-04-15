package gov.va.med.srcalc.domain;

import gov.va.med.srcalc.domain.model.RiskModel;
import gov.va.med.srcalc.domain.variable.*;
import gov.va.med.srcalc.util.MissingValuesException;

import java.io.Serializable;
import java.util.*;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a risk calculation: either in-progress, calculated, or signed.
 */
public class Calculation implements Serializable
{
	private static final Logger fLogger = LoggerFactory.getLogger(Calculation.class);
    /**
     * Change this when changing the class!
     */
    private static final long serialVersionUID = 1L;

    private DateTime fStartDateTime;
    private Patient fPatient;
    private Specialty fSpecialty;
    private SortedSet<Value> fValues;
    private SortedMap<String, Double> fOutcomes;
    
    /**
     * This class presents a pure JavaBean interface, with a default constructor
     * and mutators for all fields. It also offers convenience factory methods
     * below.
     */
    public Calculation()
    {
        fStartDateTime = new DateTime();
        fValues = new TreeSet<>(new ValueVariableComparator(new DisplayNameComparator()));
    }
    
    public static Calculation forPatient(final Patient patient)
    {
        final Calculation c = new Calculation();
        c.setPatient(patient);
        return c;
    }
    
    /**
     * The DateTime at which this Calculation first began (for example, when the
     * user first opened the tool).
     */
    public DateTime getStartDateTime()
    {
        return fStartDateTime;
    }

    public void setStartDateTime(final DateTime startDateTime)
    {
        this.fStartDateTime = startDateTime;
    }

    public Patient getPatient()
    {
        return fPatient;
    }

    public void setPatient(final Patient patient)
    {
        this.fPatient = patient;
    }

    public Specialty getSpecialty()
    {
        return fSpecialty;
    }

    public void setSpecialty(final Specialty specialty)
    {
        this.fSpecialty = specialty;
    }
    
    /**
     * Returns the Set of {@link Variable}s that the calculation requires.
     * @throws IllegalStateException if no specialty has been set.
     * @return an unmodifiable list of Variables.
     */
    public Set<Variable> getVariables()
    {
        // Ensure we are in the proper state.
        if (fSpecialty == null)
        {
            throw new IllegalStateException(
                    "Cannot return list of variables because no specialty has been set.");
        }
        
        return Collections.unmodifiableSet(fSpecialty.getModelVariables());
    }
    
    /**
     * Builds a brand-new, sorted list of {@link PopulatedVariableGroup}s from
     * the given variables. The groups are sorted by their natural ordering and
     * each variable list is sorted by display name.
     */
    protected List<PopulatedVariableGroup> buildVariableGroupList(
            Collection<? extends Variable> variables)
    {
        // Bucket the Variables according to VariableGroup.
        final HashMap<VariableGroup, List<Variable>> map = new HashMap<>();
        for (final Variable var : variables)
        {
            final VariableGroup group = var.getGroup();
            if (!map.containsKey(group))
            {
                final ArrayList<Variable> varList = new ArrayList<>();
                map.put(group, varList);
            }
            map.get(group).add(var);
        }
        
        // Transform the map into PopulatedVariableGroups.
        final ArrayList<PopulatedVariableGroup> groupList =
                new ArrayList<>(map.values().size());
        final DisplayNameComparator comparator = new DisplayNameComparator();
        for (final List<Variable> varList : map.values())
        {
            Collections.sort(varList, comparator);
            groupList.add(new PopulatedVariableGroup(varList));
        }
        
        // Finally, sort the List.
        Collections.sort(groupList);
        
        return groupList;
    }
    
    /**
     * Returns all {@link Variable}s associated with this Specialty, bucketed
     * into their groups.
     * @throws IllegalStateException if no specialty has been set.
     * @return an immutable List, sorted in group order
     */
    public List<PopulatedVariableGroup> getVariableGroups()
    {
        // Ensure we are in the proper state.
        if (fSpecialty == null)
        {
            throw new IllegalStateException(
                    "Cannot return list of variables because no specialty has been set.");
        }
        
        return Collections.unmodifiableList(
                buildVariableGroupList(getVariables()));
    }

    public SortedSet<Value> getValues()
    {
        return fValues;
    }
    
    /**
     * For bean construction only. Replaces the internal Set of Values with the
     * given one.
     */
    void setValues(final SortedSet<Value> values)
    {
        fValues = values;
    }
    
    /**
     * Returns an unmodifiable view of the last-calculated outcomes.
     */
    public SortedMap<String, Double> getOutcomes()
    {
        // At time of writing, fOutcomes is already unmodifiable, but re-wrap it
        // just in case we change our minds.
        return Collections.unmodifiableSortedMap(fOutcomes);
    }

    /**
     * Runs the calculation for each outcome with the given Values.
     * @throws IllegalArgumentException if incomplete values are provided
     * @return the outcomes for convenience
     * @throws MissingValuesException if there are any required variables without an assigned value
     */
    public SortedMap<String, Double> calculate(final Collection<Value> values) throws MissingValuesException
    {
        // Run the calculation first to make sure we don't get any exceptions.
        final TreeMap<String, Double> outcomes = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        // TODO: Change into a Set.
        final List<MissingValueException> missingValues = new ArrayList<MissingValueException>();
        for (final RiskModel model : getSpecialty().getRiskModels())
        {
            try
            {
            	outcomes.put(model.getDisplayName(), model.calculate(values));
            }
            catch(MissingValuesException e)
            {
            	missingValues.addAll(e.getMissingValues());
            }
        }

        if(missingValues.size() > 0)
        {
        	fLogger.debug("Could not run calculation due to missing values: {}", missingValues);
        	throw new MissingValuesException("The calculation is missing values.", missingValues);
        }
        // Store the given values for reference.
        fValues.clear();
        fValues.addAll(values);

        fOutcomes = Collections.unmodifiableSortedMap(outcomes);

        return fOutcomes;
    }
    
    public String buildNoteBody()
	{
    	final StringBuilder returnString = new StringBuilder();
    	// Build the note body where each section is separated by a blank line
		// Specialty
    	returnString.append(String.format("Specialty = %s%n%n", this.getSpecialty().toString()));
		// Variable display names and values
    	returnString.append(String.format("Calculation Inputs%n"));
		for(final Value value: this.getValues())
		{
			returnString.append(String.format("%s = %s%n", value.getVariable().getDisplayName(), value.getDisplayString()));
		}
		// Model results
		returnString.append(String.format("%nResults%n"));
		for(final String key: this.getOutcomes().keySet())
		{
			returnString.append(String.format("%s = %s%%%n", key, this.getOutcomes().get(key) * 100));
		}
		return returnString.toString();
	}
}
