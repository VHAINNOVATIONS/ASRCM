package gov.va.med.srcalc.service;

import gov.va.med.srcalc.db.ProcedureDao;
import gov.va.med.srcalc.domain.model.Procedure;

import javax.inject.Inject;

import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ImmutableList;

/**
 * Canonical implementation of {@link ReferenceDataService}.
 */
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
    public ImmutableList<Procedure> getAllProcedures()
    {
        return fProcedureDao.getAllProcedures();
    }
    
}
