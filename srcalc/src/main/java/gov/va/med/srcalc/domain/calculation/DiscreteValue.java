package gov.va.med.srcalc.domain.calculation;

import gov.va.med.srcalc.domain.model.DiscreteTerm;
import gov.va.med.srcalc.domain.model.DiscreteVariable;
import gov.va.med.srcalc.domain.model.MultiSelectOption;

/**
 * <p>A value of a {@link DiscreteVariable}.</p>
 * 
 * <p>(This simple interface supports {@link DiscreteTerm}.)</p>
 */
public interface DiscreteValue extends Value
{
    public MultiSelectOption getSelectedOption();
}
