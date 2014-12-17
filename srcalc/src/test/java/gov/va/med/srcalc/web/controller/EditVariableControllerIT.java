package gov.va.med.srcalc.web.controller;

import gov.va.med.srcalc.test.util.TestNameLogger;
import gov.va.med.srcalc.web.view.Tile;

import org.junit.*;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration  // need to tell Spring to instantiate a WebApplicationContext.
@ContextConfiguration({"/srcalc-context.xml", "/srcalc-controller.xml", "/test-context.xml"})
public class EditVariableControllerIT
{
    private static final Logger fLogger = LoggerFactory.getLogger(EditVariableControllerIT.class);
    
    @Autowired
    WebApplicationContext fWac;

    private MockMvc fMockMvc;
    
    @Rule
    public final TestRule fTestLogger = new TestNameLogger(fLogger);

    @Before
    public void setup()
    {
        fMockMvc = MockMvcBuilders.webAppContextSetup(fWac).build();
    }
    
    @Test
    @Transactional
    public void testEditVariable() throws Exception
    {
        fMockMvc.perform(get("/admin/models/editVariable/Preop Pneumonia")).
            andExpect(status().isOk()).
            andExpect(model().attribute("variable", hasProperty("displayName")));
        
        fMockMvc.perform(
                post("/admin/models/editVariable/Preop Pneumonia").
                param("displayName", "Preop Something")).
            andExpect(redirectedUrl("/admin/models"));
    }
    
    @Test
    @Transactional
    public void testEditVariableTooLong() throws Exception
    {
        fMockMvc.perform(get("/admin/models/editVariable/Preop Pneumonia")).
            andExpect(status().isOk()).
            andExpect(model().attribute("variable", hasProperty("displayName")));
        
        fMockMvc.perform(
                post("/admin/models/editVariable/Preop Pneumonia").
                param("displayName",
                        // 81 characters
                        "01234567890123456789012345678901234567890123456789012345678901234567890123456789X")).
            andExpect(model().attributeHasErrors("variable")).
            andExpect(view().name(Tile.EDIT_VARIABLE));
    }

    @Test
    @Transactional
    public void testEditVariableInvalidChars() throws Exception
    {
        fMockMvc.perform(get("/admin/models/editVariable/Preop Pneumonia")).
            andExpect(status().isOk()).
            andExpect(model().attribute("variable", hasProperty("displayName")));
        
        fMockMvc.perform(
                post("/admin/models/editVariable/Preop Pneumonia").
                param("displayName",
                        "Preop_Pneumonia")).
            andExpect(model().attributeHasErrors("variable")).
            andExpect(view().name(Tile.EDIT_VARIABLE));
    }
    
}
