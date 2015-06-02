package gov.va.med.srcalc.web.controller.admin;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.Matchers.hasProperty;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import gov.va.med.srcalc.domain.model.*;
import gov.va.med.srcalc.service.AdminService;
import gov.va.med.srcalc.test.util.IntegrationTest;
import gov.va.med.srcalc.test.util.TestHelpers;
import gov.va.med.srcalc.web.controller.AdminHomeController;
import gov.va.med.srcalc.web.view.Views;
import gov.va.med.srcalc.web.view.admin.EditExistingBooleanVar;
import gov.va.med.srcalc.web.view.admin.EditExistingMultiSelectVar;

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

import com.google.common.collect.ImmutableList;

/**
 * Integration Test for {@link EditVariableController}. Only tests some basic
 * happy-path and error-path cases: unit tests should cover the details.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration  // need to tell Spring to instantiate a WebApplicationContext.
@ContextConfiguration({"/srcalc-context.xml", "/srcalc-controller.xml", "/test-context.xml"})
@Transactional // run each test in its own (rolled-back) transaction
public class EditVariableControllerIT extends IntegrationTest
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
    public void testEditVariable() throws Exception
    {
        fMockMvc.perform(get("/admin/variables/preopPneumonia")).
            andExpect(status().isOk()).
            andExpect(model().attribute("variable", isA(EditExistingBooleanVar.class)));
        
        fMockMvc.perform(
                post("/admin/variables/preopPneumonia").
                param("displayName", "Preop Something")).
            andExpect(redirectedUrl(AdminHomeController.BASE_URL));
    }
    
    @Test
    public void testEditMultiSelectVariable() throws Exception
    {
        final String option1Text = "My First Option";
        final String option2Text = "My Second Option";

        fMockMvc.perform(get("/admin/variables/gender")).
            andExpect(status().isOk()).
            andExpect(model().attribute("variable", isA(EditExistingMultiSelectVar.class)));
        
        fMockMvc.perform(
                post("/admin/variables/gender").
                param("options[0]", option1Text).
                param("options[1]", option2Text)).
            andExpect(redirectedUrl(AdminHomeController.BASE_URL));
        
        // Perform some basic validation that the edit was saved.
        final MultiSelectVariable var =
                (MultiSelectVariable)fAdminService.getVariable("gender");
        final ImmutableList<MultiSelectOption> expectedOptions = ImmutableList.of(
                new MultiSelectOption(option1Text),
                new MultiSelectOption(option2Text));
        assertEquals(expectedOptions, var.getOptions());
    }
    
    @Test
    public void testEditVariableTooLong() throws Exception
    {
        fMockMvc.perform(get("/admin/variables/preopPneumonia")).
            andExpect(status().isOk()).
            andExpect(model().attribute("variable", hasProperty("displayName")));
        
        fMockMvc.perform(
                post("/admin/variables/preopPneumonia").
                param("displayName",
                        TestHelpers.stringOfLength(Variable.DISPLAY_NAME_MAX + 1))).
            andExpect(model().attributeHasErrors("variable")).
            andExpect(view().name(Views.EDIT_BOOLEAN_VARIABLE));
    }
    
    @Test
    public void testEditMultiSelectOptionTooLong() throws Exception
    {
        fMockMvc.perform(
                post("/admin/variables/gender").
                param("options[0]", "My First Option").
                param("options[1]", TestHelpers.stringOfLength(MultiSelectOption.VALUE_MAX + 1))).
            andExpect(view().name(Views.EDIT_MULTI_SELECT_VARIABLE)).
            andExpect(model().errorCount(1)).
            andExpect(model().attributeHasFieldErrors("variable", "options[1]"));
    }
}
