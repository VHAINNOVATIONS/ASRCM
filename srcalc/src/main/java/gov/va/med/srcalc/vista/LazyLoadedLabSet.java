package gov.va.med.srcalc.vista;

import gov.va.med.srcalc.util.RetrievedValue;

import java.util.HashMap;
import java.util.Map;

public class LazyLoadedLabSet
{
    private Map<String, RetrievedValue> fLabs;
    
    public void setLabs(final Map<String, RetrievedValue> labs)
    {
        fLabs = labs;
    }
    
    public Map<String, RetrievedValue> getLabs(final VistaPatientDao patientDao)
    {
        if (fLabs == null)
        {
            // TODO: Get the list of labs from Vista
            fLabs = new HashMap<String, RetrievedValue>();
        }
        return fLabs;
    }
}
