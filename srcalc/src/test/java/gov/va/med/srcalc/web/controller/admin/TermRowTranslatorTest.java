package gov.va.med.srcalc.web.controller.admin;

import static org.junit.Assert.*;
import gov.va.med.srcalc.domain.model.ModelTerm;
import gov.va.med.srcalc.service.InvalidIdentifierException;
import gov.va.med.srcalc.service.MockModelService;
import gov.va.med.srcalc.util.ValidationCodes;
import gov.va.med.srcalc.util.csv.CsvReader;
import gov.va.med.srcalc.util.csv.TabularParseResult;
import gov.va.med.srcalc.util.csv.TabularUploadError;
import gov.va.med.srcalc.web.view.admin.EditModelTerm;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.CharSource;
import com.google.common.io.Resources;

/**
 * Tests {@link TermRowTranslator}.
 */
public class TermRowTranslatorTest
{
    /**
     * Points to a CSV file on the classpath which represents a valid model terms upload.
     */
    public static final URL VALID_TERMS_RESOURCE = Resources.getResource(
            "admin_resources/sample_terms_upload.csv");
    
    public static final ImmutableList<EditModelTerm> VALID_TERMS = ImmutableList.of(
            EditModelTerm.forConstant(-2.3434f),
            EditModelTerm.forVariable("age", 0.0512f),
            EditModelTerm.forRule("Age multiplier for functional status", 0.0023f),
            EditModelTerm.forVariable("procedure", 0.0139f),
            EditModelTerm.forVariable("gender", 0.1509f).setOptionValue("Male"),
            EditModelTerm.forVariable("gender", 0.1409f).setOptionValue("Female"),
            EditModelTerm.forVariable("dnr", 0.2143f),
            EditModelTerm.forVariable("wbc", 1.0f).setOptionValue(">11.0"));
    
    private static final URL INVALID_TERMS_RESOURCE = Resources.getResource(
            TermRowTranslatorTest.class, "invalid_terms_upload.csv");
    
    private final MockModelService fModelService = new MockModelService();
    
    private ArrayList<ModelTerm> buildTerms(final List<EditModelTerm> editTerms)
                    throws InvalidIdentifierException
    {
        final ArrayList<ModelTerm> terms = new ArrayList<>(editTerms.size());
        for (final EditModelTerm editTerm : editTerms)
        {
            terms.add(editTerm.build(fModelService));
        }
        return terms;
    }
    
    @Test
    public final void testReadTermsValid() throws IOException, InvalidIdentifierException
    {
        final CharSource validCsv =
                Resources.asCharSource(VALID_TERMS_RESOURCE, Charsets.US_ASCII);
        // Build the expected ModelTerms themselves since EditModelTerm doesn't provide
        // a value-based equals() method.
        final List<ModelTerm> expectedTerms = buildTerms(VALID_TERMS);
        
        /* Behavior */
        final CsvReader<EditModelTerm> termReader = new CsvReader<>(
                new TermRowTranslator());
        final TabularParseResult<EditModelTerm> result =
                termReader.readObjects(validCsv.openStream());
        
        /* Verification */
        // Comparing the lists instead of the sizes presents a better failure message.
        assertEquals(ImmutableList.of(), result.getErrors());
        assertEquals(expectedTerms, buildTerms(result.getRowObjects()));
    }
    
    @Test
    public final void testReadTermsInvalid() throws IOException
    {
        final CharSource invalidTermsCsv =
                Resources.asCharSource(INVALID_TERMS_RESOURCE, Charsets.US_ASCII);
        final ImmutableSet<TabularUploadError> expectedErrors = ImmutableSet.of(
                TabularUploadError.forField(
                        2, "termType", EditModelTerm.TermType.class, ValidationCodes.INVALID_OPTION,
                        new Object[] { ImmutableList.copyOf(EditModelTerm.TermType.values()) }, ""),
                TabularUploadError.forField(
                        2, "coefficient", float.class, ValidationCodes.TYPE_MISMATCH, null, ""),
                TabularUploadError.forField(
                        3, "coefficient", float.class, ValidationCodes.TYPE_MISMATCH, null, ""),
                TabularUploadError.forField(
                        4, "coefficient", float.class, ValidationCodes.TYPE_MISMATCH, null, ""));
        
        /* Behavior */
        final CsvReader<EditModelTerm> termReader =
                new CsvReader<>(new TermRowTranslator());
        final TabularParseResult<EditModelTerm> result =
                termReader.readObjects(invalidTermsCsv.openStream());
        
        /* Verification */
        assertEquals(expectedErrors, ImmutableSet.copyOf(result.getErrors()));
    }
    
}
