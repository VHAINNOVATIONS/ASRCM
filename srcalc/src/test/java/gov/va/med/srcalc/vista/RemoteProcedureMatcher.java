package gov.va.med.srcalc.vista;

import org.mockito.ArgumentMatcher;

public class RemoteProcedureMatcher extends ArgumentMatcher<RemoteProcedure>
{
	@Override
	public boolean matches(Object remoteProcedure)
	{
		return remoteProcedure instanceof RemoteProcedure;
	}
	
}
