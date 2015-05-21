package gov.va.med.srcalc.service;

import java.util.List;

import gov.va.med.srcalc.db.RiskModelDao;
import gov.va.med.srcalc.db.VariableDao;
import gov.va.med.srcalc.domain.model.*;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ImmutableCollection;

public class DefaultAdminService implements AdminService
{
    private static final Logger fLogger = LoggerFactory.getLogger(DefaultAdminService.class);
    
    private final VariableDao fVariableDao;
    private final RiskModelDao fRiskModelDao;
    
    @Inject
    public DefaultAdminService(
            final VariableDao variableDao, final RiskModelDao riskModelDao)
    {
        fVariableDao = variableDao;
        fRiskModelDao = riskModelDao;
    }
    
    @Override
    @Transactional
    public List<AbstractVariable> getAllVariables()
    {
        fLogger.debug("Getting all Variables.");

        return fVariableDao.getAllVariables();
    }
    
    @Override
    @Transactional(readOnly = true)
    public ImmutableCollection<VariableGroup> getAllVariableGroups()
    {
        fLogger.debug("Getting all VariableGroups.");
        
        return fVariableDao.getAllVariableGroups();
    }
    
    @Override
    @Transactional
    public AbstractVariable getVariable(final String keyName)
        throws InvalidIdentifierException
    {
        fLogger.debug("Getting Variable {}.", keyName);
        
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
    public void saveVariable(final AbstractVariable variable)
    {
        fLogger.debug("Saving {}.", variable);
        fVariableDao.mergeVariable(variable);
        // This is a significant (and infrequent) transaction: log it at INFO
        // level.
        fLogger.info("Saved variable {}.", variable.getKey());
    }
    
    @Override
    @Transactional(readOnly = true)
    public ImmutableCollection<RiskModel> getAllRiskModels()
    {
        fLogger.debug("Getting all RiskModels.");
        return fRiskModelDao.getAllRiskModels();
    }
}
