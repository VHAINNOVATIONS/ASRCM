package gov.va.med.srcalc.db;

import gov.va.med.srcalc.domain.model.Specialty;
import gov.va.med.srcalc.domain.model.Variable;

import java.util.List;

import javax.inject.Inject;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.google.common.collect.ImmutableList;

/**
 * Data Access Object (DAO) for {@link Specialty} objects.
 */
@Repository
public class SpecialtyDao
{
    private static final Logger LOGGER = LoggerFactory.getLogger(SpecialtyDao.class);
    
    private final SessionFactory fSessionFactory;
    
    /**
     * Constructs an instance.
     * @param sessionFactory the SessionFactory used to get the current session.
     */
    @Inject // Allow arguments to be autowired.
    public SpecialtyDao(final SessionFactory sessionFactory)
    {
        fSessionFactory = sessionFactory;
    }
    
    /**
     * @see org.hibernate.SessionFactory#getCurrentSession()
     */
    protected Session getCurrentSession()
    {
        return fSessionFactory.getCurrentSession();
    }
    
    /**
     * Returns the Specialty from the Database with the given name. The Specialty will
     * have all of the associated RiskModels loaded from the DB.
     * @return the Specialty object or null if there was no specialty with the
     * given name
     */
    public Specialty getByName(final String name)
    {
        final Query q = getCurrentSession().createQuery(
                "from Specialty s left join fetch s.riskModels where s.name = :name");
        q.setString("name", name);
        final Specialty s = (Specialty)q.uniqueResult();
        // Kludge until I figure out how to get Hibernate to automatically load
        // the procedures for a ProcedureVariable.
        ProcedureLoaderVisitor visitor =
                new ProcedureLoaderVisitor(new ProcedureDao(fSessionFactory));
        for (Variable var : s.getModelVariables())
        {
            try
            {
                var.accept(visitor);
            }
            catch (Exception e)
            {
                // ProcedureLoaderVisitor should never throw a checked exception.
                throw new RuntimeException(
                        "ProcedureLoaderVisitor threw an Exception!", e);
            }
        }
        return s;
    }

    /**
     * Returns all Specialties in the database.
     * @return a List ordered by Specialty name
     */
    @SuppressWarnings("unchecked") // trust Hibernate
    public List<Specialty> getAllSpecialties()
    {
        return getCurrentSession().createQuery("from Specialty s order by s.name").list();
    }
    
    /**
     * Returns all Specialties with the RiskModels fully loaded (as opposed to lazy
     * proxies).
     * @return a List ordered by Specialty name
     */
    public ImmutableList<Specialty> getAllSpecialtiesWithRiskModels()
    {
        // We could try to do a join fetch here, but RiskModels require so much joining
        // (and there are so few specialties) that we just do N+1 selects.
        final List<Specialty> specialties = getAllSpecialties();
        LOGGER.debug("Specialties loaded, now loading associated RiskModels...");
        for (final Specialty s : specialties)
        {
            // Yes, this is coupling to Hibernate outside the db package, but this keeps
            // it straightforward and simple.
            Hibernate.initialize(s.getRiskModels());
        }
        return ImmutableList.copyOf(specialties);
    }
}
