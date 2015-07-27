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
import gov.va.med.srcalc.util.DisplayNameConditions;
import gov.va.med.srcalc.web.SrcalcUrls;
import gov.va.med.srcalc.web.view.Views;
import gov.va.med.srcalc.web.view.admin.*;

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
        
        fMockMvc.perform(post("/admin/variables/preopPneumonia")
                .param("displayName", "Preop Something"))
            .andExpect(status().isMovedTemporarily())
            .andExpect(redirectedUrl(SrcalcUrls.MODEL_ADMIN_HOME));
    }
    
    @Test
    public void testEditMultiSelectVariable() throws Exception
    {
        final String option1Text = "My First Option";
        final String option2Text = "My Second Option";

        fMockMvc.perform(get("/admin/variables/gender")).
            andExpect(status().isOk()).
            andExpect(model().attribute("variable", isA(EditExistingMultiSelectVar.class)));
        
        fMockMvc.perform(post("/admin/variables/gender")
                .param("options[0]", option1Text)
                .param("options[1]", option2Text))
            .andExpect(status().isMovedTemporarily())
            .andExpect(redirectedUrl(SrcalcUrls.MODEL_ADMIN_HOME));
        
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
                        TestHelpers.stringOfLength(DisplayNameConditions.DISPLAY_NAME_MAX + 1))).
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
    
    @Test
    public void testEditDiscreteNumericalVariable() throws Exception
    {
        final String url = "/admin/variables/bun";
        final String firstCatName = "first category";
        final String secondCatName = "second category";
        final String thirdCatName = "third category";
        
        fMockMvc.perform(get(url)).
            andExpect(status().isOk()).
            andExpect(model().attribute(
                    "variable", isA(EditExistingDiscreteNumericalVar.class)));
        
        fMockMvc.perform(post(url)
                .param("categories[0].value", firstCatName)
                .param("categories[0].upperBound", "10")
                .param("categories[1].value", secondCatName)
                .param("categories[1].upperBound", "50")
                .param("categories[2].value", thirdCatName)
                .param("categories[2].upperBound", "90"))
            .andExpect(status().isMovedTemporarily())
            .andExpect(redirectedUrl(SrcalcUrls.MODEL_ADMIN_HOME));
        
        // Perform some basic validation that the edit was saved.
        final DiscreteNumericalVariable var =
                (DiscreteNumericalVariable)fAdminService.getVariable("bun");
        assertEquals(3, var.getCategories().size());
        assertEquals(firstCatName, var.getCategories().first().getOption().getValue());
        assertEquals(thirdCatName, var.getCategories().last().getOption().getValue());
    }
    
    @Test
    public final void testEditDiscreteValueTooLong() throws Exception
    {
        fMockMvc.perform(post("/admin/variables/bun")
                .param("categories[0].value", "first category")
                .param("categories[0].upperBound", "10")
                .param("categories[1].value",
                        TestHelpers.stringOfLength(MultiSelectOption.VALUE_MAX + 1))
                .param("categories[1].upperBound", "50"))
            .andExpect(view().name(Views.EDIT_DISCRETE_NUMERICAL_VARIABLE))
            .andExpect(model().errorCount(1))
            .andExpect(model().attributeHasFieldErrors("variable", "categories[1].value"));
    }
}
