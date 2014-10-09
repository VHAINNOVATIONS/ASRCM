package gov.va.med.srcalc.service;

import java.util.Set;

import gov.va.med.srcalc.db.SpecialtyDao;
import gov.va.med.srcalc.domain.Specialty;

public class SpecialtyService
{
    private final SpecialtyDao fSpecialtyDao;
    
    public SpecialtyService(SpecialtyDao specialtyDao)
    {
        fSpecialtyDao = specialtyDao;
    }
    
    public Set<Specialty> getAllSpecialties()
    {
        return fSpecialtyDao.getAllSpecialties();
    }
}
