package gov.va.med.srcalc.web;

import javax.naming.NamingException;

import org.hsqldb.jdbc.JDBCDataSource;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Tests the WebApplicationContext.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(
        {"/srcalc-context.xml", "/test-context.xml", "file:src/main/webapp/WEB-INF/applicationContext.xml"})
public class WebConfigIT
{
    @BeforeClass
    public static void setupJndi() throws NamingException
    {
        final SimpleNamingContextBuilder builder =
                SimpleNamingContextBuilder.emptyActivatedContextBuilder();
        final JDBCDataSource dataSource = new JDBCDataSource();
        dataSource.setUrl("jdbc:hsqldb:mem:srcalctest");
        builder.bind("java:comp/env/jdbc/srcalcDB", dataSource);
    }

    /**
     * Simply forces loading of the application context.
     */
    @Test
    public void testWebConfig()
    {
        // If this method executes, it loaded properly.
    }
}
