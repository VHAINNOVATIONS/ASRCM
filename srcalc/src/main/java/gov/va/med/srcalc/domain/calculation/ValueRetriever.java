package gov.va.med.srcalc.domain.calculation;

import java.text.SimpleDateFormat;
import java.util.Date;

import gov.va.med.srcalc.domain.Patient;
import gov.va.med.srcalc.domain.model.Variable;
import gov.va.med.srcalc.vista.RpcVistaPatientDao;
import gov.va.med.srcalc.web.view.VariableEntry;

public enum ValueRetriever
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
                final String retrievalString = makeRetrievalString(
                        patient.getBmi().getValue(),
                        patient.getBmi().getMeasureDate(),
                        patient.getBmi().getUnits());
                variableEntry.getDynamicValues().put(
                        key + VariableEntry.SEPARATOR + VariableEntry.RETRIEVAL_STRING,
                        retrievalString);
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
                final String retrievalString = makeRetrievalString(
                        patient.getWeight().getValue(),
                        patient.getWeight().getMeasureDate(),
                        patient.getWeight().getUnits());
                variableEntry.getDynamicValues().put(
                        key + VariableEntry.SEPARATOR + VariableEntry.RETRIEVAL_STRING,
                        retrievalString);
                
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
                final String retrievalString = makeRetrievalString(
                        patient.getWeight6MonthsAgo().getValue(),
                        patient.getWeight6MonthsAgo().getMeasureDate(),
                        patient.getWeight6MonthsAgo().getUnits());
                variableEntry.getDynamicValues().put(
                        key + VariableEntry.SEPARATOR + VariableEntry.RETRIEVAL_STRING,
                        retrievalString);
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
                final String retrievalString = makeRetrievalString(
                        patient.getHeight().getValue(),
                        patient.getHeight().getMeasureDate(),
                        patient.getHeight().getUnits());
                variableEntry.getDynamicValues().put(
                        key + VariableEntry.SEPARATOR + VariableEntry.RETRIEVAL_STRING,
                        retrievalString);
            }
        }
    },
    ALBUMIN
    {
        @Override
        public void execute(final Patient patient, final VariableEntry variableEntry, final Variable variable,
                final String key)
        {
            final RetrievedValue albuminValue = patient.getLabs().get("ALBUMIN");
            if(albuminValue != null)
            {
                variableEntry.getDynamicValues().put(key, String.valueOf(albuminValue.getValue()));
                final String retrievalString = makeRetrievalString(
                        albuminValue.getValue(),
                        albuminValue.getMeasureDate(),
                        albuminValue.getUnits());
                variableEntry.getDynamicValues().put(
                        key + VariableEntry.SEPARATOR + VariableEntry.RETRIEVAL_STRING,
                        retrievalString);
            }
        }
    };
    
    public abstract void execute(final Patient patient, final VariableEntry variableEntry, final Variable variable,
            final String key);
    
    /**
     * Make a string to tell the user information about the automatically retrieved value.
     * @param value the retrieved value to display
     * @param measureDate the date on which the value was measured
     * @param units the units in which the value was measured, can be empty but not null
     * @return
     */
    protected String makeRetrievalString(final double value, final Date measureDate, final String units)
    {
        final SimpleDateFormat originalFormat = new SimpleDateFormat(RpcVistaPatientDao.VISTA_DATE_OUTPUT_FORMAT);
        final String dateString = " on " + originalFormat.format(measureDate);
        String unitString = "";
        if(units.length() > 0)
        {
            unitString = " " + units;
        }
        return String.format("(Retrieved: %.2f%s%s)", value, unitString, dateString);
    }
}
