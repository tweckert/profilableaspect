package de.nexum.aspects.profiling;

import java.util.Map;

/**
 * @author <a href="mailto:thomas.weckert@nexum.de">Thomas Weckert</a>
 */
public class DummyBean {

	@Profilable(logLevel = LogLevel.DEBUG)
	public String doSomething(String arg1, int arg2, Map<?,?> arg3) throws Throwable {			
		Thread.sleep(500);			
		return "finished";
	}
	
}
