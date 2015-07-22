package gov.va.med.srcalc.service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import gov.va.med.srcalc.db.*;
import gov.va.med.srcalc.domain.model.*;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;

/**
 * The canonical implementation of {@link AdminService}.
 */
public class DefaultAdminService implements AdminService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultAdminService.class);
    
    private final VariableDao fVariableDao;
    private final RiskModelDao fRiskModelDao;
    private final SpecialtyDao fSpecialtyDao;
    private final RuleDao fRuleDao;
    private final ProcedureDao fProcedureDao;
    
    @Inject
    public DefaultAdminService(
            final VariableDao variableDao,
            final RiskModelDao riskModelDao,
            SpecialtyDao specialtyDao,
            final RuleDao ruleDao,
            final ProcedureDao procedureDao)
    {
        fVariableDao = variableDao;
        fRiskModelDao = riskModelDao;
        fSpecialtyDao = specialtyDao;
        fRuleDao = ruleDao;
        fProcedureDao = procedureDao;
    }
    
    @Override
    @Transactional
    public ImmutableList<AbstractVariable> getAllVariables()
    {
        LOGGER.debug("Getting all Variables.");

        return ImmutableList.copyOf(fVariableDao.getAllVariables());
    }
    
    @Override
    @Transactional(readOnly = true)
    public ImmutableCollection<VariableGroup> getAllVariableGroups()
    {
        LOGGER.debug("Getting all VariableGroups.");
        
        return fVariableDao.getAllVariableGroups();
    }
    
    @Override
    @Transactional
    public AbstractVariable getVariable(final String keyName)
        throws InvalidIdentifierException
    {
        LOGGER.debug("Getting Variable {}.", keyName);
        
        final AbstractVariable var = fVariableDao.getByKey(keyName);
        if (var == null)
        {
            throw new InvalidIdentifierException(
                    "There is no Variable called " + keyName);
        }
        return var;
    }
    
    /**
     * Returns true if there is already a different variable with the same key as the
     * given variable, false otherwise.
     * @param variable the variable to check
     */
    private boolean keyAlreadyExists(final AbstractVariable variable)
    {
        final AbstractVariable existingVar = fVariableDao.getByKey(variable.getKey());
        LOGGER.debug("Existing variable with key {}: {}", variable.getKey(), existingVar);
        // If there is an existing var with the same key and it is not actually
        // the same variable, then we have a conflict.
        return (existingVar != null && variable.getId() != existingVar.getId());
    }
    
    @Override
    @Transactional
    public void saveVariable(final AbstractVariable variable)
    {
        LOGGER.debug("Saving {}.", variable);

        // Per method Javadoc, throw a DuplicateVariableKeyException if a
        // different variable with the same variable key already exists.
        // (mergeVariable() below would throw a DataAccessException in this
        // case, but we cannot robustly determine the cause of the Exception so
        // we explicitly check here.)
        if (keyAlreadyExists(variable))
        {
            throw new DuplicateVariableKeyException(
                    "Duplicate variable key " + variable.getKey());
        }

        fVariableDao.mergeVariable(variable);
        // This is a significant (and infrequent) transaction: log it at INFO
        // level.
        LOGGER.info("Saved variable {}.", variable.getKey());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Specialty> getAllSpecialties()
    {
        LOGGER.debug("Getting all Specialties.");
        return fSpecialtyDao.getAllSpecialties();
    }
    
    @Override
    @Transactional(readOnly = true)
    public ImmutableCollection<RiskModel> getAllRiskModels()
    {
        LOGGER.debug("Getting all RiskModels.");
        return fRiskModelDao.getAllRiskModels();
    }
    
    @Override
    @Transactional
    public ImmutableCollection<Rule> getAllRules()
    {
        LOGGER.debug("Getting all Rules.");
        return fRuleDao.getAllRules();
    }
    
    @Override
    @Transactional
    public Rule getRule(final String displayName) throws InvalidIdentifierException
    {
        LOGGER.debug("Getting Rule by name {}.", displayName);
        final Rule rule = fRuleDao.getByDisplayName(displayName);
        if (rule == null)
        {
            throw new InvalidIdentifierException("There is no Rule called " + displayName);
        }
        return rule;
    }
    
    @Override
    @Transactional
    public Rule getRuleById(final int ruleId) throws InvalidIdentifierException
    {
        LOGGER.debug("Getting Rule by ID {}", ruleId);
        final Rule rule = fRuleDao.getById(ruleId);
        if(rule == null)
        {
            throw new InvalidIdentifierException("There is no Rule with ID " + ruleId);
        }
        return rule;
    }
    
    /**
     * Returns true if there is already a different rule with the same display name.
     * @param rule
     */
    private boolean ruleNameAlreadyExists(final Rule rule)
    {
        final Rule existingRule = fRuleDao.getByDisplayName(rule.getDisplayName());
        LOGGER.debug("Existing rule with name {}: {}", rule.getDisplayName(), existingRule);
        // If there is an existing rule with the same name and it is not actually
        // the same rule, then we have a conflict.
        return (existingRule != null && rule.getId() != existingRule.getId());
    }

    @Override
    @Transactional
    public void saveRule(final Rule rule)
    {
        LOGGER.debug("Saving Rule {}.", rule);

        // Per method Javadoc, throw a DuplicateRuleNameException if a
        // different rule with the same display name already exists.
        // (mergeRule() below would throw a DataAccessException in this
        // case, but we cannot robustly determine the cause of the Exception so
        // we explicitly check here.)
        if (ruleNameAlreadyExists(rule))
        {
            throw new DuplicateRuleNameException(
                    "Duplicate rule name " + rule.getDisplayName());
        }

        fRuleDao.mergeRule(rule);
        // This is a significant (and infrequent) transaction: log it at INFO
        // level.
        LOGGER.info("Saved rule {}.", rule.getDisplayName());
    }
    
    @Override
    @Transactional
    public void replaceAllProcedures(final Set<Procedure> newProcedures)
    {
        final Stopwatch stopwatch = Stopwatch.createStarted();
        final int deleteCount = fProcedureDao.replaceAllProcedures(newProcedures);
        stopwatch.stop();
        
        LOGGER.info(
                "Replaced all {} Procedures in the DB with a new set of {} in {}ms.",
                deleteCount,
                newProcedures.size(),
                stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }

    @Override
    @Transactional(readOnly = true)
    public ImmutableList<Procedure> getAllProcedures()
    {
        return fProcedureDao.getAllProcedures();
    }

    @Transactional(readOnly = true)
    public RiskModel getRiskModelForId(final int modelId)
    {        
        LOGGER.debug("Getting RiskModel for {}.", modelId);
        return fRiskModelDao.getRiskModelForId( modelId );
    }

    @Override
    @Transactional
    public void saveRiskModel( final RiskModel model ) 
    {        
        if( getRiskModelForId( model.getId() ) == null ) 
        {  // TODO : remove this when possible to create new RiskModels
            LOGGER.warn( "Warning: Saving RiskModel {}. ID {} doesn't exist in the DB ", 
                    model.getDisplayName(), model.getId() );
        }
        
        final RiskModel persistentModel = fRiskModelDao.saveRiskModel( model );
        LOGGER.info("Saved Risk Model {}.", model.getDisplayName() );
        LOGGER.debug("Persistent state is now: {}", persistentModel);
    }
}
