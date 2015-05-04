package gov.va.med.srcalc.service;

import gov.va.med.srcalc.db.ProcedureDao;
import gov.va.med.srcalc.domain.model.Procedure;

import java.util.List;

import javax.inject.Inject;

import org.springframework.transaction.annotation.Transactional;

public class DefaultReferenceDataService implements ReferenceDataService
{
    private final ProcedureDao fProcedureDao;
    
    @Inject
    public DefaultReferenceDataService(final ProcedureDao procedureDao)
    {
        fProcedureDao = procedureDao;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Procedure> getActiveProcedures()
    {
        return fProcedureDao.getActiveProcedures();
    }
    
}
