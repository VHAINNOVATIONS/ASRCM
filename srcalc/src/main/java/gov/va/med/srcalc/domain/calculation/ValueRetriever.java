package gov.va.med.srcalc.domain.calculation;

import com.google.common.collect.ImmutableSet;

import gov.va.med.srcalc.domain.Patient;
import gov.va.med.srcalc.domain.ReferenceNote;
import gov.va.med.srcalc.domain.VistaLabs;
import gov.va.med.srcalc.domain.model.Variable;
import gov.va.med.srcalc.util.XmlDateAdapter;
import gov.va.med.srcalc.web.view.VariableEntry;

/**
 * <p>The set of retrievers that extract a particular variable's value from a
 * Patient object.</p>
 * 
 * <p>Most of these retrieve a numerical (floating-point) value, but GENDER
 * retrieves discrete String values. TODO: this enumeration should probably be
 * split based on this discrepancy.</p>
 */
public enum ValueRetriever
{
    GENDER
    {
        @Override
        public void execute(final Patient patient, final VariableEntry variableEntry, final Variable variable,
                final String key)
        {
            // Note that the MultiSelectOption value must exactly match the enum name for
            // this to work.
            variableEntry.getDynamicValues().put(key, patient.getGender().name());
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
                final String retrievalString = VariableEntry.makeRetrievalString(
                        patient.getBmi().getValue(),
                        patient.getBmi().getMeasureDate(),
                        patient.getBmi().getUnits());
                variableEntry.setMeasureDate(key, retrievalString);
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
                final String retrievalString = VariableEntry.makeRetrievalString(
                        patient.getWeight().getValue(),
                        patient.getWeight().getMeasureDate(),
                        patient.getWeight().getUnits());
                variableEntry.setMeasureDate(key, retrievalString);
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
                final String retrievalString = VariableEntry.makeRetrievalString(
                        patient.getWeight6MonthsAgo().getValue(),
                        patient.getWeight6MonthsAgo().getMeasureDate(),
                        patient.getWeight6MonthsAgo().getUnits());
                variableEntry.setMeasureDate(key, retrievalString);
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
                final String retrievalString = VariableEntry.makeRetrievalString(
                        patient.getHeight().getValue(),
                        patient.getHeight().getMeasureDate(),
                        patient.getHeight().getUnits());
                variableEntry.setMeasureDate(key, retrievalString);
            }
        }
    },
    ALBUMIN
    {
        @Override
        public void execute(final Patient patient, final VariableEntry variableEntry, final Variable variable,
                final String key)
        {
            addLabValue(VistaLabs.ALBUMIN, patient, variableEntry, key);
        }
    },
    CREATININE
    {
        @Override
        public void execute(final Patient patient, final VariableEntry variableEntry, final Variable variable,
                final String key)
        {
            addLabValue(VistaLabs.CREATININE, patient, variableEntry, key);
        }
    },
    ALKALINE_PHOSPHATASE
    {
        @Override
        public void execute(final Patient patient, final VariableEntry variableEntry, final Variable variable,
                final String key)
        {
            addLabValue(VistaLabs.ALKALINE_PHOSPHATASE, patient, variableEntry, key);
        }
    },
    BUN
    {
        @Override
        public void execute(final Patient patient, final VariableEntry variableEntry, final Variable variable,
                final String key)
        {
            addLabValue(VistaLabs.BUN, patient, variableEntry, key);
        }
    },
    SGOT
    {
        @Override
        public void execute(final Patient patient, final VariableEntry variableEntry, final Variable variable,
                final String key)
        {
            addLabValue(VistaLabs.SGOT, patient, variableEntry, key);
        }
    },
    WBC
    {
        @Override
        public void execute(final Patient patient, final VariableEntry variableEntry, final Variable variable,
                final String key)
        {
            addLabValue(VistaLabs.WBC, patient, variableEntry, key);
        }
    },
    PLATELETS
    {
        @Override
        public void execute(final Patient patient, final VariableEntry variableEntry, final Variable variable,
                final String key)
        {
            addLabValue(VistaLabs.PLATELETS, patient, variableEntry, key);
        }
    },
    HEMATOCRIT
    {
        @Override
        public void execute(final Patient patient, final VariableEntry variableEntry, final Variable variable,
                final String key)
        {
            addLabValue(VistaLabs.HEMATOCRIT, patient, variableEntry, key);
        }
    },
    SODIUM
    {
        @Override
        public void execute(final Patient patient, final VariableEntry variableEntry, final Variable variable,
                final String key)
        {
            addLabValue(VistaLabs.SODIUM, patient, variableEntry, key);
        }
    },
    INR
    {
        @Override
        public void execute(final Patient patient, final VariableEntry variableEntry, final Variable variable,
                final String key)
        {
            addLabValue(VistaLabs.INR, patient, variableEntry, key);
        }
    },
    BILIRUBIN
    {
        @Override
        public void execute(final Patient patient, final VariableEntry variableEntry, final Variable variable,
                final String key)
        {
            addLabValue(VistaLabs.BILIRUBIN, patient, variableEntry, key);
        }
    },
    PTT
    {
        @Override
        public void execute(final Patient patient, final VariableEntry variableEntry, final Variable variable,
                final String key)
        {
            addLabValue(VistaLabs.PTT, patient, variableEntry, key);
        }
    },
    ADL_NOTES
    {
        @Override
        public String retrieveReferenceInfo(final Patient patient, final Variable var)
        {
            final StringBuilder variableRefInfo = new StringBuilder();
            for(final ReferenceNote note: patient.getAdlNotes())
            {
                // Add a line break here so that the note body is separated.
                variableRefInfo.append(String.format("Local Title: %s Sign Date: %s%n%s%n%n",
                        note.getLocalTitle(), 
                        XmlDateAdapter.REFERENCE_NOTE_DATE_FORMAT.print(note.getSignDate()),
                        note.getNoteBody()));
            }
            return variableRefInfo.toString();
        }
    },
    POTASSIUM
    {
        @Override
        public void execute(final Patient patient, final VariableEntry variableEntry, final Variable variable,
            final String key)
        {
            addLabValue(VistaLabs.POTASSIUM, patient, variableEntry, key);
        }
    },
    HGA1C
    {
        @Override
        public void execute(final Patient patient, final VariableEntry variableEntry, final Variable variable,
            final String key)
        {
            addLabValue(VistaLabs.HGA1C, patient, variableEntry, key);
        }
    },
    GLUCOSE
    {
        @Override
        public void execute(final Patient patient, final VariableEntry variableEntry, final Variable variable,
            final String key)
        {
            addLabValue(VistaLabs.GLUCOSE, patient, variableEntry, key);
        }
    },
    DNR_NOTES
    {
        @Override
        public String retrieveReferenceInfo(final Patient patient, final Variable var)
        {
            final StringBuilder variableRefInfo = new StringBuilder();
            for(final ReferenceNote note: patient.getDnrNotes())
            {
                // Add a line break here so that the note body is separated.
                variableRefInfo.append(String.format("Local Title: %s Sign Date: %s%n%s%n%n",
                        note.getLocalTitle(), XmlDateAdapter.REFERENCE_NOTE_DATE_FORMAT.print(note.getSignDate()), note.getNoteBody()));
            }
            return variableRefInfo.toString();
        }
    };
    
    /**
     * An ImmutableSet including any ValueRetrievers that are used to 
     * add reference information.
     */
    public static final ImmutableSet<ValueRetriever> REFERENCE_NOTES_SET = 
            ImmutableSet.of(DNR_NOTES, ADL_NOTES);
    /**
     * An ImmutableSet including any ValueRetrievers that are used for Boolean
     * variables.
     */
    public static final ImmutableSet<ValueRetriever> BOOLEAN_SET = 
            new ImmutableSet.Builder<ValueRetriever>()
            .addAll(REFERENCE_NOTES_SET)
            .build();
    /**
     * An ImmutableSet including any ValueRetrievers that are used for multi-select
     * variables.
     */
    public static final ImmutableSet<ValueRetriever> MULTI_SELECT_SET = 
            new ImmutableSet.Builder<ValueRetriever>()
            .addAll(REFERENCE_NOTES_SET)
            .add(GENDER)
            .build();
    /**
     * An ImmutableSet including any ValueRetrievers that are used for numerical
     * variables.
     */
    public static final ImmutableSet<ValueRetriever> NUMERICAL_SET = 
            new ImmutableSet.Builder<ValueRetriever>()
            .addAll(REFERENCE_NOTES_SET)
            .add(AGE)
            .add(BMI)
            .add(WEIGHT)
            .add(WEIGHT_6_MONTHS_AGO)
            .add(HEIGHT)
            .add(ALBUMIN)
            .add(CREATININE)
            .add(ALKALINE_PHOSPHATASE)
            .add(BUN)
            .add(SGOT)
            .add(WBC)
            .add(PLATELETS)
            .add(HEMATOCRIT)
            .add(SODIUM)
            .add(INR)
            .add(BILIRUBIN)
            .add(PTT)
            .build();
    
    /**
     * Attempt to add the retrieved value to the {@link VariableEntry} object. Do nothing if
     * there is no retrieved value for the specified variable.
     * @param patient the current patient
     * @param variableEntry the {@link VariableEntry} object to put the retrieved value into
     * @param variable the variable to get the retrieved value for
     * @param key the variable's fully qualified key (i.e. "bmi$numerical" for bmi)
     */
    public void execute(final Patient patient, final VariableEntry variableEntry, final Variable variable,
            final String key)
    {
    }
    
    /**
     * Attempt to retrieve any reference information for this instance. Return an empty 
     * String if here is no reference information for the specified variable.
     * @param patient the current patient
     * @param var the variable to get reference information for
     * @return the reference information or an empty String
     */
    public String retrieveReferenceInfo(final Patient patient, final Variable var)
    {
        return "";
    }
    
    /**
     * If there was a retrieved value for the specified lab, it will be added to the variable entry.
     * If there was no value then nothing needs to be done.
     * @param vistaLab the VistaLabs enum
     * @param patient the current patient
     * @param variableEntry the {@link VariableEntry} object to put the retrieved value into
     * @param key the variable's fully qualified key (i.e. "bmi$numerical" for bmi)
     */
    protected static void addLabValue(final VistaLabs vistaLab, final Patient patient, final VariableEntry variableEntry,
            final String key)
    {
        final RetrievedValue labValue = patient.getLabs().get(vistaLab);
        if(labValue != null)
        {
            variableEntry.getDynamicValues().put(key, String.valueOf(labValue.getValue()));
            final String retrievalString = VariableEntry.makeRetrievalString(
                    labValue.getValue(),
                    labValue.getMeasureDate(),
                    labValue.getUnits());
            variableEntry.setMeasureDate(key, retrievalString);
        }
    }
}
