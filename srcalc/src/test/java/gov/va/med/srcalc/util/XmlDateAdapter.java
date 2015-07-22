package gov.va.med.srcalc.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class XmlDateAdapter extends XmlAdapter<String, Date>
{

    @Override
    public Date unmarshal(final String dateString) throws Exception
    {
        final SimpleDateFormat format = new SimpleDateFormat("MM/dd/yy");
        return format.parse(dateString);
    }

    @Override
    public String marshal(final Date date) throws Exception
    {
        return date.toString();
    }
    
}
