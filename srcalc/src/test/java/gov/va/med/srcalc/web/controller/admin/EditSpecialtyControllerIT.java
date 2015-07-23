package gov.va.med.srcalc.web.controller.admin;

import static org.hamcrest.Matchers.isA;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import gov.va.med.srcalc.domain.model.Specialty;
import gov.va.med.srcalc.service.AdminService;
import gov.va.med.srcalc.test.util.IntegrationTest;
import gov.va.med.srcalc.web.view.admin.EditSpecialty;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

/**
 * Integration Test for {@link EditSpecialtyController}. Only tests some basic
 * happy-path and error-path cases: unit tests should cover the details.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration  // need to tell Spring to instantiate a WebApplicationContext.
@ContextConfiguration({"/srcalc-context.xml", "/srcalc-controller.xml", "/test-context.xml"})
@Transactional // run each test in its own (rolled-back) transaction
public class EditSpecialtyControllerIT extends IntegrationTest
{
    @Autowired
    WebApplicationContext fWac;
    
    @Autowired
    AdminService fAdminService;

    private MockMvc fMockMvc;

    @Before
    public void setup()
    {
        fMockMvc = MockMvcBuilders.webAppContextSetup(fWac).build();
    }
    
    @Test
    public void testEditSpecialty() throws Exception
    {
        /* Setup */
        final String newName = "Bogus Specialty Name";
        final Specialty initialSpec = fAdminService.getSpecialtyForId(2);

        /* Verification */
        
        fMockMvc.perform(get(EditSpecialtyController.BASE_URL, "2"))
            .andExpect(status().isOk())
//            .andExpect(model().attribute(
//                    EditSpecialtyController.ATTRIBUTE_MAX_DISPLAY_NAME, equals(Specialty.SPECIALTY_NAME_MAX) ) )
            .andExpect(model().attribute(
                    EditSpecialtyController.ATTRIBUTE_SPECIALTY, isA( EditSpecialty.class ) ) );
        
        fMockMvc.perform(
                post(EditSpecialtyController.BASE_URL, "2")
                .param("name", newName ) )
            .andExpect( redirectedUrl(AdminHomeController.BASE_URL ) );
        
        // Simulate a new Session.
        getHibernateSession().flush();
        getHibernateSession().clear();
        
        final Specialty savedSpec = fAdminService.getSpecialtyForId( 2 );
        assertEquals( newName, savedSpec.getName() );
        
//        // Expected terms
//        final ImmutableSet<ModelTerm> expectedTerms = ImmutableSet.of(
//                new ConstantTerm(-1.23f),
//                new DiscreteTerm(asaClassification, 3, 0.5f),
//                new DerivedTerm(1.5f, fAdminService.getRule(ruleName)));
//        assertEquals(expectedTerms, savedModel.getTerms());
    }
        
//    @Test
//    public void testEditModelNameTooLong() throws Exception
//    {
//        fMockMvc.perform(get(EditSpecialtyController.BASE_URL, "1")).
//            andExpect(status().isOk()).
//            andExpect(model().attribute("riskModel", hasProperty("modelName")));
//        
//        fMockMvc.perform(
//                post(EditSpecialtyController.BASE_URL, "1").
//                param("modelName",
//                        TestHelpers.stringOfLength(DisplayNameConditions.DISPLAY_NAME_MAX + 1))).
//            andExpect(model().attributeHasErrors("Specialty")).
//            andExpect(view().name(Views.EDIT_RISK_MODEL));
//    }        
}
