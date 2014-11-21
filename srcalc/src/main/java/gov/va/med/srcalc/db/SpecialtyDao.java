package gov.va.med.srcalc.db;

import java.util.List;

import javax.inject.Inject;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import gov.va.med.srcalc.domain.Specialty;
import gov.va.med.srcalc.domain.variable.Variable;

@Repository
public class SpecialtyDao
{
    private final SessionFactory fSessionFactory;
    
    @Inject // Allow arguments to be autowired.
    public SpecialtyDao(final SessionFactory sessionFactory)
    {
        fSessionFactory = sessionFactory;
    }
    
    protected Session getCurrentSession()
    {
        return fSessionFactory.getCurrentSession();
    }
    
    /**
     * Returns the Specialty from the Database with the given name. The Specialty
     * will have all of the associated Variables loaded from the DB.
     * @return the Specialty object or null if there was no specialty with the
     * given name
     */
    public Specialty getByName(final String name)
    {
        final Query q = getCurrentSession().createQuery(
                "from Specialty s left join fetch s.variables where s.name = :name");
        q.setString("name", name);
        final Specialty s = (Specialty)q.uniqueResult();
        // Kludge until I figure out how to get Hibernate to automatically load
        // the procedures for a ProcedureVariable.
        ProcedureLoaderVisitor visitor = new ProcedureLoaderVisitor(getCurrentSession());
        for (Variable var : s.getVariables())
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

    @SuppressWarnings("unchecked") // trust Hibernate
    public List<Specialty> getAllSpecialties()
    {
        return getCurrentSession().createQuery("from Specialty s order by s.name").list();
    }
}
