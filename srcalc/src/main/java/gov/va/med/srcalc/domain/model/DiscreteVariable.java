package gov.va.med.srcalc.domain.model;

import java.util.List;

/**
 * A {@link Variable} whose value is one of a discrete set.
 */
public interface DiscreteVariable extends Variable
{
    /**
     * Returns the indexed list of possible discrete values. The returned list
     * may or may not be modifiable.
     */
    public List<MultiSelectOption> getOptions();
}
