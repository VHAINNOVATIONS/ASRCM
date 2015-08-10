package gov.va.med.srcalc.vista;

import static org.junit.Assert.assertEquals;
import gov.va.med.srcalc.domain.ReferenceNote;
import gov.va.med.srcalc.domain.ReferenceNotes;
import gov.va.med.srcalc.util.XmlDateAdapter;

import java.io.StringReader;
import java.util.Collections;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;

/**
 * Tests the {@link ReferenceNotes} class by testing if an XML String can be parsed 
 * into an instance of {@link ReferenceNotes}.
 */
public class ReferenceNotesTest
{
    private static final String VALID_NOTES = "<notes><note localTitle='Note Title' signDate='07/14/15 12:12'>" +
        "<body>Body line 1\nBody line 2\n</body>" +
        "</note></notes>";
    private static final String INVALID_DATE = "<notes><note localTitle='Note Title' signDate='badDate'>" +
            "<body>Body line 1\nBody line 2\n</body>" +
            "</note></notes>";
    private static final String MISSING_ATTR = "<notes><note signDate='badDate'>" +
            "<body>Body line 1\nBody line 2\n</body>" +
            "</note></notes>";
    private static final String CDATA_BLOCK = "<notes>\n<note localTitle='AUDIOLOGY - HEARING LOSS CONSULT' "
            + "signDate='04/01/2004 22:24'>\n<body>\n"
            + "<![CDATA[HX:  Patient was seen for hearing aid fitting and orientation.]]>\n"
            + "<![CDATA[The batteries supplied for this hearing aid were: za312.]]>\n"
            + "</body>\n</note>\n</notes>";
    private static final String NO_NOTES = "<notes></notes>";

    private Unmarshaller fUnmarshaller;
    
    @Before
    public void setup() throws Exception
    {
        final JAXBContext context = JAXBContext.newInstance(ReferenceNotes.class);
        fUnmarshaller = context.createUnmarshaller();
    }
    
    @Test
    public void testXmlParsingValidString() throws Exception
    {
        final InputSource input = new InputSource();
        input.setCharacterStream(new StringReader(VALID_NOTES));
        final ReferenceNotes allNotes = (ReferenceNotes) fUnmarshaller.unmarshal(input);
        final ReferenceNote parsedNote = allNotes.getAllNotes().get(0);
        assertEquals("Note Title", parsedNote.getLocalTitle());
        assertEquals("Body line 1\nBody line 2\n", parsedNote.getNoteBody());
        assertEquals(XmlDateAdapter.REFERENCE_NOTE_DATE_FORMAT.parseDateTime("07/14/15 12:12"), parsedNote.getSignDate());
    }
    
    @Test
    public void testXmlParsingInvalidDate() throws Exception
    {
        final InputSource input = new InputSource();
        input.setCharacterStream(new StringReader(INVALID_DATE));
        final ReferenceNotes allNotes = (ReferenceNotes) fUnmarshaller.unmarshal(input);
        final ReferenceNote parsedNote = allNotes.getAllNotes().get(0);
        assertEquals("Note Title", parsedNote.getLocalTitle());
        assertEquals("Body line 1\nBody line 2\n", parsedNote.getNoteBody());
        assertEquals(null, parsedNote.getSignDate());
    }
    
    @Test
    public void testXmlParsingMissingAttribute() throws Exception
    {
        final InputSource input = new InputSource();
        input.setCharacterStream(new StringReader(MISSING_ATTR));
        final ReferenceNotes allNotes = (ReferenceNotes) fUnmarshaller.unmarshal(input);
        final ReferenceNote parsedNote = allNotes.getAllNotes().get(0);
        // Missing attributes remain null because they were never initialized
        assertEquals(null, parsedNote.getLocalTitle());
        assertEquals("Body line 1\nBody line 2\n", parsedNote.getNoteBody());
        assertEquals(null, parsedNote.getSignDate());
    }
    
    @Test
    public void testCdataParsing() throws Exception
    {
        final ReferenceNotes allNotes = parseAdlNotes(CDATA_BLOCK);
        final ReferenceNote parsedNote = allNotes.getAllNotes().get(0);
        assertEquals("AUDIOLOGY - HEARING LOSS CONSULT", parsedNote.getLocalTitle());
        assertEquals("\nHX:  Patient was seen for hearing aid fitting and orientation.\n"
                + "The batteries supplied for this hearing aid were: za312.\n", parsedNote.getNoteBody());
        assertEquals(XmlDateAdapter.REFERENCE_NOTE_DATE_FORMAT.parseDateTime("04/01/2004 22:24"), parsedNote.getSignDate());
    }
    
    @Test
    public void testNoNotes() throws JAXBException
    {
        final ReferenceNotes allNotes = parseAdlNotes(NO_NOTES);
        assertEquals(Collections.emptyList(), allNotes.getAllNotes());
    }
    
    private ReferenceNotes parseAdlNotes(final String adlString) throws JAXBException
    {
        final InputSource input = new InputSource();
        input.setCharacterStream(new StringReader(adlString));
        return (ReferenceNotes) fUnmarshaller.unmarshal(input);
    }
}
