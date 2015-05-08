package gov.va.med.srcalc.domain.calculation;

import gov.va.med.srcalc.domain.model.MultiSelectOption;

public interface DiscreteValue extends Value
{
    public MultiSelectOption getSelectedOption();
}
