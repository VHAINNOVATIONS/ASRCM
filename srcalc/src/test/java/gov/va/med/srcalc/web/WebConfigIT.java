package gov.va.med.srcalc.web;

import javax.naming.NamingException;

import org.hsqldb.jdbc.JDBCDataSource;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.*;

/**
 * <p>Tests the WebApplicationContext.</p>
 * 
 * <p>Note that I'd love to test view rendering here but it requires JSP
 * rendering which is only available in the real Servlet container.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(
        "file:src/main/webapp/WEB-INF/applicationContext.xml")
public class WebConfigIT
{
    @Autowired
    WebApplicationContext fWac;

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
        // If this method executes, there were no Exceptions while loading the
        // Application Context. Also perform some basic inspection of the
        // WebApplicationContext to make sure it loaded properly.
        assertNotNull(
                "WebApplicationContext should have a ServletContext",
                fWac.getServletContext());
    }
}
