package gov.va.med.srcalc.service;

import gov.va.med.srcalc.db.VariableDao;
import gov.va.med.srcalc.domain.variable.Variable;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

public class DefaultAdminService implements AdminService
{
    private static final Logger fLogger = LoggerFactory.getLogger(DefaultAdminService.class);
    
    private final VariableDao fVariableDao;
    
    @Inject
    public DefaultAdminService(final VariableDao variableDao)
    {
        fVariableDao = variableDao;
    }
    
    @Override
    @Transactional
    public List<Variable> getAllVariables()
    {
        fLogger.debug("Returning all Variables.");

        return fVariableDao.getAllVariables();
    }
    
}
