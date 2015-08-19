package gov.va.med.srcalc.util;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * This is a class that is used for data binding to a joda DateTime object. It is necessary
 * because XML data binding does not know how to inherently parse a DateTime.
 */
public class XmlDateAdapter extends XmlAdapter<String, DateTime>
{
    /**
     * The date format to use for parsing and outputting DateTime instances associated with
     * the XML binding.
     */
    public static final DateTimeFormatter REFERENCE_NOTE_DATE_FORMAT = DateTimeFormat.forPattern("MM/dd/yy HH:mm");

    @Override
    public DateTime unmarshal(final String dateString) throws Exception
    {
        return REFERENCE_NOTE_DATE_FORMAT.parseDateTime(dateString);
    }

    @Override
    public String marshal(final DateTime date) throws Exception
    {
        return date.toString();
    }
    
}
