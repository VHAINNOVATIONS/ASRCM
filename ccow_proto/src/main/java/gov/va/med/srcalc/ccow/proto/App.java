package gov.va.med.srcalc.ccow.proto;

import gov.va.med.srcalc.ccow.CmrProxy;
import gov.va.med.srcalc.ccow.ComponentLocation;

public class App
{
    public static void main(String[] args)
    {
        final ComponentLocation cmLocation = CmrProxy.queryLocator();
        System.out.println("CM Location: " + cmLocation);
    }
    
}
