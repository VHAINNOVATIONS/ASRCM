package gov.va.med.srcalc.service;

import org.springframework.dao.DuplicateKeyException;

/**
 * Indicates that the code tried to save a new variable with the same variable
 * key as another variable.
 */
public class DuplicateVariableKeyException extends DuplicateKeyException
{
    /**
     * Change this when changing the class!
     */
    private static final long serialVersionUID = 1L;

    public DuplicateVariableKeyException(final String msg)
    {
        super(msg);
    }
    
}
