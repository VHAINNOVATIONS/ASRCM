package gov.va.med.srcalc.web.view.admin;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import gov.va.med.srcalc.domain.model.AbstractVariable;

/**
 * Contains tests verifying the {@link EditExistingVar} method contracts. Tests for
 * EditExistingVar implementations should use these tests to verify that the
 * implementations obey the contracts.
 */
public class EditExistingVarTest
{
    /**
     * Tests the basic contract of {@link EditExistingVar} methods.
     * @param impl an instance of some EditExistingVar implementation
     */
    public static final void testEditExistingVarMethods(final EditExistingVar impl)
    {
        final AbstractVariable target = impl.getTargetVariable();
        assertNotNull(target);
        assertSame(target, impl.applyToVariable());
        assertThat(impl.getEditViewName(), not(isEmptyOrNullString()));
    }
}
