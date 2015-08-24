package gov.va.med.srcalc.web.controller.admin;

import static org.junit.Assert.*;
import gov.va.med.srcalc.domain.model.Procedure;
import gov.va.med.srcalc.util.*;
import gov.va.med.srcalc.util.csv.CsvReader;
import gov.va.med.srcalc.util.csv.TabularParseResult;
import gov.va.med.srcalc.util.csv.TabularUploadError;

import java.io.IOException;
import java.net.URL;

import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.collect.*;
import com.google.common.io.CharSource;
import com.google.common.io.Resources;

/**
 * Tests {@link ProcedureRowTranslator} and, consequentially, {@link CsvReader}.
 */
public class ProcedureRowTranslatorTest
{
    /**
     * Points to a CSV file on the classpath which represents a valid procedures upload.
     */
    public static final URL VALID_PROCEDURES_RESOURCE = Resources.getResource(
            "admin_resources/sample_procedures_upload.csv");
    
    /**
     * Points to a totally invalid CSV file. (The file cannot be interpreted as a CSV.)
     */
    public static final URL INVALID_CSV_FILE =
            Resources.getResource(ProcedureRowTranslatorTest.class, "invalid.csv");
    
    /**
     * Points to a CSV file which contains invalid procedure content. (The file
     * <em>can</em> be interpreted as a CSV.)
     */
    public static final URL INVALID_PROCEDURES_RESOURCE = Resources.getResource(
            ProcedureRowTranslatorTest.class, "invalid_procedures_upload.csv");
    
    /**
     * These are the procedures in {@link #VALID_PROCEDURES_RESOURCE}.
     */
    public static final ImmutableList<Procedure> VALID_PROCEDURES = ImmutableList.of(
            new Procedure(
                    "00012", 1.45f, "Short Desc One",
                    "Procedure Twelve Long Description", "NotinSurgCM", false),
            new Procedure(
                    "10002", 7.56f, "Procedure 10002 desc",
                    "Procedure Ten Thousand and Two", "Complex", true));

    @Test
    public final void testReadProceduresValid() throws IOException
    {
        final CharSource validCsv =
                Resources.asCharSource(VALID_PROCEDURES_RESOURCE, Charsets.US_ASCII);
        
        final CsvReader<Procedure> procedureReader =
                new CsvReader<>(new ProcedureRowTranslator());
        final TabularParseResult<Procedure> result =
                procedureReader.readObjects(validCsv.openStream());
        
        /* Verification */
        assertEquals(0, result.getErrors().size());
        assertEquals(VALID_PROCEDURES, result.getRowObjects());
    }
    
    @Test
    public final void testReadCorruptCsv() throws IOException
    {
        final CharSource corruptCsv = Resources.asCharSource(
                INVALID_CSV_FILE, Charsets.US_ASCII);
        
        final CsvReader<Procedure> procedureReader =
                new CsvReader<>(new ProcedureRowTranslator());
        final TabularParseResult<Procedure> result =
                procedureReader.readObjects(corruptCsv.openStream());
        
        /* Verification */
        assertEquals(1, result.getErrors().size());
        final TabularUploadError error = result.getErrors().asList().get(0);
        assertEquals(ValidationCodes.INVALID_CONTENTS, error.getCode());
        assertEquals(ImmutableList.of(), result.getRowObjects());
    }
    
    @Test
    public final void testReadProceduresInvalid() throws IOException
    {
        final CharSource invalidCsv = Resources.asCharSource(
                INVALID_PROCEDURES_RESOURCE, Charsets.US_ASCII);

        // The expected errors for each field. (We use a Set because order doesn't matter.)
        final ImmutableSet<TabularUploadError> expectedErrors = ImmutableSet.of(
                TabularUploadError.forField(
                        2, "cptCode", String.class, ValidationCodes.BAD_FIXED_LENGTH,
                        new Object[] { Procedure.CPT_CODE_LENGTH }, ""),
                TabularUploadError.forField(
                        2, "rvu", float.class, ValidationCodes.TYPE_MISMATCH, null, ""),
                TabularUploadError.forField(
                        2, "eligible", boolean.class, ValidationCodes.TYPE_MISMATCH, null, ""),
                TabularUploadError.forField(
                        2, "complexity", String.class, ValidationCodes.TOO_LONG,
                        new Object[] { Procedure.COMPLEXITY_MAX }, ""),
                TabularUploadError.forField(
                        2, "longDescription", String.class, ValidationCodes.NO_VALUE, null, ""),
                TabularUploadError.forField(
                        2, "shortDescription", String.class, ValidationCodes.NO_VALUE, null, ""),
                TabularUploadError.forField(
                        3, "cptCode", String.class, ValidationCodes.BAD_FIXED_LENGTH,
                        new Object[] { Procedure.CPT_CODE_LENGTH }, ""),
                TabularUploadError.forField(
                        3, "rvu", float.class, ValidationCodes.TYPE_MISMATCH, null, ""),
                TabularUploadError.forField(
                        3, "eligible", boolean.class, ValidationCodes.TYPE_MISMATCH, null, ""),
                TabularUploadError.forField(
                        3, "complexity", String.class, ValidationCodes.NO_VALUE, null, ""),
                TabularUploadError.forField(
                        3, "longDescription", String.class, ValidationCodes.TOO_LONG,
                        new Object[] { Procedure.DESCRIPTION_MAX }, ""),
                TabularUploadError.forField(
                        3, "shortDescription", String.class, ValidationCodes.TOO_LONG,
                        new Object[] { Procedure.DESCRIPTION_MAX }, ""));
                
        /* Behavior */
        final CsvReader<Procedure> procedureReader =
                new CsvReader<>(new ProcedureRowTranslator());
        final TabularParseResult<Procedure> result =
                procedureReader.readObjects(invalidCsv.openStream());
        
        /* Verification */
        assertEquals(expectedErrors, ImmutableSet.copyOf(result.getErrors()));
    }
    
}
