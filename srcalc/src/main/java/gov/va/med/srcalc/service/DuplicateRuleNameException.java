package gov.va.med.srcalc.service;

import org.springframework.dao.DuplicateKeyException;

/**
 * Indicates that the code tried to save a new rule with the same rule
 * display name as another rule.
 */
public class DuplicateRuleNameException extends DuplicateKeyException
{
    /**
     * Change this when changing the class!
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * @see DuplicateKeyException#DuplicateKeyException(String)
     * @param msg 
     */
    public DuplicateRuleNameException(final String msg)
    {
        super(msg);
    }
    
}
