package gov.va.med.srcalc.util;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class XmlDateAdapter extends XmlAdapter<String, DateTime>
{
    public static final DateTimeFormatter ADL_DATE_FORMAT = DateTimeFormat.forPattern("MM/dd/yy HH:mm");

    @Override
    public DateTime unmarshal(final String dateString) throws Exception
    {
        
        return ADL_DATE_FORMAT.parseDateTime(dateString);
    }

    @Override
    public String marshal(final DateTime date) throws Exception
    {
        return date.toString();
    }
    
}
