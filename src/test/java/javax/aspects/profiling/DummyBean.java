package javax.aspects.profiling;

import java.util.Map;

import javax.aspects.profiling.LogLevel;
import javax.aspects.profiling.Profilable;

/**
 * @author Thomas Weckert
 */
public class DummyBean {

	@Profilable(logLevel = LogLevel.DEBUG)
	public String doSomething(String arg1, int arg2, Map<?,?> arg3) throws Throwable {			
		Thread.sleep(500);			
		return "finished";
	}
	
}
