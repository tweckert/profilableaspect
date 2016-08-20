package javax.aspects.profiling;

import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Thomas Weckert
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/ProfilableAspectTests.xml" })
public class ProfilableAspectTests {
	
	@Autowired
	private ApplicationContext appCtx;
	
	@Test
	public void test1() throws Throwable {
		
		DummyBean dummyBean = appCtx.getBean("dummyBean", DummyBean.class);
		
		String result = dummyBean.doSomething("Hello, world!", 123, Collections.singletonMap("someKey", "someValue"));		
		dummyBean.doSomething(null, 123, null);
		
		Assert.assertTrue("finished".equals(result));
	}
	
}
