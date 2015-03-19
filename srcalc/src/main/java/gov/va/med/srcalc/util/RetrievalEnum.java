package gov.va.med.srcalc.util;

import gov.va.med.srcalc.domain.Patient;
import gov.va.med.srcalc.domain.variable.Variable;
import gov.va.med.srcalc.web.view.VariableEntry;

public enum RetrievalEnum {
	GENDER {
		@Override
		public void execute(final Patient patient, final VariableEntry variableEntry,
				final Variable variable, final String key)
		{
			variableEntry.getDynamicValues().put(key, patient.getGender());
		}
	}, AGE {
		@Override
		public void execute(final Patient patient, final VariableEntry variableEntry,
				final Variable variable, final String key)
		{
			variableEntry.getDynamicValues().put(key, String.valueOf(patient.getAge()));
		}
	}, BMI {
		@Override
		public void execute(final Patient patient, final VariableEntry variableEntry,
				final Variable variable, final String key)
		{
			if(patient.getBmiDate() != null)
			{
				variableEntry.getDynamicValues().put(key, String.valueOf(patient.getBmi()));
				variable.setRetrievalDateString(VariableEntry.makeRetrievalMessage(patient.getBmiDate()));
			}
		}
	}, WEIGHT {
		@Override
		public void execute(final Patient patient, final VariableEntry variableEntry,
				final Variable variable, final String key)
		{
			if(patient.getWeightDate() != null)
			{
				variableEntry.getDynamicValues().put(key, String.valueOf(patient.getWeight()));
				variable.setRetrievalDateString(VariableEntry.makeRetrievalMessage(patient.getWeightDate()));
			}
		}
	}, WEIGHT_6_MONTHS_AGO {
		@Override
		public void execute(final Patient patient, final VariableEntry variableEntry,
				final Variable variable, final String key)
		{
			if(patient.getWeight6MonthsAgoDate() != null)
			{
				variableEntry.getDynamicValues().put(key, String.valueOf(patient.getWeight6MonthsAgo()));
				variable.setRetrievalDateString(VariableEntry.makeRetrievalMessage(patient.getWeight6MonthsAgoDate()));
			}
		}
	}, HEIGHT {
		@Override
		public void execute(final Patient patient, final VariableEntry variableEntry,
				final Variable variable, final String key)
		{
			if(patient.getHeightDate() != null)
			{
				variableEntry.getDynamicValues().put(key, String.valueOf(patient.getHeight()));
				variable.setRetrievalDateString(VariableEntry.makeRetrievalMessage(patient.getHeightDate()));
			}
		}
	};
	
	public abstract void execute(final Patient patient, final VariableEntry variableEntry,
			final Variable variable, final String key);
}
