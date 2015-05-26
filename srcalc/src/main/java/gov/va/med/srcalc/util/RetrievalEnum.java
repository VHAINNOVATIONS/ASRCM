package gov.va.med.srcalc.util;

import gov.va.med.srcalc.domain.Patient;
import gov.va.med.srcalc.domain.model.Variable;
import gov.va.med.srcalc.web.view.VariableEntry;

public enum RetrievalEnum
{
    GENDER
    {
        @Override
        public void execute(final Patient patient, final VariableEntry variableEntry, final Variable variable,
                final String key)
        {
            variableEntry.getDynamicValues().put(key, patient.getGender());
        }
    },
    AGE
    {
        @Override
        public void execute(final Patient patient, final VariableEntry variableEntry, final Variable variable,
                final String key)
        {
            variableEntry.getDynamicValues().put(key, String.valueOf(patient.getAge()));
        }
    },
    BMI
    {
        @Override
        public void execute(final Patient patient, final VariableEntry variableEntry, final Variable variable,
                final String key)
        {
            if (patient.getBmi() != null)
            {
                variableEntry.getDynamicValues().put(key, String.valueOf(patient.getBmi().getValue()));
                variable.setRetrievalDateString(VariableEntry.makeRetrievalMessage(patient.getBmi().getMeasureDate()));
            }
        }
    },
    WEIGHT
    {
        @Override
        public void execute(final Patient patient, final VariableEntry variableEntry, final Variable variable,
                final String key)
        {
            if (patient.getWeight() != null)
            {
                variableEntry.getDynamicValues().put(key, String.valueOf(patient.getWeight().getValue()));
                variable.setRetrievalDateString(VariableEntry.makeRetrievalMessage(patient.getWeight().getMeasureDate()));
            }
        }
    },
    WEIGHT_6_MONTHS_AGO
    {
        @Override
        public void execute(final Patient patient, final VariableEntry variableEntry, final Variable variable,
                final String key)
        {
            if (patient.getWeight6MonthsAgo() != null)
            {
                variableEntry.getDynamicValues().put(key, String.valueOf(patient.getWeight6MonthsAgo().getValue()));
                variable.setRetrievalDateString(VariableEntry.makeRetrievalMessage(patient.getWeight6MonthsAgo().getMeasureDate()));
            }
        }
    },
    HEIGHT
    {
        @Override
        public void execute(final Patient patient, final VariableEntry variableEntry, final Variable variable,
                final String key)
        {
            if (patient.getHeight() != null)
            {
                variableEntry.getDynamicValues().put(key, String.valueOf(patient.getHeight().getValue()));
                variable.setRetrievalDateString(VariableEntry.makeRetrievalMessage(patient.getHeight().getMeasureDate()));
            }
        }
    },
    CARDIAC_AGE
    {
        @Override
        public void execute(final Patient patient, final VariableEntry variableEntry, final Variable variable,
                final String key)
        {
            variableEntry.getDynamicValues().put(key, String.valueOf(patient.getAge()));
        }
    },
    ALBUMIN
    {
        @Override
        public void execute(final Patient patient, final VariableEntry variableEntry, final Variable variable,
                final String key)
        {
            final RetrievedValue value = patient.getLabs().get("ALBUMIN");
            variableEntry.getDynamicValues().put(key, String.valueOf(value.getValue()));
            variable.setRetrievalDateString(VariableEntry.makeRetrievalMessage(value.getMeasureDate()));
        }
    };
    
    public abstract void execute(final Patient patient, final VariableEntry variableEntry, final Variable variable,
            final String key);
}
