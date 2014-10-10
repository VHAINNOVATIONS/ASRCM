package gov.va.med.srcalc.db;

import java.util.List;

import javax.inject.Inject;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import gov.va.med.srcalc.domain.Specialty;

@Repository
public class SpecialtyDao
{
    private final SessionFactory fSessionFactory;
    
    @Inject // Allow arguments to be autowired.
    public SpecialtyDao(SessionFactory sessionFactory)
    {
        fSessionFactory = sessionFactory;
    }
    
    protected Session getCurrentSession()
    {
        return fSessionFactory.getCurrentSession();
    }

    @SuppressWarnings("unchecked") // trust Hibernate
    public List<Specialty> getAllSpecialties()
    {
        return getCurrentSession().createQuery("from Specialty s order by s.name").list();
    }
}
