package org.elearning.tests;

import org.exoplatform.component.test.AbstractKernelTest;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.component.test.ConfigurationUnit;
import org.exoplatform.component.test.ConfiguredBy;
import org.exoplatform.component.test.ContainerScope;


@ConfiguredBy({
  @ConfigurationUnit(scope = ContainerScope.PORTAL, path = "conf/exo.portal.component.portal-configuration.xml"),
  @ConfigurationUnit(scope = ContainerScope.PORTAL, path = "conf/exo.portal.component.test.jcr-configuration.xml"),
  @ConfigurationUnit(scope = ContainerScope.PORTAL, path = "conf/exo.portal.component.identity-configuration.xml"),
  @ConfigurationUnit(scope = ContainerScope.PORTAL, path = "conf/portal/test-portal-configuration.xml"),
  @ConfigurationUnit(scope = ContainerScope.PORTAL, path = "conf/portal/estudy.component.core.test.configuration.xml"),
  @ConfigurationUnit(scope = ContainerScope.PORTAL, path = "conf/portal/estudy.test.jcr-configuration.xml"),
  @ConfigurationUnit(scope = ContainerScope.PORTAL, path = "conf/portal/estudy.test.portal-configuration.xml")
})

public class GenericServiceTest extends AbstractKernelTest {
	protected static Log logger = ExoLogger.getLogger("org.elearning.tests");
	
	public void setUp() throws Exception{
		begin();
	}
	
	public void tearDown(){
		removeAllData();
		end();
	}
	
	private void removeAllData(){
		
	}
	
	@SuppressWarnings("unchecked")
	protected <T> T getService(Class<T> serviceClass){
		return (T) getContainer().getComponentInstanceOfType(serviceClass);
	}
}
