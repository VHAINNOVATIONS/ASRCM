package gov.va.med.srcalc.web.controller.admin;

import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import gov.va.med.srcalc.domain.model.RiskModel;
import gov.va.med.srcalc.service.AdminService;
import gov.va.med.srcalc.test.util.IntegrationTest;
import gov.va.med.srcalc.test.util.TestHelpers;
import gov.va.med.srcalc.util.DisplayNameConditions;
import gov.va.med.srcalc.web.view.Views;
import gov.va.med.srcalc.web.view.admin.EditRiskModel;

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
 * Integration Test for {@link EditRiskModelController}. Only tests some basic
 * happy-path and error-path cases: unit tests should cover the details.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration  // need to tell Spring to instantiate a WebApplicationContext.
@ContextConfiguration({"/srcalc-context.xml", "/srcalc-controller.xml", "/test-context.xml"})
@Transactional // run each test in its own (rolled-back) transaction
public class EditRiskModelControllerIT extends IntegrationTest
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
    public void testEditModel() throws Exception
    {
        final String newName = "Bogus Model Name";
        fMockMvc.perform(get("/admin/models/2")).
            andExpect(status().isOk()).
            andExpect(model().attribute("riskModel", isA(EditRiskModel.class)));
        
        fMockMvc.perform(
                post("/admin/models/2").
                param("modelName", newName )).
            andExpect(redirectedUrl(AdminHomeController.BASE_URL));
        
        final RiskModel rm2 = fAdminService.getRiskModelForId( 2 );
        assertEquals( newName, rm2.getDisplayName() );
    }
        
    @Test
    public void testEditModelNameTooLong() throws Exception
    {
        fMockMvc.perform(get("/admin/models/1")).
            andExpect(status().isOk()).
            andExpect(model().attribute("riskModel", hasProperty("modelName")));
        
        fMockMvc.perform(
                post("/admin/models/1").
                param("modelName",
                        TestHelpers.stringOfLength(DisplayNameConditions.DISPLAY_NAME_MAX + 1))).
            andExpect(model().attributeHasErrors("riskModel")).
            andExpect(view().name(Views.EDIT_RISK_MODEL));
    }

    @Test
    public void testEditModelNameTooShort() throws Exception
    {
        fMockMvc.perform(get("/admin/models/1")).
            andExpect(status().isOk()).
            andExpect(model().attribute("riskModel", hasProperty("modelName")));
        
        fMockMvc.perform(
                post("/admin/models/1").
                param("modelName", "" ) ).
            andExpect(model().attributeHasErrors("riskModel")).
            andExpect(view().name(Views.EDIT_RISK_MODEL));
    }
        
}
