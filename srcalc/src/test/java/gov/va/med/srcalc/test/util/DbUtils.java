package gov.va.med.srcalc.test.util;

import java.sql.*;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

/**
 * Utility methods for interacting with the database in tests.
 */
public class DbUtils
{
    /**
     * No construction!
     */
    private DbUtils()
    {
    }
    
    /**
     * Directly executes a single SQL statement on the given Hibernate Session.
     * Transaction-ignorant.
     * @param session the Hibernate Session to use
     * @param sql the single SQL statement
     */
    public static void executeStatement(final Session session, final String sql)
    {
        session.doWork(new Work()
        {
            @Override
            public void execute(Connection c) throws SQLException
            {
                final Statement s = c.createStatement();
                s.execute(sql);
            }
        });
        
    }
}
