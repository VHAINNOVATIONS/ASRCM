package gov.va.med.srcalc.web.controller.admin;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import gov.va.med.srcalc.domain.model.*;
import gov.va.med.srcalc.service.AdminService;
import gov.va.med.srcalc.test.util.IntegrationTest;
import gov.va.med.srcalc.test.util.TestHelpers;
import gov.va.med.srcalc.util.DisplayNameConditions;
import gov.va.med.srcalc.web.SrcalcUrls;
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

import com.google.common.collect.ImmutableSet;
import com.google.common.io.ByteSource;
import com.google.common.io.Resources;

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
        /* Setup */
        final String newName = "Bogus Model Name";
        final String asaKey = "asaClassification";
        final MultiSelectVariable asaClassification =
                (MultiSelectVariable)fAdminService.getVariable(asaKey);
        final String ruleName = "Age multiplier for functional status";
        final RiskModel initialModel = fAdminService.getRiskModelForId(2);

        /* Verification */
        
        fMockMvc.perform(get(EditRiskModelController.BASE_URL, "2"))
            .andExpect(status().isOk())
            .andExpect(model().attribute(
                    EditRiskModelController.ATTRIBUTE_RISK_MODEL, isA(EditRiskModel.class)))
            .andExpect(model().attribute(
                    EditRiskModelController.ATTRIBUTE_SUMMARIES,
                    hasSize(initialModel.getTerms().size())));
        
        fMockMvc.perform(
                post(EditRiskModelController.BASE_URL, "2")
                .param("modelName", newName )
                .param("terms[0].termType", "CONSTANT")
                .param("terms[0].coefficient", "-1.23")
                .param("terms[1].termType", "VARIABLE")
                .param("terms[1].key", asaKey)
                .param("terms[1].optionValue", "Class 5")
                .param("terms[1].coefficient", "0.5")
                .param("terms[2].termType", "RULE")
                .param("terms[2].key", ruleName)
                .param("terms[2].coefficient", "1.5"))
            .andExpect(status().isFound())
            .andExpect(redirectedUrl(SrcalcUrls.MODEL_ADMIN_HOME));
        
        // Simulate a new Session.
        getHibernateSession().flush();
        getHibernateSession().clear();
        
        final RiskModel savedModel = fAdminService.getRiskModelForId( 2 );
        assertEquals( newName, savedModel.getDisplayName() );
        
        // Expected terms
        final ImmutableSet<ModelTerm> expectedTerms = ImmutableSet.of(
                new ConstantTerm(-1.23f),
                new DiscreteTerm(asaClassification, 3, 0.5f),
                new DerivedTerm(1.5f, fAdminService.getRule(ruleName)));
        assertEquals(expectedTerms, savedModel.getTerms());
    }
        
    @Test
    public void testEditModelNameTooLong() throws Exception
    {
        fMockMvc.perform(get(EditRiskModelController.BASE_URL, "1")).
            andExpect(status().isOk()).
            andExpect(model().attribute("riskModel", hasProperty("modelName")));
        
        fMockMvc.perform(
                post(EditRiskModelController.BASE_URL, "1").
                param("modelName",
                        TestHelpers.stringOfLength(DisplayNameConditions.DISPLAY_NAME_MAX + 1))).
            andExpect(model().attributeHasErrors("riskModel")).
            andExpect(view().name(Views.EDIT_RISK_MODEL));
    }

    @Test
    public void testEditModelNameTooShort() throws Exception
    {
        fMockMvc.perform(get(EditRiskModelController.BASE_URL, "1")).
            andExpect(status().isOk()).
            andExpect(model().attribute("riskModel", hasProperty("modelName")));
        
        fMockMvc.perform(
                post(EditRiskModelController.BASE_URL, "1").
                param("modelName", "" ) ).
            andExpect(model().attributeHasErrors("riskModel")).
            andExpect(view().name(Views.EDIT_RISK_MODEL));
    }
    
    @Test
    public void testImportValidModelTerms() throws Exception
    {
        final ByteSource sampleUpload =
                Resources.asByteSource(TermRowTranslatorTest.VALID_TERMS_RESOURCE);
        final int expectedTermsSize = TermRowTranslatorTest.VALID_TERMS.size();

        fMockMvc.perform(fileUpload(EditRiskModelController.BASE_URL, "1")
                .file("termsFile", sampleUpload.read())
                .param("uploadTerms", "uploadTerms"))
            .andExpect(status().isOk())
            .andExpect(model().attribute(
                    EditRiskModelController.ATTRIBUTE_RISK_MODEL,
                    hasProperty("terms", hasSize(expectedTermsSize))));
    }
        
}
