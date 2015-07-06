package gov.va.med.srcalc.util;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import gov.va.med.srcalc.util.TabularUploadError;
import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;
import org.springframework.validation.DefaultMessageCodesResolver;

import com.google.common.collect.ImmutableList;

public class TabularUploadErrorTest
{
    @Test
    public final void testEqualsObject()
    {
        EqualsVerifier.forClass(TabularUploadError.class).verify();
    }
    
    @Test
    public final void testGlobal()
    {
        final String defaultMessage = "global error";
        final String code = ValidationCodes.NO_VALUE;

        final TabularUploadError error = TabularUploadError.global(
                code, null, defaultMessage);
        
        final DefaultMessageCodesResolver codesResolver =
                new DefaultMessageCodesResolver();
        final String[] expectedCodes = codesResolver.resolveMessageCodes(
                code, TabularUploadError.OBJECT_NAME);
        assertArrayEquals(expectedCodes, error.getCodes());
        
        assertEquals(code, error.getCode());
        assertNull(null, error.getArguments());
        assertEquals(defaultMessage, error.getDefaultMessage());
        
        assertEquals("", error.getLocationPrefix());
        
        // Verify the minimal contract of toString().
        assertThat(error.toString(), stringContainsInOrder(ImmutableList.of(code)));
    }
    
    @Test
    public final void testFieldError()
    {
        final String defaultMessage = "default message";
        final String columnName = "column";
        final String code = ValidationCodes.TOO_LONG;
        final Object[] args = new Object[] { 100 };

        final TabularUploadError error = TabularUploadError.forField(
                1, columnName, String.class, code, args, defaultMessage);
        
        final DefaultMessageCodesResolver codesResolver =
                new DefaultMessageCodesResolver();
        final String[] expectedCodes = codesResolver.resolveMessageCodes(
                code, TabularUploadError.OBJECT_NAME, columnName, String.class);
        assertArrayEquals(expectedCodes, error.getCodes());
        
        assertEquals(code, error.getCode());
        assertArrayEquals(args, error.getArguments());
        assertEquals(defaultMessage, error.getDefaultMessage());
        
        assertEquals("Row 1, column: ", error.getLocationPrefix());
        
        // Verify the minimal contract of toString().
        assertThat(error.toString(), stringContainsInOrder(ImmutableList.of(code)));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public final void testEmptyCodes()
    {
        new TabularUploadError(0, "column", new String[0], null, "default");
    }
    
}
