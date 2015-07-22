package gov.va.med.srcalc.vista;

import static org.junit.Assert.assertEquals;
import gov.va.med.srcalc.vista.AdlNotes.AdlNote;

import java.io.StringReader;
import java.text.SimpleDateFormat;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;

/**
 * Tests the {@link AdlNotes} class by testing if an XML String can be parsed 
 * into an instance of {@link AdlNotes}.
 */
public class AdlNotesTest
{
    private static final String VALID_NOTES = "<notes><note localTitle=\"Note Title\" signDate=\"07/14/15\">" +
        "<body>Body line 1\nBody line 2\n</body>" +
        "</note></notes>";
    private static final String INVALID_DATE = "<notes><note localTitle=\"Note Title\" signDate=\"badDate\">" +
            "<body>Body line 1\nBody line 2\n</body>" +
            "</note></notes>";
    private static final String MISSING_ATTR = "<notes><note signDate=\"badDate\">" +
            "<body>Body line 1\nBody line 2\n</body>" +
            "</note></notes>";
    
    private static Unmarshaller fUnmarshaller;
    
    @Before
    public void setup() throws Exception
    {
        final JAXBContext context = JAXBContext.newInstance(AdlNotes.class);
        fUnmarshaller = context.createUnmarshaller();
    }
    
    @Test
    public void testXmlParsingValidString() throws Exception
    {
        
        final InputSource input = new InputSource();
        input.setCharacterStream(new StringReader(VALID_NOTES));
        
        final AdlNotes allNotes = (AdlNotes) fUnmarshaller.unmarshal(input);
        final AdlNote parsedNote = allNotes.getAllNotes().get(0);
        assertEquals("Note Title", parsedNote.getLocalTitle());
        assertEquals("Body line 1\nBody line 2\n", parsedNote.getNoteBody());
        final SimpleDateFormat format = new SimpleDateFormat("MM/dd/yy");
        assertEquals(format.parse("07/14/15"), parsedNote.getSignDate());
    }
    
    @Test
    public void testXmlParsingInvalidDate() throws Exception
    {
        final InputSource input = new InputSource();
        input.setCharacterStream(new StringReader(INVALID_DATE));
        final AdlNotes allNotes = (AdlNotes) fUnmarshaller.unmarshal(input);
        final AdlNote parsedNote = allNotes.getAllNotes().get(0);
        assertEquals("Note Title", parsedNote.getLocalTitle());
        assertEquals("Body line 1\nBody line 2\n", parsedNote.getNoteBody());
        // SimpleDateFormat returns a null value if the string could not be parsed.
        assertEquals(null, parsedNote.getSignDate());
    }
    
    @Test
    public void testXmlParsingMissingAttribute() throws Exception
    {
        final InputSource input = new InputSource();
        input.setCharacterStream(new StringReader(MISSING_ATTR));
        final AdlNotes allNotes = (AdlNotes) fUnmarshaller.unmarshal(input);
        final AdlNote parsedNote = allNotes.getAllNotes().get(0);
        // Missing attributes remain null because they were never initialized
        assertEquals(null, parsedNote.getLocalTitle());
        assertEquals("Body line 1\nBody line 2\n", parsedNote.getNoteBody());
        // SimpleDateFormat returns a null value if the string could not be parsed.
        assertEquals(null, parsedNote.getSignDate());
    }
}
