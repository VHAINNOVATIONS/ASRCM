package gov.va.med.srcalc.service;

import gov.va.med.srcalc.db.VariableDao;
import gov.va.med.srcalc.domain.model.AbstractVariable;
import gov.va.med.srcalc.domain.model.VariableGroup;

import java.util.List;
import java.util.SortedSet;

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
    public List<AbstractVariable> getAllVariables()
    {
        fLogger.debug("Returning all Variables.");

        return fVariableDao.getAllVariables();
    }
    
    @Override
    @Transactional
    public SortedSet<VariableGroup> getAllVariableGroups()
    {
        return fVariableDao.getAllVariableGroups();
    }
    
    @Override
    @Transactional
    public AbstractVariable getVariable(final String keyName)
        throws InvalidIdentifierException
    {
        fLogger.debug("Loading Variable {}", keyName);
        
        final AbstractVariable var = fVariableDao.getByKey(keyName);
        if (var == null)
        {
            throw new InvalidIdentifierException(
                    "There is no Variable called " + keyName);
        }
        return var;
    }
    
    @Override
    @Transactional
    public void updateVariable(final AbstractVariable variable)
    {
        fVariableDao.updateVariable(variable);
    }
}
