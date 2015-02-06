package gov.va.med.srcalc.ccow;

/**
 * Represents an Exception defined by the CMA standard.
 */
public class CmaException extends Exception
{
    /**
     * TODO: change this when changing the class!
     */
    private static final long serialVersionUID = 1L;
    
    private final String fExceptionName;
    
    public CmaException(final String exceptionName, final String exceptionMessage)
    {
        super(exceptionMessage);
        
        fExceptionName = exceptionName;
    }

    /**
     * Returns the actual exception name of the CMA Exception since we don't
     * have a subclass for every exception type.
     */
    public String getExceptionName()
    {
        return fExceptionName;
    }
    
    @Override
    public String toString()
    {
        // Override to include the actual exception name instead of the class
        // name.
        return getExceptionName() + ": " + getLocalizedMessage();
    }
}
