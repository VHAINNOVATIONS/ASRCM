package gov.va.med.srcalc.test.util;

import java.util.Objects;

import org.springframework.validation.FieldError;

import com.google.common.base.MoreObjects;

/**
 * Encapsulates just a field and primary error code. A very simple form of Spring's
 * {@link FieldError} useful for test verifications.
 */
public class SimpleFieldError
{
    private final String fField;
    
    private final String fPrimaryCode;
    
    public SimpleFieldError(final String field, final String primaryCode)
    {
        fField = field;
        fPrimaryCode = primaryCode;
    }
    
    public static SimpleFieldError fromFieldError(final FieldError fieldError)
    {
        return new SimpleFieldError(fieldError.getField(), fieldError.getCode());
    }

    public String getField()
    {
        return fField;
    }

    public String getPrimaryCode()
    {
        return fPrimaryCode;
    }
    
    @Override
    public boolean equals(final Object o)
    {
        if (o instanceof SimpleFieldError)
        {
            final SimpleFieldError other = (SimpleFieldError)o;

            return Objects.equals(this.fField, other.fField) &&
                    Objects.equals(this.fPrimaryCode, other.fPrimaryCode);
        }
        else
        {
            return false;
        }
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(fField, fPrimaryCode);
    }
    
    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("field", fField)
                .add("primaryCode", fPrimaryCode)
                .toString();
    }
}
